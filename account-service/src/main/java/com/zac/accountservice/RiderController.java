package com.zac.accountservice;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RiderController {

    @Autowired
    private RiderRepository riderRepository;

    @RequestMapping(value = "/riders", method = RequestMethod.POST)
    public ResponseEntity<Rider> create(@RequestBody(required = true) Rider inputRider) {
        System.out.println(111);
        Rider rider = new Rider(
                inputRider.firstName,
                inputRider.lastName,
                inputRider.address,
                inputRider.phone,
                inputRider.email,
                inputRider.payment);

        System.out.println(222);

        riderRepository.save(rider);

        System.out.println(333);

        return new ResponseEntity<>(rider, HttpStatus.CREATED);
    }

    @HystrixCommand(fallbackMethod = "defaultRiders")
    @RequestMapping(value = "/riders", method = RequestMethod.GET)
    public ResponseEntity<List<Rider>> getAll(@RequestParam(value = "firstName", defaultValue = "") String firstName,
                                              @RequestParam(value = "lastName", defaultValue = "") String lastName) {
        List<Rider> riders = new ArrayList<>();

        if ("".equals(firstName) && "".equals(lastName)) {
            riderRepository.findAll().iterator().forEachRemaining((riders :: add));
        } else if ("".equals(firstName)) {
            riderRepository.findByLastName(lastName).iterator().forEachRemaining(riders :: add);
        } else if ("".equals(lastName)) {
            riderRepository.findByFirstName(firstName).iterator().forEachRemaining(riders :: add);
        } else {
            riderRepository.findByFirstNameAndLastName(firstName, lastName).iterator().forEachRemaining(riders :: add);
        }

        return new ResponseEntity<>(riders, HttpStatus.OK);
    }

    public ResponseEntity<List<Rider>> defaultRiders(String firstName, String lastName) {
        List<Rider> riders = new ArrayList<>();
        Rider xxx = new Rider(
                "minsun",
                "kim",
                "123 erb st",
                "123-456-7890",
                 "minsunstory@gmail.com",
                "alipay");
        riders.add(xxx);
        return new ResponseEntity<>(riders, HttpStatus.OK);
    }

    @RequestMapping(value = "/riders/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Rider> update(@PathVariable("id") String id,
                                        @RequestBody(required = true) Rider inputRider) {
        Rider rider = null;
        Optional<Rider> optional = riderRepository.findById(Long.parseLong(id));

        if (!optional.isPresent()) {
            return new ResponseEntity<>(rider, HttpStatus.NOT_FOUND);
        }

        if (!optional.get().id.equals(inputRider.id)) {
            return new ResponseEntity<>(rider, HttpStatus.BAD_REQUEST);
        }

        // keep the origin createdAt time
        rider = new Rider(Long.parseLong(id),
                inputRider.firstName,
                inputRider.lastName,
                inputRider.address,
                inputRider.phone,
                inputRider.email,
                inputRider.payment,
                optional.get().createdAt);

        riderRepository.save(rider);

        return new ResponseEntity<>(rider, HttpStatus.OK);
    }

    @RequestMapping(value = "/riders/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Rider> delete(@PathVariable("id") String id) {
        Rider rider = null;
        Optional<Rider> optional = riderRepository.findById(Long.parseLong(id));

        if (!optional.isPresent()) {
            return new ResponseEntity<>(rider, HttpStatus.NOT_FOUND);
        }

        riderRepository.delete(optional.get());

        return new ResponseEntity<>(rider, HttpStatus.NO_CONTENT);
    }
}
