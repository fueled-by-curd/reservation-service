package com.hotel.reservationsystem.controller;

import com.hotel.reservationsystem.controller.utils.BedInfo;
import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.data.entity.Room;
import com.hotel.reservationsystem.service.model.HotelRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/*
Unit Tests
 */
@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController{

    @Autowired
    private HotelRoomService hotelRoomService;
    @GetMapping
    public Iterable<Room> getRooms(){
        return this.hotelRoomService.getAllRooms();
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoomById(@PathVariable("roomId") long roomId){

        Optional<Room> res = hotelRoomService.getRoomByRoomId(roomId);

        if(res.isPresent()) {
            return ResponseEntity.ok().body(res.get());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/{roomId}/reservations")
    public Iterable<Reservation> getRoomReservations(@PathVariable("roomId") long roomId){
        return hotelRoomService.getAllRoomReservations(roomId);
    }

    @PatchMapping("{roomId}/bed")
    public ResponseEntity<Room> updateBedInfo(@PathVariable("roomId") long roomId, @RequestBody BedInfo bedInfo){

        Optional<Room> res = hotelRoomService.updateBed(bedInfo, roomId);
        if(res.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}