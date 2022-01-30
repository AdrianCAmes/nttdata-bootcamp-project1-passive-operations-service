package com.nttdata.bootcamp.passiveoperationsservice.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BillingOrderActiveServiceResponseDTO {
    private String id;
    private Double calculatedAmount;
    private Double amountToRefund;
    private String cycle;
    private String status;
}
