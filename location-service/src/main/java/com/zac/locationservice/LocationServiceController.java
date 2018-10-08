package com.zac.locationservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class LocationServiceController {

    private final AtomicLong counter = new AtomicLong();
    private Random random = new Random();

    // key: driver Id
    // value: driver's location
    // only save the last location for each driver
    private HashMap<String, Location> idToLocationMap = new HashMap<>();

    // key: driver location's GeoHash value
    // value: driver Id
    private TreeMap<String, String> geohashToIdMap = new TreeMap<>();

    @RequestMapping(value = "/drivers/{id}/location", method = RequestMethod.POST)
    public ResponseEntity<Location> createOrUpdate(@PathVariable("id") String id,
                                           @RequestBody(required = false) Location inputLocation) {
        Location location;

        if (inputLocation == null) { // for debugging purpose only
            location = new Location(Long.parseLong(id),
                    random.nextInt(90),
                    random.nextInt(90),
                    0,
                    0);
        } else {
            // todo: check if the driver is valid and still active

            location = new Location(
                    Long.parseLong(id),
                    inputLocation.getLatitude(),
                    inputLocation.getLongitude(),
                    inputLocation.getStatus(),
                    inputLocation.getTripId());
        }

        idToLocationMap.put(id, location);
        geohashToIdMap.put(location.getGeohash(), id);

        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

//    @RequestMapping(value = "/drivers/{id}/locations", method = RequestMethod.GET)
//    public ResponseEntity<List<Location>> getAll(@PathVariable("id") String id) {
//        List<Location> locations = null;
//
//        // TODO: check if driver is valid
//
//        Location location = idToLocationMap.get(id);
//
//        if (location == null) {
//            return new ResponseEntity<>(locations, HttpStatus.OK);
//        }
//
//        locations = new ArrayList<>();
//        locations.add(location);
//
//        return new ResponseEntity<>(locations, HttpStatus.OK);
//    }

    @RequestMapping(value = "/drivers/{id}/location}", method = RequestMethod.GET)
    public ResponseEntity<Location> get(@PathVariable("id") String id) {

        Location location;

        // TODO: check if driver is valid and still active

        location = idToLocationMap.get(id);

        if (location == null) {
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    // given a rider's location in GeoHash format and the expiration in seconds
    // return the nearest driver who has an updated location that's not expired
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<Location> findNearestDriver(
            @RequestParam(value = "locationHash", defaultValue = "") String locationHash,
            @RequestParam(value = "expirationInSec", defaultValue = "") String expirationInSec) {

        // find the nearest one using GeoHash search

        System.out.println("Trying to find driver for location: "+locationHash+" with expiration in "+expirationInSec+" seconds");

        Map.Entry<String, String> low = geohashToIdMap.floorEntry(locationHash);
        Map.Entry<String, String> high = geohashToIdMap.ceilingEntry(locationHash);

        LocalDateTime validTillTime = LocalDateTime.now().minusSeconds(Long.parseLong(expirationInSec));

        // find a low and high which are still valid

        // if the location has expired or driver is busy, keep searching
        while (low != null &&
                ((idToLocationMap.get(low.getValue())).getTimestamp().isBefore(validTillTime) ||
                        (idToLocationMap.get(low.getValue())).getStatus() != 0) ) {
            low = geohashToIdMap.lowerEntry(low.getKey());
        }
        while (high != null &&
                ((idToLocationMap.get(high.getValue())).getTimestamp().isBefore(validTillTime) ||
                        (idToLocationMap.get(high.getValue())).getStatus() != 0) ) {
            high = geohashToIdMap.higherEntry(high.getKey());
        }

        // pick the location closer to the target
        if (low != null && high != null) {
            Location closerLoc = findCloser(idToLocationMap.get(low.getValue()),
                    idToLocationMap.get(high.getValue()),
                    locationHash);

            return new ResponseEntity<>(closerLoc, HttpStatus.OK);
        }

        if (low != null) {
            return new ResponseEntity<>(idToLocationMap.get(low.getValue()), HttpStatus.OK);
        } else if (high != null) {
            return new ResponseEntity<>(idToLocationMap.get(high.getValue()), HttpStatus.OK);
        } else {
            Location dummy = null;
            return new ResponseEntity<>(dummy, HttpStatus.NOT_FOUND);
        }
    }

    private Location findCloser(Location locA, Location locB, String targetGeoHash) {
        double[] a = GeoHashUtils.decode(locA.getGeohash());
        double[] b = GeoHashUtils.decode(locB.getGeohash());
        double[] target = GeoHashUtils.decode(targetGeoHash);

        if (dist(a, target) < dist(b, target)) {
            return locA;
        } else {
            return locB;
        }
    }

    private double dist(double[] a, double[] b) {
        return Math.pow(a[0] - b[0], 2) + Math.pow(a[1] - b[1], 2);
    }

    // show all driver locations. Internal debugging use only
    @RequestMapping(value = "/driverLocations", method = RequestMethod.GET)
    public ResponseEntity<List<Location>> showAllDriverLocations() throws InterruptedException {

        // add some arbitrary delay so the server send/client receive order is natural in Zipkin
        Thread.sleep(500);

        return new ResponseEntity<>(new ArrayList<>(idToLocationMap.values()), HttpStatus.OK);
    }
}
