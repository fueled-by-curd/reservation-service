package com.hotel.reservationsystem.controller;

import com.hotel.reservationsystem.controller.dtos.ReservationReq;
import com.hotel.reservationsystem.data.entity.Guest;
import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.controller.dtos.DateReq;
import com.hotel.reservationsystem.service.model.BookingService;
import org.hibernate.exception.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    @Autowired
    private BookingService bookingService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public Iterable<Reservation> getReservations(){
        return this.bookingService.getAllReservations();
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getReservationById(@PathVariable("bookingId") long bookingId){

        //System.out.println("Booking ID:"+ bookingId);

        Optional<Reservation> res = bookingService.getReservationById(bookingId);
        Map<String, String> body = new HashMap<>();

        if(res.isPresent()) {
            return ResponseEntity.ok().body(modelMapper.map(res.get(), ReservationReq.class));
        } else {
            body.put("error", "No reservation found with reservationId:" + bookingId );
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping
    public ResponseEntity<Object> makeReservation(@Valid @RequestBody ReservationReq req) throws Throwable {

        Reservation reservation = modelMapper.map(req, Reservation.class);

        Map<String, String> body = new HashMap<>();

        try {
            Reservation res = bookingService.createReservation(reservation);
        }

        catch(DataIntegrityViolationException err) {
            throw err.getCause();
        }
        return ResponseEntity.noContent().build();

    }
    @DeleteMapping("{bookingId}")
    public ResponseEntity<Object> cancelReservation(@PathVariable("bookingId") long bookingId){

        Map<String, String> body = new HashMap<>();
        if(bookingService.getReservationById(bookingId).isEmpty()){
            body.put("error","No reservation found with reservationId:" + bookingId);
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
        body.put("response","reservation:" + bookingId+" removed successfully");
        bookingService.removeReservation(bookingId);
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);

    }

    @PatchMapping("{bookingId}/date")
    public ResponseEntity<Object> changeReservationDate(@PathVariable("bookingId") long bookingId, @Valid @RequestBody DateReq dateReq){

        Map<String, String> body = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date ;
        try{
            date = sdf.parse(dateReq.getReservationDate());
        }
        catch (ParseException e){
            body.put("error","something went wrong. try again later");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
        Optional<Reservation> res = bookingService.updateReservationDate(date, bookingId);
        if(res.isEmpty()) {
            body.put("error","No reservation found with reservationId:" + bookingId);
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        body.put("response","reservation updated");
        return new ResponseEntity<>(body, HttpStatus.CREATED);
        //return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
