package com.zac.locationservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class LocationServiceController {

    private final AtomicLong counter = new AtomicLong();
    private Random random = new Random();

    // key: driver Id, value: a list of driver locations
    private HashMap<String, Location> locationMap = new HashMap<>();

    @RequestMapping(value = "/drivers/{id}/location", method = RequestMethod.POST)
    public ResponseEntity<Location> create(@PathVariable("id") String id,
                                           @RequestBody(required = false) Location inputLocation) {
        Location location;

        if (inputLocation == null) {
            location = new Location(random.nextInt(90), random.nextInt(90));
        } else {
            location = new Location(inputLocation.getLatitude(), inputLocation.getLongitude());
        }

        locationMap.put(id, location);
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/drivers/{id}/locations", method = RequestMethod.GET)
    public ResponseEntity<List<Location>> getAll(@PathVariable("id") String id) {
        List<Location> locations = null;

        // TODO: check if driver is valid

        Location location = locationMap.get(id);

        if (location == null) {
            return new ResponseEntity<>(locations, HttpStatus.OK);
        }

        locations = new ArrayList<>();
        locations.add(location);

        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @RequestMapping(value = "/drivers/{id}/locations}", method = RequestMethod.GET)
    public ResponseEntity<Location> get(@PathVariable("id") String id) {
        Location location = null;

        // TODO: check if driver is valid

        location = locationMap.get(id);

        if (location == null) {
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(location, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<Location> findNearestDriver(@RequestParam(value = "locHash", defaultValue = "") String locHash) {
        throw new NotImplementedException();
    }
}
