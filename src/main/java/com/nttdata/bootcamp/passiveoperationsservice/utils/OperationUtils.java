package com.nttdata.bootcamp.passiveoperationsservice.utils;


import com.nttdata.bootcamp.passiveoperationsservice.model.Operation;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.OperationDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.OperationCommissionResponseDTO;

public interface OperationUtils {
    Operation operationDoOperationRequestDTOToOperation(OperationDoOperationRequestDTO operationDTO);
    Operation operationCommissionResponseDTOToOperation(OperationCommissionResponseDTO operationDTO);
    OperationDoOperationRequestDTO operationToOperationDoOperationRequestDTO(Operation operation);
    OperationCommissionResponseDTO operationToOperationCommissionResponseDTO(Operation operation);

}
