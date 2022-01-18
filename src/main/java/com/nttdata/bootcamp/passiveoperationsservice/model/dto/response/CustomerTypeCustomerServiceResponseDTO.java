package com.nttdata.bootcamp.passiveoperationsservice.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerTypeCustomerServiceResponseDTO {
    private String group;
    private String subgroup;
}
