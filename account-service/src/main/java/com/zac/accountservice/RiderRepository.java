package com.zac.accountservice;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RiderRepository extends CrudRepository<Rider, Long> {

    List<Rider> findByFirstName(String firstName);
    List<Rider> findByLastName(String lastName);
    List<Rider> findByFirstNameAndLastName(String firstName, String lastName);
}
