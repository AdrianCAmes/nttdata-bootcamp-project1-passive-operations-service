package com.nttdata.bootcamp.passiveoperationsservice.model.dto.request;

import lombok.*;

import java.util.Date;
import java.util.UUID;

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
