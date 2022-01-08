package com.nttdata.bootcamp.passiveoperationsservice.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Operation {
    private String operationNumber = UUID.randomUUID().toString();
    private String type;
    private Double amount;
}