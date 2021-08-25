package com.hotel.reservationsystem.data.repository;

import com.hotel.reservationsystem.data.entity.Guest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuestRepository extends CrudRepository<Guest, Long> {
    //Iterable<Guest> findAllById(long guestId);

    @Override
    Optional<Guest> findById(Long aLong);
}
