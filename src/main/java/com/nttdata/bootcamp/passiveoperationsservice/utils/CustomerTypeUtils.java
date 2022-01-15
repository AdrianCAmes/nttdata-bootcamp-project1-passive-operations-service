package com.nttdata.bootcamp.passiveoperationsservice.utils;

import com.nttdata.bootcamp.passiveoperationsservice.model.CustomerType;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.CustomerTypeCustomerServiceResponseDTO;

public interface CustomerTypeUtils {
    CustomerTypeCustomerServiceResponseDTO customerTypeToCustomerTypeCustomerServiceResponseDTO(CustomerType customerType);
    CustomerType customerTypeCustomerServiceResponseDTOToCustomerType(CustomerTypeCustomerServiceResponseDTO customerTypeDTO);
}
