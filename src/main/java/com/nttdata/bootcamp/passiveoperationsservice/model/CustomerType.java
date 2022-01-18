package com.nttdata.bootcamp.passiveoperationsservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerType {
    private String group;
    private String subgroup;
}
