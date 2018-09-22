package com.zac.tripservice;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TripRepository extends CrudRepository<Trip, Long> {
    List<Trip> findByDriverId(long driverId);
    List<Trip> findByRiderId(long riderId);
    List<Trip> findByDriverIdAndRiderId(long driverId, long riderId);
}
