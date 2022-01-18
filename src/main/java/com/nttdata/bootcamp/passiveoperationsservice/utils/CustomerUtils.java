package com.nttdata.bootcamp.passiveoperationsservice.utils;

import com.nttdata.bootcamp.passiveoperationsservice.model.Customer;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.CustomerCustomerServiceResponseDTO;

public interface CustomerUtils {
    Customer customerCustomerServiceDTOToCustomer(CustomerCustomerServiceResponseDTO customerDTO);
    CustomerCustomerServiceResponseDTO customerToCustomerCustomerServiceResponseDTO(Customer customer);
}
