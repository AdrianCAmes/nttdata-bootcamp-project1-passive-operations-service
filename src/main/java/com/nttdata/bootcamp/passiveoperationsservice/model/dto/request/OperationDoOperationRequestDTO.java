package com.nttdata.bootcamp.passiveoperationsservice.model.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OperationDoOperationRequestDTO {
    private String type;
    private Double amount;
}
