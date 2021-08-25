package com.hotel.reservationsystem.data.repository;


import com.hotel.reservationsystem.data.entity.Reservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    Iterable<Reservation> findByGuestId(long guestId);
    Iterable<Reservation> findByRoomId(long roomId);

}
