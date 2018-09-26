package com.zac.locationservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.time.LocalDateTime;

public class Location {

    private long driverId;
    private long tripId;
    private int status;
    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;

    public Location() {

    }

    public Location(long driverId, double latitude, double longitude, int status, long tripId) {
        this.driverId = driverId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.tripId = tripId;
        this.timestamp = LocalDateTime.now();
    }


    @JsonProperty
    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    @JsonProperty
    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    @JsonProperty
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @JsonProperty
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @JsonProperty
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @JsonProperty
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getGeohash() {
        return GeoHashUtils.encode(latitude, longitude);
    }
}
