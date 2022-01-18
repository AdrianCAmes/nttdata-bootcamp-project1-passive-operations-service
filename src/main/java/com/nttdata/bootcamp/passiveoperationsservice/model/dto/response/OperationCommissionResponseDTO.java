package com.nttdata.bootcamp.passiveoperationsservice.model.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OperationCommissionResponseDTO {
    private String operationNumber;
    private Date time;
    private Double amount;
    private Double commission;
}
