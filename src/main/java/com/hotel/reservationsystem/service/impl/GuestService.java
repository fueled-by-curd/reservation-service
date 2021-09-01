package com.hotel.reservationsystem.service.impl;

import com.hotel.reservationsystem.data.entity.Guest;
import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.data.repository.GuestRepository;
import com.hotel.reservationsystem.data.repository.ReservationRepository;
import com.hotel.reservationsystem.data.repository.RoomRepository;
import com.hotel.reservationsystem.service.model.HotelGuestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GuestService implements HotelGuestService {

    private GuestRepository guestRepository;
    private RoomRepository roomRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public GuestService(GuestRepository guestRepository, RoomRepository roomRepository, ReservationRepository reservationRepository) {
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Iterable<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    @Override
    public Optional<Guest> getGuestById(long guestId) {
        return guestRepository.findById(guestId);

    }

    @Override
    public Iterable<Reservation> getAllGuestReservations(long guestId) {
        return reservationRepository.findByGuestId(guestId);
    }

    /*
    throw it to controller class
     */
    @Override
    public Guest createGuest(Guest guest)throws DataIntegrityViolationException  {
        return guestRepository.save(guest);
    }

    @Override
    public void deleteGuest(long guestId) {
        guestRepository.deleteById(guestId);
    }

    @Override
    public Optional<Guest> updateGuestInfo(Guest guest, long guestId) {
        Optional<Guest> g = guestRepository.findById(guestId);
        if(g.isEmpty()) return g;

        g.get().setFirstName(guest.getFirstName());
        g.get().setLastName(guest.getLastName());
        g.get().setAddress(guest.getAddress());
        g.get().setEmailAddress(guest.getEmailAddress());
        g.get().setCountry(guest.getCountry());
        g.get().setState(guest.getState());
        g.get().setPhoneNumber(guest.getPhoneNumber());

        guestRepository.save(g.get());

        return g;

    }


}
