package com.zac.dispatchservice;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface LocationService {
    @RequestMapping(value = "/drivers/{id}/location", method = RequestMethod.GET, produces = "application/json")
    Location getDriverLocation(@PathVariable("id") String id);

    @RequestMapping(value = "/drivers/{id}/location", method = RequestMethod.POST)
    Location createOrUpdate(@PathVariable("id") String id, @RequestBody(required = false) Location inputLocation);

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    Location findNearestDriver(@RequestParam(value = "locationHash", defaultValue = "") String locationHash,
                               @RequestParam(value = "expirationInSec", defaultValue = "") String expirationInSec);
}
