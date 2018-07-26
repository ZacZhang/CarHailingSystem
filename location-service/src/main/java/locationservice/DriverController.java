package locationservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class DriverController {

    private final AtomicLong counter = new AtomicLong();

    private static HashMap<String, Driver> drivers = new HashMap<>();

    @RequestMapping(value = "/drivers/{id}", method = RequestMethod.GET)
    public ResponseEntity<Driver> get(@PathVariable("id") String id) {
        Driver driver = null;
        if (!drivers.containsKey(id)) {
            return new ResponseEntity<>(driver, HttpStatus.BAD_REQUEST);
        } else {
            driver = drivers.get(id);
        }

        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    @RequestMapping(value = "/drivers", method = RequestMethod.POST)
    public ResponseEntity<Driver> create(@RequestBody(required = false) Driver driver) {
        long id = counter.incrementAndGet();
        driver.setId(id);

        drivers.put(String.valueOf(id), driver);
        return new ResponseEntity<>(driver, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/drivers", method = RequestMethod.GET)
    public ResponseEntity<List<Driver>> getAll() {
        return new ResponseEntity<>(new ArrayList<>(drivers.values()), HttpStatus.OK);
    }

    public static boolean isDriverValid(String id) {
        return drivers.containsKey(id);
    }
}
