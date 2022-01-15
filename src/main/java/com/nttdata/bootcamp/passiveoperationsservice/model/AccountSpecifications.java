package com.nttdata.bootcamp.passiveoperationsservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountSpecifications {
    private Double maintenanceCommissionPercentage;
    private Double minimumDailyAverage;
    private Double operationCommissionPercentage;
    private Integer maximumNumberOfOperations;
    private Integer allowedDayForOperations;
}