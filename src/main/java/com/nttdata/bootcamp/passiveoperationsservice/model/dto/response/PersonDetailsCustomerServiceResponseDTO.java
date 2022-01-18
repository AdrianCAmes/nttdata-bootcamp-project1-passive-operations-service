package com.nttdata.bootcamp.passiveoperationsservice.model.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonDetailsCustomerServiceResponseDTO {
    private String name;
    private String lastname;
    private String identityNumber;
    private AddressCustomerServiceResponseDTO address;
    private String email;
    private String phoneNumber;
    private String mobileNumber;
    private Date birthdate;
}
