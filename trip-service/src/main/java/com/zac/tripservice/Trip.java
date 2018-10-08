package com.zac.tripservice;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trip_generator")
    @SequenceGenerator(name = "trip_generator", sequenceName = "trip_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    public long driverId;

    public long riderId;

    public String origin;

    public String destination;

    public int status;

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;

    public Trip() {

    }

    // called by the update function
    public Trip(long id,
                long driverId,
                long riderId,
                String origin,
                String destination,
                int status,
                LocalDateTime createdAt) {
        this.id = id;
        this.driverId = driverId;
        this.riderId = riderId;
        this.origin = origin;
        this.destination = destination;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = LocalDateTime.now();
    }

    // called by the create function
    public Trip(long driverId,
                long riderId,
                String origin,
                String destination,
                int status) {
        this.driverId = driverId;
        this.riderId = riderId;
        this.origin = origin;
        this.destination = destination;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("Trip[id=%d,driverId='%s',riderId='%s',origin='%s',destination='%s',status='%s',updatedAt='%s',createdAt='%s]",
                id, driverId, riderId, origin, destination, status, updatedAt, createdAt);
    }
}
