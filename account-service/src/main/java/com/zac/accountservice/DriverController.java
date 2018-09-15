package com.zac.accountservice;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class DriverController {

    @Autowired
    private DriverRepository driverRepository;

    @RequestMapping(value = "/drivers", method = RequestMethod.POST)
    public ResponseEntity<Driver> create(@RequestBody(required = true) Driver inputDriver) {
        Driver driver = new Driver(inputDriver.firstName,
                inputDriver.lastName,
                inputDriver.address,
                inputDriver.phone, inputDriver.isActive);

        driverRepository.save(driver);

        return new ResponseEntity<>(driver, HttpStatus.CREATED);
    }

    @HystrixCommand(fallbackMethod = "defaultDrivers")
    @RequestMapping(value = "/drivers", method = RequestMethod.GET)
    public ResponseEntity<List<Driver>> getAll(@RequestParam(value = "firstName", defaultValue = "") String firstName,
                                               @RequestParam(value = "lastName", defaultValue = "") String lastName) {
        List<Driver> drivers = new ArrayList<>();

        if ("".equals(firstName) && "".equals(lastName)) {
            driverRepository.findAll().iterator().forEachRemaining(drivers :: add);
        } else if ("".equals(firstName)) {
            driverRepository.findByLastName(lastName).iterator().forEachRemaining(drivers :: add);
        } else if ("".equals(lastName)) {
            driverRepository.findByFirstName(firstName).iterator().forEachRemaining(drivers :: add);
        } else {
            driverRepository.findByFirstNameAndLastName(firstName, lastName).iterator().forEachRemaining(drivers :: add);
        }

        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }

    public ResponseEntity<List<Driver>> defaultDrivers(String firstName, String lastName) {
        List<Driver> drivers = new ArrayList<>();
        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }

    @HystrixCommand(fallbackMethod = "defaultDriver")
    @RequestMapping(value = "/drivers/{id}", method = RequestMethod.GET)
    public ResponseEntity<Driver> get(@PathVariable("id") String id) {
        Driver driver = null;
        Optional<Driver> optional = driverRepository.findById(Long.parseLong(id));

        if (!optional.isPresent()) {
            return new ResponseEntity<>(driver, HttpStatus.NOT_FOUND);
        }

        driver = optional.get();

        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    public ResponseEntity<Driver> defaultDriver(String id) {
        Driver driver = null;
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    @RequestMapping(value = "/drivers/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Driver> update(@PathVariable("id") String id,
                                         @RequestBody(required = true) Driver inputDriver) {
        Driver driver = null;
        Optional<Driver> optional = driverRepository.findById(Long.parseLong(id));

        if (!optional.isPresent()) {
            return new ResponseEntity<>(driver, HttpStatus.NOT_FOUND);
        }

        if (!optional.get().id.equals(inputDriver.id)) {
            return new ResponseEntity<>(driver, HttpStatus.BAD_REQUEST);
        }

        driver = new Driver(Long.parseLong(id),
                inputDriver.firstName,
                inputDriver.lastName,
                inputDriver.address,
                inputDriver.phone,
                inputDriver.isActive,
                optional.get().createdAt);

        driverRepository.save(driver);

        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    @RequestMapping(value = "/drivers/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Driver> delete(@PathVariable("id") String id) {
        Driver driver = null;
        Optional<Driver> optional = driverRepository.findById(Long.parseLong(id));

        if (!optional.isPresent()) {
            return new ResponseEntity<>(driver, HttpStatus.NOT_FOUND);
        }

        driverRepository.delete(optional.get());

        return new ResponseEntity<>(driver, HttpStatus.NO_CONTENT);
    }
}
