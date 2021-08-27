package com.hotel.reservationsystem.controller.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter @NoArgsConstructor
public class GuestReq {
    private long guestId;

    @NotBlank(message = "first name can't be left empty")
    private String firstName;


    @NotBlank(message = "last name can't be left empty")
    private String lastName;


    @NotBlank(message = "email can't be left empty")
    @Email(message = "invalid email format")
    private String emailAddress;

    private String address;
    private String country;
    private String state;
    private String phoneNumber;
}
