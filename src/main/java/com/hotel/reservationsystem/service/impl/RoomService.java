package com.hotel.reservationsystem.service.impl;

import com.hotel.reservationsystem.controller.models.BedInfo;
import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.data.entity.Room;
import com.hotel.reservationsystem.data.repository.GuestRepository;
import com.hotel.reservationsystem.data.repository.ReservationRepository;
import com.hotel.reservationsystem.data.repository.RoomRepository;
import com.hotel.reservationsystem.service.model.HotelRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomService implements HotelRoomService {
    GuestRepository guestRepository;
    RoomRepository roomRepository;
    ReservationRepository reservationRepository;

    @Autowired
    public RoomService(GuestRepository guestRepository, RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Optional<Room> getRoomByRoomId(long roomId) {
        return roomRepository.findById(roomId);
    }

    @Override
    public Iterable<Reservation> getAllRoomReservations(long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    @Override
    public Iterable<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Optional<Room> updateBed(BedInfo bedInfo, long roomId) {
        Optional<Room> res = roomRepository.findById(roomId);

        if(res.isEmpty()) return res;

        res.get().setBedInfo(bedInfo.getBedInfo());
        roomRepository.save(res.get());
        return res;
    }
}
