package com.zac.accountservice;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DriverRepository extends CrudRepository<Driver, Long> {
    List<Driver> findByFirstName(String firstName);
    List<Driver> findByLastName(String lastName);
    List<Driver> findByFirstNameAndLastName(String firstName, String lastName);
}
