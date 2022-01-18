package com.nttdata.bootcamp.passiveoperationsservice.model;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Operation {
    private String operationNumber = UUID.randomUUID().toString();
    private Date time;
    private String type;
    private Double amount;
    private Double commission;
    private Double finalBalance;
}