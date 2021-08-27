package com.hotel.reservationsystem.controller.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Getter @Setter @NoArgsConstructor
public class ReservationReq {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private long reservationId;
    private long roomId;
    private long guestId;

    //@Pattern(regexp = "((?:19|20)\\\\d\\\\d)-(0?[1-9]|1[012])-([12][0-9]|3[01]|0?[1-9])", message = "invalid date format")
    //private String reservationDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date reservationDate;



}
