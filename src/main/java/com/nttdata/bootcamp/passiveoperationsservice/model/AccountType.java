package com.nttdata.bootcamp.passiveoperationsservice.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountType {
    private String id = UUID.randomUUID().toString();
    private String description;
    private Double commission;
    private Integer maximumNumberOfOperations;
    private Integer allowedDayForOperations;
}