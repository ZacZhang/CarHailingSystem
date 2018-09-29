package com.zac.driverclient;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {

    private long driverId;
    private double latitude;
    private double longitude;
    private long tripId;
    private int status;

    public Location() {

    }

    public Location(long driverId, double latitude, double longitude, int status, long tripId) {
        this.driverId = driverId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.tripId = tripId;
    }

    @JsonProperty
    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
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
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @JsonProperty
    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }
}
