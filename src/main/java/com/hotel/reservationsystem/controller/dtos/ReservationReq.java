package com.hotel.reservationsystem.controller.dtos;

import com.hotel.reservationsystem.controller.utils.ValidDate;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.text.SimpleDateFormat;

@Getter @Setter @NoArgsConstructor
public class ReservationReq {

    private long reservationId;
    private long roomId;
    private long guestId;

    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotBlank(message = "date can't be left empty")
    @ValidDate
    private String reservationDate;



}
