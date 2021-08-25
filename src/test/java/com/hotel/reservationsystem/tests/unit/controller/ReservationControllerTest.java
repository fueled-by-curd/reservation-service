package com.hotel.reservationsystem.tests.unit.controller;

import com.hotel.reservationsystem.controller.models.DateReq;
import com.hotel.reservationsystem.data.entity.Guest;
import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.service.model.BookingService;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ReservationControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService reservationService;


    private static Reservation reservation;
    private static JSONObject json;


    @BeforeAll
    public static void setup() throws JSONException {
        reservation = new Reservation();
        reservation.setReservationId(1);
        reservation.setGuestId(1334);
        reservation.setRoomId(21);
        reservation.setReservationDate(new Date( new java.util.Date().getTime()));

        json = new JSONObject();
        json.put("roomId" , reservation.getRoomId());
        json.put("guestId" , reservation.getGuestId());
        json.put("reservationId" , reservation.getReservationId());
        json.put("reservationDate" , reservation.getReservationDate().toString());
    }

    @Test
    public void getAllReservationsTest() throws Exception{
        List<Reservation> allReservations = Arrays.asList(reservation);

        given(reservationService.getAllReservations()).willReturn(allReservations);

        mvc.perform(get("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].reservationId", is((int)reservation.getReservationId())));


    }

    @Test
    public void getReservationByIdTest() throws Exception{

        given(reservationService.getReservationById(21)).willReturn(java.util.Optional.of(reservation));

        mvc.perform(get("/api/v1/reservations/21")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("reservationId", is((int)reservation.getReservationId())));

        mvc.perform(get("/api/v1/reservations/12")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void deleteReservationTest()throws Exception{

        given(reservationService.getReservationById(any(Long.class))).willReturn(Optional.empty());
        mvc.perform(delete("/api/v1/reservations/1"))
                //.andDo(print())
                .andExpect(status().isBadRequest());

        given(reservationService.getReservationById(any(Long.class))).willReturn(java.util.Optional.ofNullable(reservation));
        doNothing().when(reservationService).removeReservation(isA(Long.class));

        mvc.perform(delete("/api/v1/reservations/1"))
                //.andDo(print())
                .andExpect(status().isAccepted());
        verify(reservationService, times(1)).removeReservation(1);
    }

    @Test
    public void addReservationTest()throws Exception{



        given(reservationService.createReservation(any(Reservation.class))).willReturn(reservation);
        mvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.toString()))
                //.andDo(print())
                .andExpect(status().isNoContent());

        given(reservationService.createReservation(any(Reservation.class))).willReturn(null);
        mvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.toString()))
                //.andDo(print())
                .andExpect(status().isBadRequest());


    }

    @Test
    public void updateReservationTest()throws Exception{
        JSONObject dateUpdate = new JSONObject();
        dateUpdate.put("reservationDate", "2021-02-12");

        given(reservationService.updateReservationDate(any(DateReq.class), any(Long.class)))
                .willReturn(Optional.empty());
        mvc.perform(patch("/api/v1/reservations/1/date")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dateUpdate.toString()))
                .andExpect(status().isBadRequest());

        given(reservationService.updateReservationDate(any(DateReq.class), any(Long.class)))
                .willReturn(Optional.ofNullable(reservation));
        mvc.perform(patch("/api/v1/reservations/1/date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dateUpdate.toString()))
                .andExpect(status().isCreated());


    }



}
