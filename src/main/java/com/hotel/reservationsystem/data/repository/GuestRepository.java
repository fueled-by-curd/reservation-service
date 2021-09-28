package com.hotel.reservationsystem.data.repository;

import com.hotel.reservationsystem.data.entity.Guest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends CrudRepository<Guest, Long> {
    @Override
    Optional<Guest> findById(Long aLong);

    Optional<Guest> findByEmailAddress(String value);

    Iterable<Guest> findByFirstName(String fName);
    Iterable<Guest> findByLastName(String fName);

}
