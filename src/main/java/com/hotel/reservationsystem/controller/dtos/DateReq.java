package com.hotel.reservationsystem.controller.dtos;

import java.sql.Date;

public class DateReq {
    private Date reservationDate;

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }
}
