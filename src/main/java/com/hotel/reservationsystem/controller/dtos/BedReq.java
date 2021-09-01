package com.hotel.reservationsystem.controller.dtos;

import javax.validation.constraints.NotBlank;

public class BedReq {

    @NotBlank(message = "specify bed information")
    private String bedInfo;

    public String getBedInfo() {
        return bedInfo;
    }

    public void setBedInfo(String bedInfo) {
        this.bedInfo = bedInfo;
    }
}
