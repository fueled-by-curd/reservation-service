package com.hotel.reservationsystem.service.impl;

import com.hotel.reservationsystem.controller.models.DateReq;
import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.data.repository.ReservationRepository;
import com.hotel.reservationsystem.service.model.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservationService implements BookingService {
    @Autowired
    ReservationRepository reservationRepository;

    @Override
    public Iterable<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Optional<Reservation> getReservationById(long bookingId) {
        return reservationRepository.findById(bookingId);

    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public void removeReservation(long bookingId) {
        reservationRepository.deleteById(bookingId);
    }

    @Override
    public Optional<Reservation> updateReservationDate(DateReq dateReq, long bookingId) {
        Optional<Reservation> res = reservationRepository.findById(bookingId);

        if(res.isEmpty()) return res;

        res.get().setReservationDate(dateReq.getReservationDate());
        reservationRepository.save(res.get());
        return res;


    }
}
