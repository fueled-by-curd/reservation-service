package com.hotel.reservationsystem.service.model;

import com.hotel.reservationsystem.data.entity.Guest;
import com.hotel.reservationsystem.data.entity.Reservation;

import java.util.Optional;

public interface HotelGuestService {
    Iterable<Guest> getAllGuests();
    Optional<Guest> getGuestById(long guestId);
    Iterable<Reservation> getAllGuestReservations(long guestId);
    Guest createGuest(Guest guest);

    void deleteGuest(long guestId);

    Optional<Guest> updateGuestInfo(Guest guest,long guestId);

    Iterable<Guest> findByName(String name);


}
