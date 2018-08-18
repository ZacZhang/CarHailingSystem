package com.zac.dispatchservice;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface LocationService {
    @RequestMapping(value = "/drivers/{id}/locations/{locationId}", method = RequestMethod.GET, produces = "application/json")
    Location getDriverLocation(@PathVariable("id") String id, @PathVariable("locationId") String locationId);
}
