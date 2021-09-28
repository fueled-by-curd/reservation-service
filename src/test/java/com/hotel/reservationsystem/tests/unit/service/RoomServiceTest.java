package com.hotel.reservationsystem.tests.unit.service;

import com.hotel.reservationsystem.controller.dtos.BedReq;
import com.hotel.reservationsystem.data.entity.Reservation;
import com.hotel.reservationsystem.data.entity.Room;
import com.hotel.reservationsystem.data.repository.GuestRepository;
import com.hotel.reservationsystem.data.repository.ReservationRepository;
import com.hotel.reservationsystem.data.repository.RoomRepository;
import com.hotel.reservationsystem.service.model.HotelRoomService;
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
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RoomServiceTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RoomRepository roomRepository;
    @MockBean
    private GuestRepository guestRepository;
    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
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
    public void getRoomsTest(){
        List<Room> roomList = new ArrayList<>();
        roomList.add(room);
        Room r2 = new Room();
        r2.setRoomName("Emirates");
        r2.setRoomId(4);
        r2.setBedInfo("1K");
        r2.setRoomNumber("204");
        roomList.add(r2);
        given(roomRepository.findAll()).willReturn(roomList);

        Iterable<Room> res = roomService.getAllRooms();
        assertThat(res).isNotNull();

        int i = 0;
        for(Room elem : res){
            assertThat(elem).isEqualTo(roomList.get(i++));
        }
    }
    @Test
    public void getRoomByIdTest(){
        given(roomRepository.findById(any(Long.class))).willReturn(Optional.empty());
        Optional<Room> res = roomService.getRoomByRoomId(21);
        assertThat(res.isEmpty()).isEqualTo(true);

        when(roomRepository.findById(21L)).thenReturn(Optional.of(room));

        res = roomService.getRoomByRoomId(21);
        assertThat(res.isPresent()).isEqualTo(true);
        assertThat(res.get()).isEqualTo(room);

    }
    @Test
    public void getRoomReservationsTest(){
        Reservation r1 = new Reservation();
        r1.setGuestId(1334);r1.setRoomId(21);r1.setReservationId(1);
        Reservation r2 = new Reservation();
        r2.setGuestId(1335);r2.setRoomId(21);r1.setReservationId(2);

        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(r1);reservationList.add(r2);
        given(reservationRepository.findByRoomId(21)).willReturn(reservationList);
        Iterable<Reservation> res = roomService.getAllRoomReservations(21);
        assertThat(res).isNotNull();

        int i = 0;
        for(Reservation elem : res){
            assertThat(elem).isEqualTo(reservationList.get(i++));
        }




    }
    @Test
    public void updateBedTest(){
        BedReq bedReq = new BedReq();
        bedReq.setBedInfo("2Q");

        given(roomRepository.findById(any(Long.class))).willReturn(Optional.empty());
        Optional<Room> res = roomService.updateBed(bedReq, 21);
        assertThat(res.isEmpty()).isEqualTo(true);

        when(roomRepository.findById(21L)).thenReturn(Optional.of(room));
        given(roomRepository.save(any(Room.class))).willReturn(null);

        res = roomService.updateBed(bedReq, 21);

        verify(roomRepository, times(1)).save(any(Room.class));
        assertThat(res.isPresent()).isTrue();
        assertThat(res.get().getBedInfo()).isEqualTo(bedReq.getBedInfo());


    }

}
