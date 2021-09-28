package com.hotel.reservationsystem.controller.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter @NoArgsConstructor
public class RoomReq {
    private long roomId;
    private String roomName;
    private String roomNumber;
    private String bedInfo;

}
