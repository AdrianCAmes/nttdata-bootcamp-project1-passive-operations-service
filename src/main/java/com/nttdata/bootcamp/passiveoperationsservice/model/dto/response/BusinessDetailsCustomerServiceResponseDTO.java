package com.nttdata.bootcamp.passiveoperationsservice.model.dto.response;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BusinessDetailsCustomerServiceResponseDTO {
    private String name;
    private String ruc;
    private AddressCustomerServiceResponseDTO address;
    private ArrayList<PersonDetailsCustomerServiceResponseDTO> representatives;
}
