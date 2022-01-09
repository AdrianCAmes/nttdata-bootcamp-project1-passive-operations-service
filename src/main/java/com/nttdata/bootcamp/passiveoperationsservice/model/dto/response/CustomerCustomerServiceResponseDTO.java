package com.nttdata.bootcamp.passiveoperationsservice.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerCustomerServiceResponseDTO {
    private String id;
    private String type;
    private String status;
    private PersonDetailsCustomerServiceResponseDTO personDetails;
    private BusinessDetailsCustomerServiceResponseDTO businessDetails;
}
