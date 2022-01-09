package com.nttdata.bootcamp.passiveoperationsservice.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Customer {
    private String id;
    private String type;
    private String status;
}