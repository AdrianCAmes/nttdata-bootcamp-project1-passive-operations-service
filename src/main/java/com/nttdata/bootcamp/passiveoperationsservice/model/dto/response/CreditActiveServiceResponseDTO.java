package com.nttdata.bootcamp.passiveoperationsservice.model.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreditActiveServiceResponseDTO {
    private String id;
    private String status;
    private CustomerActiveServiceResponseDTO customer;
    private Double fullGrantedAmount;
    private Double availableAmount;
    private String creditCardNumber = UUID.randomUUID().toString();
    private Date issueDate;
    private Date dueDate;
    private ArrayList<OperationActiveServiceResponseDTO> operations;
    private BillingDetailsActiveServiceResponseDTO billingDetails;
}
