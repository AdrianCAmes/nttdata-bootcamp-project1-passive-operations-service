package com.nttdata.bootcamp.passiveoperationsservice.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person {
    private String name;
    private String lastname;
    private String identityNumber;
    private Address address;
    private String email;
    private String phoneNumber;
    private String mobileNumber;
    private Date birthdate;
}