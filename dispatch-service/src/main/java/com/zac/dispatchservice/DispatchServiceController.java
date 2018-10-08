package com.zac.dispatchservice;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RefreshScope
@RestController
public class DispatchServiceController {

    @Autowired
    private LocationServiceFeignClient locationServiceFeignClient;

    @Autowired
    private TripServiceFeignClient tripServiceFeignClient;

    @Autowired
    private OrderServiceFeignClient orderServiceFeignClient;

    @Value("${location.expirationInSec}")
    private String expirationInSec;

    @Value("${location.numOfNearestDrivers}")
    private String numOfNearestDrivers;

    // Methods for LocationService
    @HystrixCommand(fallbackMethod = "getDefaultLocationViaFeign")
    @RequestMapping(value = "/drivers/{id}/location", method = RequestMethod.GET)
    public ResponseEntity<Location> getLocationViaFeign(
            @PathVariable("id") String id) {

        // Invoke Feign client
        Location location = this.locationServiceFeignClient.getDriverLocation(id);

        if (location == null) {
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(location, HttpStatus.OK);
        }
    }

    public ResponseEntity<Location> getDefaultLocationViaFeign(
            String id) {
        Location location = null;
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    private void findAndSetDriver(Trip trip) {
        Location location = null;
        try {
            location =
                    this.locationServiceFeignClient.findNearestDriver(
                            trip.origin,
                            this.expirationInSec);
        }catch(FeignException fe){
            // Do nothing. Exception would be thrown when no driver is found
            System.out.println("FeignException caught!");
        }

        if(location != null) {
            // Set the trip id to the driver's location
            location.setTripId(trip.id);
            location.setStatus(1); // 1 means pending driver acceptance

            this.locationServiceFeignClient.createOrUpdate(
                    String.valueOf(location.getDriverId()), location);
        }
    }

    // Methods for TripService

    // Called by rider to create a new trip
    @RequestMapping(value = "/trips", method = RequestMethod.POST)
    public ResponseEntity<Trip> createTripViaFeign(
            @RequestBody(required = true) Trip inputTrip
    ) {
        Trip trip = this.tripServiceFeignClient.create(inputTrip);

        if (trip == null) {
            return new ResponseEntity<>(trip, HttpStatus.BAD_REQUEST);
        } else {
            // Find a nearest driver
            this.findAndSetDriver(trip);

            return new ResponseEntity<>(trip, HttpStatus.OK);
        }
    }

    // Returns all trips or filtered by driver and/or rider IDs
    @RequestMapping(value = "/trips", method = RequestMethod.GET)
    public ResponseEntity<List<Trip>> getTripsViaFeign(
            @RequestParam(value = "driverId", defaultValue = "") String driverId,
            @RequestParam(value = "riderId", defaultValue = "") String riderId) {

        List<Trip> trips = this.tripServiceFeignClient.getAll(driverId, riderId);

        if (trips == null) {
            return new ResponseEntity<>(trips, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(trips, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/trips/{id}", method = RequestMethod.GET)
    public ResponseEntity<Trip> getTripViaFeign(
            @PathVariable("id") String id) {

        Trip trip = this.tripServiceFeignClient.get(id);

        if (trip == null) {
            return new ResponseEntity<>(trip, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(trip, HttpStatus.OK);
        }
    }

    // Called by driver to accept the trip or complete the trip
    @RequestMapping(value = "/trips/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Trip> updateTripViaFeign(
            @PathVariable("id") String id,
            @RequestBody(required = true) Trip inputTrip) {

        Trip trip = this.tripServiceFeignClient.update(id, inputTrip);

        if (trip == null) {
            return new ResponseEntity<>(trip, HttpStatus.BAD_REQUEST);
        } else {
            if(trip.status == 1){
                Order order = this.createOrderViaFeign(trip);
                if(order != null) {
                    System.out.println("Order created for trip: " + trip.id + " order id: " + order.id);
                }else{
                    System.out.println("Order creation failed for trip: " + trip.id);
                }
            }

            return new ResponseEntity<>(trip, HttpStatus.OK);
        }
    }

    // Called by rider to check if a trip has an assigned driver, if not
    // dispatchService will find another driver and repeat the process
    @RequestMapping(value = "/trips/{id}/check", method = RequestMethod.POST)
    public ResponseEntity<Trip> checkTrip(
            @PathVariable("id") String id) {

        Trip trip = this.tripServiceFeignClient.get(id);

        if (trip == null) {
            return new ResponseEntity<>(trip, HttpStatus.BAD_REQUEST);
        } else {

            if (trip.driverId != 0) {
                return new ResponseEntity<>(trip, HttpStatus.OK);
            } else {
                // Find a nearest driver again
                this.findAndSetDriver(trip);
                return new ResponseEntity<>(trip, HttpStatus.OK);
            }
        }
    }

    // Methods for OrderService

    // private method, called by DispatchService internally
    private Order createOrderViaFeign(
            @RequestBody(required = true) Trip inputTrip
    ) {
        Order order = this.orderServiceFeignClient.create(
                new Order(inputTrip.id, 0));

        return order;
    }

    // Returns all trips or filtered by driver and/or rider IDs
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getOrdersViaFeign(
            @RequestParam(value = "tripId", defaultValue = "") String tripId) {

        List<Order> orders = this.orderServiceFeignClient.getAll(tripId);

        if (orders == null) {
            return new ResponseEntity<>(orders, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
    public ResponseEntity<Order> getOrderViaFeign(
            @PathVariable("id") String id) {

        Order order = this.orderServiceFeignClient.get(id);

        if (order == null) {
            return new ResponseEntity<>(order, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
    }
}
