package com.zac.dispatchservice;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public interface LocationService {
    @RequestMapping(value = "/drivers/{id}/locations", method = RequestMethod.GET, produces = "application/json")
    List<Location> getDriverLocations(@PathVariable("id") String id);
}
