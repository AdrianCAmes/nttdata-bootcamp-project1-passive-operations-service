package com.nttdata.bootcamp.passiveoperationsservice.utils.impl;

import com.nttdata.bootcamp.passiveoperationsservice.model.Customer;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.CustomerCustomerServiceResponseDTO;
import com.nttdata.bootcamp.passiveoperationsservice.utils.CustomerTypeUtils;
import com.nttdata.bootcamp.passiveoperationsservice.utils.CustomerUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerUtilsImpl implements CustomerUtils {
    private final CustomerTypeUtils customerTypeUtils;

    @Override
    public Customer customerCustomerServiceDTOToCustomer(CustomerCustomerServiceResponseDTO customerDTO) {
        return Customer.builder()
                .id(customerDTO.getId())
                .customerType(customerTypeUtils.customerTypeCustomerServiceResponseDTOToCustomerType(customerDTO.getCustomerType()))
                .status(customerDTO.getStatus())
                .build();
    }

    @Override
    public CustomerCustomerServiceResponseDTO customerToCustomerCustomerServiceResponseDTO(Customer customer) {
        return CustomerCustomerServiceResponseDTO.builder()
                .id(customer.getId())
                .customerType(customerTypeUtils.customerTypeToCustomerTypeCustomerServiceResponseDTO(customer.getCustomerType()))
                .status(customer.getStatus())
                .build();
    }
}