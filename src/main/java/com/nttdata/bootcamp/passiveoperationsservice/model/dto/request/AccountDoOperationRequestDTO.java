package com.nttdata.bootcamp.passiveoperationsservice.model.dto.request;

import com.nttdata.bootcamp.passiveoperationsservice.model.Operation;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountDoOperationRequestDTO {
    private String id;
    private OperationDoOperationRequestDTO operation;
}