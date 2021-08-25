package com.hotel.reservationsystem.service.model;

import com.hotel.reservationsystem.controller.models.DateReq;
import com.hotel.reservationsystem.data.entity.Reservation;

import java.util.Optional;

public interface BookingService {
    Iterable<Reservation> getAllReservations();

    Optional<Reservation> getReservationById(long bookingId);

    Reservation createReservation(Reservation reservation);

    void removeReservation(long bookingId);

    Optional<Reservation> updateReservationDate(DateReq dateReq, long bookingId);
}
