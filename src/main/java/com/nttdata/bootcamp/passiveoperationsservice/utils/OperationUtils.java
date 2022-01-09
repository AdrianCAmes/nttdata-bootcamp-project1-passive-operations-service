package com.nttdata.bootcamp.passiveoperationsservice.utils;


import com.nttdata.bootcamp.passiveoperationsservice.model.Operation;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.OperationDoOperationRequestDTO;

public interface OperationUtils {
    Operation operationDoOperationRequestDTOToOperation(OperationDoOperationRequestDTO operationDTO);
    OperationDoOperationRequestDTO operationToOperationDoOperationRequestDTO(Operation operation);
}
