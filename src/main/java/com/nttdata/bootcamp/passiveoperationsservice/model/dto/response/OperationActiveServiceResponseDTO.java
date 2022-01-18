package com.nttdata.bootcamp.passiveoperationsservice.model.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OperationActiveServiceResponseDTO {
    private String operationNumber;
    private Date time;
    private String type;
    private Double amount;
    private BillingOrderActiveServiceResponseDTO billingOrder;
}
