package com.hotel.reservationsystem.controller;

import com.hotel.reservationsystem.data.entity.Guest;
import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.service.model.BookingService;
import com.hotel.reservationsystem.service.model.HotelGuestService;
import com.hotel.reservationsystem.service.model.HotelRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/guests")
public class GuestController {
    @Autowired
    private HotelGuestService hotelGuestService;

    @GetMapping
    public Iterable<Guest> getGuests(){
        return hotelGuestService.getAllGuests();
    }

    @GetMapping("/{guestId}")
    public ResponseEntity<Guest> getGuest(@PathVariable("guestId") long guestId){

        Optional<Guest> res = hotelGuestService.getGuestById(guestId);

        if(res.isPresent()) {
            return ResponseEntity.ok().body(res.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{guestId}/reservations")
    public Iterable<Reservation> getGuestReservations(@PathVariable("guestId") long guestId){
        return hotelGuestService.getAllGuestReservations(guestId);
    }
    @PostMapping
    public ResponseEntity<Guest> addGuest(@RequestBody Guest guest){

        //System.out.println("inside post req");
        Guest res = hotelGuestService.createGuest(guest);
        if(res == null ) return ResponseEntity.badRequest().build();

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{guestId}")
    public ResponseEntity<Guest> removeGuest(@PathVariable("guestId") long guestId){

        if(hotelGuestService.getGuestById(guestId).isEmpty()) return ResponseEntity.badRequest().build();
        hotelGuestService.deleteGuest(guestId);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("{guestId}")
    public ResponseEntity<Guest> updateGuest(@RequestBody Guest guest, @PathVariable("guestId") long guestId){

        //System.out.println("inside post req");
        Optional<Guest> res = hotelGuestService.updateGuestInfo(guest, guestId);

        if(!res.isPresent()) return ResponseEntity.badRequest().build();

        return ResponseEntity.accepted().body(res.get());
    }
}
