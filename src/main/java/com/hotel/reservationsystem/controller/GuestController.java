package com.hotel.reservationsystem.controller;

import com.hotel.reservationsystem.controller.dtos.GuestReq;
import com.hotel.reservationsystem.data.entity.Guest;
import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.service.model.BookingService;
import com.hotel.reservationsystem.service.model.HotelGuestService;
import com.hotel.reservationsystem.service.model.HotelRoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/guests")
public class GuestController {
    @Autowired
    private HotelGuestService hotelGuestService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping
    public Iterable<Guest> getGuests(){
        return hotelGuestService.getAllGuests();
    }

    @GetMapping("/{guestId}")
    public ResponseEntity<Object> getGuest(@PathVariable("guestId") long guestId){

        Optional<Guest> res = hotelGuestService.getGuestById(guestId);

        if(res.isPresent()) {
            return ResponseEntity.ok().body(res.get());
        } else {
            Map<String, String> body = new HashMap<>();
            body.put("error", "No guest found with guestId:" + guestId );
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{guestId}/reservations")
    public ResponseEntity<Object> getGuestReservations(@PathVariable("guestId") long guestId){
        if(hotelGuestService.getGuestById(guestId).isEmpty()) {

            Map<String, String> body = new HashMap<>();
            body.put("error", "No guest found with guestId:" + guestId );
            return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(hotelGuestService.getAllGuestReservations(guestId), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Object> addGuest(@Valid @RequestBody GuestReq req){

        //System.out.println("inside post req");
        Guest guest = modelMapper.map(req, Guest.class);
        Map<String, String> body = new HashMap<>();

        try {
            Guest res = hotelGuestService.createGuest(guest);
        }
        catch(DataIntegrityViolationException e ) {
            body.put("error", "email already taken");
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{guestId}")
    public ResponseEntity<Object> removeGuest(@PathVariable("guestId") long guestId){

        if(hotelGuestService.getGuestById(guestId).isEmpty()) {

            Map<String, String> body = new HashMap<>();
            body.put("error", "No guest found with guestId:" + guestId );
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
        hotelGuestService.deleteGuest(guestId);
        Map<String,String> body = new HashMap<>();
        body.put("response","guest:" + guestId +" removed successfully");
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }

    @PutMapping("{guestId}")
    public ResponseEntity<Object> updateGuest(@RequestBody Guest guest, @PathVariable("guestId") long guestId){


        if(hotelGuestService.getGuestById(guestId).isEmpty()) {

            Map<String, String> body = new HashMap<>();
            body.put("error", "No guest found with guestId:" + guestId );
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
        Optional<Guest> res = hotelGuestService.updateGuestInfo(guest, guestId);


        return ResponseEntity.accepted().body(res.get());
    }

    @GetMapping("/namefetch")
    public ResponseEntity<Object> findGuestByName(@RequestParam("name") String name){

        System.out.println("Inside findByName");
        Iterable<Guest> res = hotelGuestService.findByName(name);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
