package com.nttdata.bootcamp.passiveoperationsservice.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressCustomerServiceResponseDTO {
    private Integer number;
    private String street;
    private String city;
    private String country;
}
