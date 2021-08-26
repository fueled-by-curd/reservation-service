package com.hotel.reservationsystem.tests.unit.service;

import com.hotel.reservationsystem.data.entity.Guest;
import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.data.repository.GuestRepository;
import com.hotel.reservationsystem.data.repository.ReservationRepository;
import com.hotel.reservationsystem.service.model.HotelGuestService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GuestServiceTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private GuestRepository guestRepository;
    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
    private HotelGuestService guestService;



    private static Guest guest;
    @BeforeAll
    public static void setup() {
        guest = new Guest();
        guest.setFirstName("Aaditya");
        guest.setLastName("Kumar");
        guest.setGuestId(1334);
        guest.setState("kerala");
        guest.setPhoneNumber("04872449491");
        guest.setEmailAddress("ak@ril.com");
        guest.setAddress("Thrissur");
        guest.setCountry("India");

    }

    @Test
    public void getGuestsTest(){
        List<Guest> guestList = new ArrayList<>();
        guestList.add(guest);
        given(guestRepository.findAll()).willReturn(guestList);

        Iterable<Guest> res = guestService.getAllGuests();
        assertThat(res).isNotNull();

        int i = 0;
        for(Guest elem : res){
            assertThat(elem).isEqualTo(guestList.get(i++));
        }
    }
    @Test
    public void getGuestByIdTest(){
        given(guestRepository.findById(any(Long.class))).willReturn(Optional.empty());
        Optional<Guest> res = guestService.getGuestById(21);
        assertThat(res.isEmpty()).isEqualTo(true);

        when(guestRepository.findById(21L)).thenReturn(Optional.of(guest));

        res = guestService.getGuestById(21);
        assertThat(res.isPresent()).isEqualTo(true);
        assertThat(res.get()).isEqualTo(guest);

    }

    @Test
    public void getGuestReservationsTest() {
        Reservation r1 = new Reservation();
        r1.setGuestId(1334);
        r1.setGuestId(21);
        r1.setReservationId(1);
        Reservation r2 = new Reservation();
        r2.setGuestId(1335);
        r2.setGuestId(21);
        r1.setReservationId(2);

        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(r1);
        reservationList.add(r2);
        given(reservationRepository.findByGuestId(21)).willReturn(reservationList);
        Iterable<Reservation> res = guestService.getAllGuestReservations(21);
        assertThat(res).isNotNull();

        int i = 0;
        for (Reservation elem : res) {
            assertThat(elem).isEqualTo(reservationList.get(i++));
        }
    }

    @Test
    public void deleteReservationTest(){

        doNothing().when(guestRepository).deleteById(any(Long.class));
        guestService.deleteGuest(1);
        verify(guestRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    public void createReservationTest(){

        given(guestRepository.save(any(Guest.class))).willReturn(guest);
        Guest res = guestService.createGuest(guest);

        verify(guestRepository, times(1)).save(any(Guest.class));
        assertThat(res).isEqualTo(guest);

    }

    @Test
    public void updateGuestTest(){

        given(guestRepository.findById(any(Long.class))).willReturn(Optional.empty());
        Optional<Guest> res = guestService.updateGuestInfo(guest , 21);
        assertThat(res.isEmpty()).isEqualTo(true);

        when(guestRepository.findById(21L)).thenReturn(Optional.of(guest));
        given(guestRepository.save(any(Guest.class))).willReturn(null);

        res = guestService.updateGuestInfo(guest , 21);

        verify(guestRepository, times(1)).save(any(Guest.class));
        assertThat(res.isPresent()).isTrue();

    }




}
