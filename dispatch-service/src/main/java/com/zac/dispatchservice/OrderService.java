package com.zac.dispatchservice;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface OrderService {

    @RequestMapping(value = "/orders", method = RequestMethod.POST, produces = "application/json")
    Order create(@RequestBody(required = true) Order inputOrder);

    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    List<Order> getAll(@RequestParam(value = "tripId", defaultValue = "") String tripId);

    @RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
    Order get(@PathVariable("id") String id);

    @RequestMapping(value = "/orders/{id}", method = RequestMethod.PUT)
    Order update(@PathVariable("id") String id, @RequestBody(required = true) Order inputOrder);
}
