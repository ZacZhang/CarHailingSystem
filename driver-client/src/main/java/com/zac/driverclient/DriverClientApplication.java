package com.zac.driverclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

public class DriverClientApplication {

    public DriverClientApplication() {
    }

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(DriverClientApplication.class);
        RestTemplate restTemplate = new RestTemplate();
        Random random = new Random();
        int numOfDrivers = 6;

        int i;
        for(i = 1; i <= numOfDrivers; ++i) {
            String locationUrl = getLocationUrl((long)i);
            HttpEntity<Location> request = new HttpEntity(new Location((long)i, getRandomLatitude(), getRandomLongitude(), 0, 0L));
            restTemplate.postForObject(locationUrl, request, Location.class, new Object[0]);
        }

        for(i = 0; i < 100; ++i) {
            for(int driverId = 1; driverId <= numOfDrivers; ++driverId) {
                String locationUrl = getLocationUrl((long)driverId);

                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException var18) {
                    var18.printStackTrace();
                }

                Location location = (Location)restTemplate.getForObject(locationUrl, Location.class, new Object[0]);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                location.setLatitude(latitude + getRandomNumber(-0.01D, 0.01D));
                location.setLongitude(longitude + getRandomNumber(-0.01D, 0.01D));
                if (location.getTripId() != 0L) {
                    HttpEntity tripRequest;
                    if (location.getStatus() == 1) {
                        String tripUrl = getTripUrl(location.getTripId());
                        Trip trip = (Trip)restTemplate.getForObject(tripUrl, Trip.class, new Object[0]);
                        location.setStatus(2);
                        HttpEntity<Location> request = new HttpEntity(location);
                        location = (Location)restTemplate.postForObject(locationUrl, request, Location.class, new Object[0]);
                        logger.info("accepted trip " + location.getTripId() + " and updated driver location table");
                        trip.driverId = (long)driverId;
                        tripRequest = new HttpEntity(trip);
                        restTemplate.put(tripUrl, tripRequest, new Object[]{Trip.class});
                        logger.info("accepted trip " + location.getTripId() + " and updated Trip table");
                    } else if (location.getStatus() == 2) {
                        int randomNumber = random.nextInt(5);
                        if (randomNumber == 0) {
                            String tripUrl = getTripUrl(location.getTripId());
                            Trip trip = (Trip)restTemplate.getForObject(tripUrl, Trip.class, new Object[0]);
                            trip.status = 1;
                            tripRequest = new HttpEntity(trip);
                            restTemplate.put(tripUrl, tripRequest, new Object[]{Trip.class});
                            logger.info("completed trip " + trip.id + " and updated Trip table");
                            location.setStatus(0);
                            location.setTripId(0L);
                            HttpEntity<Location> request = new HttpEntity(location);
                            location = (Location)restTemplate.postForObject(locationUrl, request, Location.class, new Object[0]);
                            logger.info("completed trip " + trip.id + " and updated driver location table");
                        }
                    }
                } else {
                    HttpEntity<Location> request = new HttpEntity(location);
                    location = (Location)restTemplate.postForObject(locationUrl, request, Location.class, new Object[0]);
                }

                logger.info("updated driver " + driverId + " for " + i + " times");
            }
        }

    }

    private static String getLocationUrl(long driverId) {
        return "http://localhost:8096/services/location/drivers/" + driverId + "/location";
    }

    private static String getTripUrl(long tripId) {
        return "http://localhost:8096/services/dispatch/trips/" + tripId;
    }

    private static double getRandomNumber(double min, double max) {
        return min + (new Random()).nextDouble() * (max - min);
    }

    private static double getRandomLatitude() {
        double lowerLatitudeLimit = -90.0D;
        double upperLatitudeLimit = 90.0D;
        return getRandomNumber(lowerLatitudeLimit, upperLatitudeLimit);
    }

    private static double getRandomLongitude() {
        double lowerLongitudeLimit = -180.0D;
        double upperLongitudeLimit = 180.0D;
        return getRandomNumber(lowerLongitudeLimit, upperLongitudeLimit);
    }
}
