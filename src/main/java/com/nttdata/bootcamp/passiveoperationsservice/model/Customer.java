package com.nttdata.bootcamp.passiveoperationsservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Customer {
    private String id;
    private CustomerType customerType;
    private String status;
}