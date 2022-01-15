package com.nttdata.bootcamp.passiveoperationsservice.utils.impl;

import com.nttdata.bootcamp.passiveoperationsservice.model.CustomerType;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.CustomerTypeCustomerServiceResponseDTO;
import com.nttdata.bootcamp.passiveoperationsservice.utils.CustomerTypeUtils;
import org.springframework.stereotype.Component;

@Component
public class CustomerTypeUtilsImpl implements CustomerTypeUtils {
    @Override
    public CustomerTypeCustomerServiceResponseDTO customerTypeToCustomerTypeCustomerServiceResponseDTO(CustomerType customerType) {
        return CustomerTypeCustomerServiceResponseDTO.builder()
                .group(customerType.getGroup())
                .subgroup(customerType.getSubgroup())
                .build();
    }

    @Override
    public CustomerType customerTypeCustomerServiceResponseDTOToCustomerType(CustomerTypeCustomerServiceResponseDTO customerTypeDTO) {
        return CustomerType.builder()
                .group(customerTypeDTO.getGroup())
                .subgroup(customerTypeDTO.getSubgroup())
                .build();
    }
}
