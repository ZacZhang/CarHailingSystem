package com.zac.riderclient;

import java.time.LocalDateTime;

public class Rider {

    public Long id;

    public String firstName;

    public String lastName;

    public String address;

    public String phone;

    public String email;

    public String payment;

    public LocalDateTime createdAt;

    public Rider() {

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
    }

    @Override
    public String toString() {
        return String.format("Rider[firstName='%s', lastName='%s', address='%s', phone='%s', email='%s', payment='%s']",
                firstName, lastName, address, phone, email, payment);
    }
}
