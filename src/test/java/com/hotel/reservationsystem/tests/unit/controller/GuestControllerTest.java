package com.hotel.reservationsystem.tests.unit.controller;

import com.hotel.reservationsystem.data.entity.Guest;
import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.data.repository.GuestRepository;
import com.hotel.reservationsystem.service.impl.GuestService;
import com.hotel.reservationsystem.service.model.HotelGuestService;
import netscape.javascript.JSObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class GuestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private HotelGuestService guestService;

    private static Guest guest;
    private static JSONObject json;

    @BeforeAll
    public static void setup() throws JSONException {
        guest = new Guest();
        guest.setFirstName("Aaditya");
        guest.setLastName("Kumar");
        guest.setGuestId(1334);
        guest.setState("kerala");
        guest.setPhoneNumber("04872449491");
        guest.setEmailAddress("ak@ril.com");
        guest.setAddress("Thrissur");
        guest.setCountry("India");

        json = new JSONObject();
        json.put("firstName", guest.getFirstName());
        json.put("lastName", guest.getLastName());
        json.put("country", guest.getCountry());
        json.put("state", guest.getState());
        json.put("address", guest.getAddress());
        json.put("email", guest.getEmailAddress());
        json.put("phoneNumber", guest.getPhoneNumber());
    }

    @Test
    public void getAllGuestsTest() throws Exception{
        List<Guest> allGuests = Arrays.asList(guest);

        given(guestService.getAllGuests()).willReturn(allGuests);

        mvc.perform(get("/api/v1/guests")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is(guest.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(guest.getLastName())));
    }

    @Test
    public void getGuestByIdTest() throws Exception{

        given(guestService.getGuestById(1334)).willReturn(java.util.Optional.of(guest));

        mvc.perform(get("/api/v1/guests/1334")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("guestId", is((int)guest.getGuestId())));

        mvc.perform(get("/api/v1/guests/1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void guestReservationTest()throws Exception{

        Reservation r = new Reservation();
        Date d = new Date();
        r.setReservationDate(new java.sql.Date(d.getTime()));
        r.setGuestId(1334);
        r.setReservationId(1);
        r.setRoomId(21);

        List<Reservation> reservations = Arrays.asList(r);

        given(guestService.getAllGuestReservations(1334)).willReturn(reservations);

        mvc.perform(get("/api/v1/guests/1334/reservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].reservationId", is((int)r.getReservationId())));


    }

    @Test
    public void addGuestTest()throws Exception{


        given(guestService.createGuest(any(Guest.class))).willReturn(guest);
        mvc.perform(post("/api/v1/guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.toString()))
                //.andDo(print())
                .andExpect(status().isNoContent());

        given(guestService.createGuest(any(Guest.class))).willReturn(null);
        mvc.perform(post("/api/v1/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.toString()))
                //.andDo(print())
                .andExpect(status().isBadRequest());


    }

    @Test
    public void deleteGuestTest()throws Exception{

        given(guestService.getGuestById(any(Long.class))).willReturn(Optional.empty());
        mvc.perform(delete("/api/v1/guests/1334"))
                //.andDo(print())
                .andExpect(status().isBadRequest());
        given(guestService.getGuestById(any(Long.class))).willReturn(java.util.Optional.ofNullable(guest));
        doNothing().when(guestService).deleteGuest(isA(Long.class));

        mvc.perform(delete("/api/v1/guests/1334"))
                //.andDo(print())
                .andExpect(status().isAccepted());
        verify(guestService, times(1)).deleteGuest(1334);
    }

    @Test
    public void updateGuestTest()throws Exception{
        given(guestService.updateGuestInfo(any(Guest.class), any(Long.class)))
                .willReturn(Optional.empty());
        mvc.perform(put("/api/v1/guests/1334")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.toString()))
                //.andDo(print())
                .andExpect(status().isBadRequest());

        //testing with a proper input
        given(guestService.updateGuestInfo(any(Guest.class), any(Long.class)))
                .willReturn(Optional.ofNullable(guest));

        mvc.perform(put("/api/v1/guests/1334")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.toString()))
                .andExpect(status().isAccepted());

    }


}
