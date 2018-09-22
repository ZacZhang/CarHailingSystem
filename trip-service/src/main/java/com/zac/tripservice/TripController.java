package com.zac.tripservice;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.jvm.hotspot.jdi.ThreadReferenceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @RequestMapping(value = "/trips", method = RequestMethod.POST)
    public ResponseEntity<Trip> create(@RequestBody(required = true) Trip inputTrip) {

        Trip trip = new Trip(inputTrip.driverId,
                inputTrip.riderId,
                inputTrip.origin,
                inputTrip.destination,
                inputTrip.status);

        tripRepository.save(trip);

        return new ResponseEntity<>(trip, HttpStatus.CREATED);
    }

    @HystrixCommand(fallbackMethod = "defaultTrips")
    @RequestMapping(value = "/trips", method = RequestMethod.GET)
    public ResponseEntity<List<Trip>> getAll(@RequestParam(value = "driverId", defaultValue = "") String driverId,
                                             @RequestParam(value = "riderId", defaultValue = "") String riderId) {

        List<Trip> trips = new ArrayList<>();

        if ("".equals(driverId) && "".equals(riderId)) {
            tripRepository.findAll().iterator().forEachRemaining(trips :: add);
        } else if ("".equals(driverId)) {
            long riderIdValue = Long.parseLong(riderId);
            tripRepository.findByRiderId(riderIdValue).iterator().forEachRemaining(trips :: add);
        } else if ("".equals(riderId)) {
            long driverIdValue = Long.parseLong(driverId);
            tripRepository.findByDriverId(driverIdValue).iterator().forEachRemaining(trips :: add);
        } else {
            long driverIdValue = Long.parseLong(driverId);
            long riderIdValue = Long.parseLong(riderId);
            tripRepository.findByDriverIdAndRiderId(driverIdValue, riderIdValue).iterator().forEachRemaining(trips :: add);
        }

        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    public ResponseEntity<List<Trip>> defaultTrips(String driverId, String riderId) {
        List<Trip> trips = new ArrayList<>();
        return new ResponseEntity<>(trips, HttpStatus.OK);
    }

    @HystrixCommand(fallbackMethod = "defaultTrip")
    @RequestMapping(value = "/trips/{id}", method = RequestMethod.GET)
    public ResponseEntity<Trip> get(@PathVariable("id") String id) {

        Trip trip = null;
        Optional<Trip> optional = tripRepository.findById(Long.parseLong(id));

        if (!optional.isPresent()) {
            return new ResponseEntity<>(trip, HttpStatus.NOT_FOUND);
        }

        trip = optional.get();

        return new ResponseEntity<>(trip, HttpStatus.OK);
    }

    public ResponseEntity<Trip> defaultTrip(String id) {
        Trip trip = null;
        return new ResponseEntity<>(trip, HttpStatus.OK);
    }

    @RequestMapping(value = "/trips/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Trip> update(@PathVariable("id") String id,
                                       @RequestBody(required = true) Trip inputTrip) {
        Trip trip = null;
        Optional<Trip> optional = tripRepository.findById(Long.parseLong(id));

        if (!optional.isPresent()) {
            return new ResponseEntity<>(trip, HttpStatus.NOT_FOUND);
        }

        if (optional.get().id != inputTrip.id) {
            return new ResponseEntity<>(trip, HttpStatus.BAD_REQUEST);
        }

        // keep the original createdAt time
        trip = new Trip(Long.parseLong(id),
                inputTrip.driverId,
                inputTrip.riderId,
                inputTrip.origin,
                inputTrip.destination,
                inputTrip.status,
                optional.get().createAt);

        tripRepository.save(trip);

        return new ResponseEntity<>(trip, HttpStatus.OK);
    }
}
