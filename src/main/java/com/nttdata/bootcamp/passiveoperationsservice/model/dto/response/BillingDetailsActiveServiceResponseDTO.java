package com.nttdata.bootcamp.passiveoperationsservice.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BillingDetailsActiveServiceResponseDTO {
    private Double interestPercentage;
    private Integer closingDay;
}
