package com.nttdata.bootcamp.passiveoperationsservice.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerActiveServiceResponseDTO {
    private String id;
    private CustomerTypeActiveServiceResponseDTO customerType;
    private String status;
}
