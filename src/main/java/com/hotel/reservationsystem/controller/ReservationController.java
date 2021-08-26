package com.hotel.reservationsystem.controller;

import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.controller.utils.DateReq;
import com.hotel.reservationsystem.service.model.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    @Autowired
    BookingService bookingService;

    @GetMapping
    public Iterable<Reservation> getReservations(){
        return this.bookingService.getAllReservations();
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("bookingId") long bookingId){

        //System.out.println("Booking ID:"+ bookingId);

        Optional<Reservation> res = bookingService.getReservationById(bookingId);

        if(res.isPresent()) {
            return ResponseEntity.ok().body(res.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping
    public ResponseEntity<Reservation> makeReservation(@RequestBody Reservation reservation){

        Reservation res = bookingService.createReservation(reservation);
        if(res == null ) return ResponseEntity.badRequest().build();

        return ResponseEntity.noContent().build();

    }
    @DeleteMapping("{bookingId}")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable("bookingId") long bookingId){

        if(bookingService.getReservationById(bookingId).isEmpty()) return ResponseEntity.badRequest().build();
        bookingService.removeReservation(bookingId);
        return ResponseEntity.accepted().build();
    }

    @PatchMapping("{bookingId}/date")
    public ResponseEntity<Reservation> changeReservationDate(@PathVariable("bookingId") long bookingId, @RequestBody DateReq dateReq){

        Optional<Reservation> res = bookingService.updateReservationDate(dateReq, bookingId);
        if(res.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
