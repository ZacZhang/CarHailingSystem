package com.zac.accountservice;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "driver_generator")
    @SequenceGenerator(name = "driver_generator", sequenceName = "driver_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    public Long id;

    public String firstName;

    public String lastName;

    public String address;

    public String phone;

    public Boolean isActive;

    public LocalDateTime createdAt;

    public Driver() {

    }

    public Driver(Long id,
                  String firstName,
                  String lastName,
                  String address,
                  String phone,
                  Boolean isActive,
                  LocalDateTime createdAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public Driver(String firstName, String lastName, String address, String phone, Boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return String.format("Driver[id=%d, firstName='%s', lastName='%s', address='%s', phone='%s', isActive='%s', createdAt='%s']",
                id, firstName, lastName, address, phone, isActive, createdAt);
    }
}
