package com.zac.orderservice;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name = "order_generator", sequenceName = "order_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    public long id;

    public long tripId;

    public int status;

    public LocalDateTime createdAt;

    public LocalDateTime updatedAt;

    public Order() {

    }

    // called by the update function
    public Order(long id, long tripId, int status, LocalDateTime createdAt) {
        this.id = id;
        this.tripId = tripId;
        this.status = status;
        // update the timestamp
        this.updatedAt = LocalDateTime.now();
        this.createdAt = createdAt;
    }

    // called by the create function
    public Order(long tripId, int status) {
        this.tripId = tripId;
        this.status = status;
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("Order[id=%d, tripId='%s', status='%s', updatedAt='%s', createdAt='%s']",
                id, tripId, status, updatedAt, createdAt);
    }
}
