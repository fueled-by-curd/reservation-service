package com.hotel.reservationsystem.controller.dtos;

import com.hotel.reservationsystem.controller.utils.ValidDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.sql.Date;

@Getter @Setter @NoArgsConstructor
public class DateReq {

    @NotBlank(message = "specify new date")
    @ValidDate
    private String reservationDate;




}
