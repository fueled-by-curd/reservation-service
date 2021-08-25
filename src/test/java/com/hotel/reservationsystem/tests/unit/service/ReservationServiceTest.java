package com.hotel.reservationsystem.tests.unit.service;

import com.hotel.reservationsystem.controller.models.BedInfo;
import com.hotel.reservationsystem.controller.models.DateReq;
import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.data.repository.GuestRepository;
import com.hotel.reservationsystem.data.repository.ReservationRepository;

import com.hotel.reservationsystem.service.model.BookingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ReservationServiceTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReservationRepository reservationRepository;
    @MockBean
    private GuestRepository guestRepository;

    @Autowired
    private BookingService reservationService;

    private static Reservation reservation;

    @BeforeAll
    public static void setup() {
        reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setGuestId(1334);
        reservation.setReservationId(21);
        reservation.setReservationDate(new Date( new java.util.Date().getTime()));
    }

    @Test
    public void getReservationsTest(){
        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);

        given(reservationRepository.findAll()).willReturn(reservationList);

        Iterable<Reservation> res = reservationService.getAllReservations();
        assertThat(res).isNotNull();

        int i = 0;
        for(Reservation elem : res){
            assertThat(elem).isEqualTo(reservationList.get(i++));
        }
    }

    @Test
    public void getReservationByIdTest(){
        given(reservationRepository.findById(any(Long.class))).willReturn(Optional.empty());
        Optional<Reservation> res = reservationService.getReservationById(21);
        assertThat(res.isEmpty()).isEqualTo(true);

        when(reservationRepository.findById(21L)).thenReturn(Optional.of(reservation));

        res = reservationService.getReservationById(21);
        assertThat(res.isPresent()).isEqualTo(true);
        assertThat(res.get()).isEqualTo(reservation);

    }

    @Test
    public void updateBedTest(){
        DateReq dateReq = new DateReq();

        LocalDate date = LocalDate.of (2020,12,19);


        ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
        long epoch = date.atStartOfDay(zoneId).toEpochSecond();
        dateReq.setReservationDate(new Date(epoch));


        given(reservationRepository.findById(any(Long.class))).willReturn(Optional.empty());
        Optional<Reservation> res = reservationService.updateReservationDate(dateReq , 21);
        assertThat(res.isEmpty()).isEqualTo(true);

        when(reservationRepository.findById(21L)).thenReturn(Optional.of(reservation));
        given(reservationRepository.save(any(Reservation.class))).willReturn(null);

        res = reservationService.updateReservationDate(dateReq , 21);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
        assertThat(res.isPresent()).isTrue();
        assertThat(res.get().getReservationDate()).isEqualTo(dateReq.getReservationDate());


    }

    @Test
    public void deleteReservationTest(){


        doNothing().when(reservationRepository).deleteById(any(Long.class));
        reservationService.removeReservation(1);
        verify(reservationRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    public void createReservationTest(){

        given(reservationRepository.save(any(Reservation.class))).willReturn(reservation);
        Reservation res = reservationService.createReservation(reservation);

        verify(reservationRepository, times(1)).save(any(Reservation.class));
        assertThat(res).isEqualTo(reservation);

    }


}
