package com.nttdata.bootcamp.passiveoperationsservice.utils.impl;

import com.nttdata.bootcamp.passiveoperationsservice.model.Operation;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.OperationDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.utils.OperationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class OperationUtilsImpl implements OperationUtils {
    @Override
    public Operation operationDoOperationRequestDTOToOperation(OperationDoOperationRequestDTO operationDTO) {
        return Operation.builder()
                .operationNumber(UUID.randomUUID().toString())
                .type(operationDTO.getType())
                .amount(operationDTO.getAmount())
                .build();
    }

    @Override
    public OperationDoOperationRequestDTO operationToOperationDoOperationRequestDTO(Operation operation) {
        return OperationDoOperationRequestDTO.builder()
                .type(operation.getType())
                .amount(operation.getAmount())
                .build();
    }
}
