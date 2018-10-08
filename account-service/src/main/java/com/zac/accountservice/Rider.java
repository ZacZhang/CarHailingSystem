package com.zac.accountservice;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "riders")
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rider_generator")
    @SequenceGenerator(name = "rider_generator", sequenceName = "rider_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    public Long id;

    public String firstName;

    public String lastName;

    public String address;

    public String phone;

    public String email;

    public String payment;

    public LocalDateTime createAt;

    public Rider() {

    }

    public Rider(Long id,
                 String firstName,
                 String lastName,
                 String address,
                 String phone,
                 String email,
                 String payment,
                 LocalDateTime createAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.payment = payment;
        this.createAt = createAt;
    }

    public Rider(String firstName,
                 String lastName,
                 String address,
                 String phone,
                 String email,
                 String payment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.payment = payment;
        this.createAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("Rider[id=%d, firstName='%s', lastName='%s', address='%s', phone='%s', email='%s', " +
                        "payment='%s', createdAt='%s']",
                id, firstName, lastName, address, phone, email, payment, createAt);
    }
}
