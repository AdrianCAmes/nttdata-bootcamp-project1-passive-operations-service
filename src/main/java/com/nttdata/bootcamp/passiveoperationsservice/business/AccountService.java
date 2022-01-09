package com.nttdata.bootcamp.passiveoperationsservice.business;

import com.nttdata.bootcamp.passiveoperationsservice.model.Account;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountCreateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountUpdateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.CustomerCustomerServiceResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<Account> create(AccountCreateRequestDTO accountDTO);
    Mono<Account> findById(String id);
    Flux<Account> findAll();
    Mono<Account> update(AccountUpdateRequestDTO accountDTO);
    Mono<Account> removeById(String id);
    Mono<CustomerCustomerServiceResponseDTO> findByIdCustomerService(String id);
    Flux<Account> findByCustomerId(String id);
    Mono<Account> doOperation(AccountDoOperationRequestDTO accountDTO);
}