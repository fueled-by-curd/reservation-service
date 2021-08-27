package com.hotel.reservationsystem.tests.unit.controller;

import com.hotel.reservationsystem.controller.dtos.BedReq;
import com.hotel.reservationsystem.data.entity.Room;
import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.service.model.HotelRoomService;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RoomControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private HotelRoomService roomService;


    private static Room room;

    @BeforeAll
    public static void setup() {
        room = new Room();
        room.setRoomId(21);
        room.setBedInfo("1K");
        room.setRoomName("Highbury");

    }

    @Test
    public void getAllRoomsTest() throws Exception{
        List<Room> allRooms = Arrays.asList(room);

        given(roomService.getAllRooms()).willReturn(allRooms);

        mvc.perform(get("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].roomId", is((int)room.getRoomId())));


    }

    @Test
    public void getRoomByIdTest() throws Exception{

        given(roomService.getRoomByRoomId(21)).willReturn(java.util.Optional.of(room));

        mvc.perform(get("/api/v1/rooms/21")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("roomId", is((int)room.getRoomId())));

        mvc.perform(get("/api/v1/rooms/12")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void roomReservationTest()throws Exception{

        Reservation r = new Reservation();
        Date d = new Date();
        r.setReservationDate(new java.sql.Date(d.getTime()));
        r.setRoomId(1334);
        r.setReservationId(1);
        r.setRoomId(21);

        List<Reservation> reservations = Arrays.asList(r);

        given(roomService.getAllRoomReservations(21)).willReturn(reservations);

        mvc.perform(get("/api/v1/rooms/21/reservations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].roomId", is((int) r.getRoomId())))
                .andExpect(jsonPath("$[0].reservationId", is((int)r.getReservationId())));


    }

    @Test
    public void updateRoomTest()throws Exception{
        JSONObject dateUpdate = new JSONObject();
        dateUpdate.put("bedInfo", "2Q");

        given(roomService.updateBed(any(BedReq.class), any(Long.class)))
                .willReturn(Optional.empty());
        mvc.perform(patch("/api/v1/rooms/21/bed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dateUpdate.toString()))
                .andExpect(status().isBadRequest());

        given(roomService.updateBed(any(BedReq.class), any(Long.class)))
                .willReturn(Optional.ofNullable(room));
        mvc.perform(patch("/api/v1/rooms/21/bed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dateUpdate.toString()))
                .andExpect(status().isCreated());


    }



}
