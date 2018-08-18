package com.zac.dispatchservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DispatchServiceController {

    @Autowired
    private LocationServiceFeignClient locationServiceFeignClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    // Feign
    @RequestMapping(value = "/drivers/v1/{id}/locations/{locationId}", method = RequestMethod.GET)
    public ResponseEntity<Location> getLocationViaFeign(@PathVariable("id") String id,
                                                        @PathVariable("locationId") String locationId) {
        // Invoke Feign client
        Location location = this.locationServiceFeignClient.getDriverLocation(id, locationId);

        if (location == null) {
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(location, HttpStatus.OK);
        }
    }

    // RestTemplate
    @RequestMapping(value = "/drivers/v2/{id}/locations/{locationId}", method = RequestMethod.GET)
    public ResponseEntity<Location> getLocationViaRestTemplate(@PathVariable("id") String id,
                                                               @PathVariable("locationId") String locationId) {
        List<ServiceInstance> instances = this.discoveryClient.getInstances("location-service");
        String host = instances.get(0).getHost();
        int port = instances.get(0).getPort();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Location> resp = restTemplate.getForEntity(
                "http://{host}:{port}/drivers/{id}/locations/{locationId}",
                Location.class,
                host,
                port,
                id,
                locationId);

        return resp;

        /*
        Location location = restTemplate.getForObject(
                "http://{host}:{port}/drivers/{id}/locations/{locationId}",
                Location.class,
                host,
                port,
                id,
                locationId);

        return new ResponseEntity<>(location, HttpStatus.OK);
        */
    }

    // WebClient
    public ResponseEntity<Location> getLocationViaWebClient(@PathVariable("id") String id,
                                                            @PathVariable("locationId") String locationId)
            throws InterruptedException{

        List<ServiceInstance> instances = this.discoveryClient.getInstances("location-service");
        String host = instances.get(0).getHost();
        int port = instances.get(0).getPort();

        String path = String.format("http://%s:%s/drivers/%s/locations/%s", host, port, id, locationId);

        WebClient webClient = WebClient.create(path);
        Mono<Location> result = webClient.get().retrieve().bodyToMono(Location.class);

        CountDownLatch latch = new CountDownLatch(1);
        final Location[] location = new Location[1];
        result.subscribe((loc) -> {
            latch.countDown();
            System.out.println("Received...");
            location[0] = loc;
        });

        while (latch.getCount() == 1) {
            System.out.println("busy doing something");
            Thread.sleep(500);
        }

        return new ResponseEntity<>(location[0], HttpStatus.OK);
    }
}
