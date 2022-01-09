package com.nttdata.bootcamp.passiveoperationsservice.expose;

import com.nttdata.bootcamp.passiveoperationsservice.business.AccountService;
import com.nttdata.bootcamp.passiveoperationsservice.model.Account;
import com.nttdata.bootcamp.passiveoperationsservice.model.Operation;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.AccountFindBalancesResponseDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountCreateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountUpdateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.CustomerCustomerServiceResponseDTO;
import com.nttdata.bootcamp.passiveoperationsservice.utils.errorhandling.BusinessLogicException;
import com.nttdata.bootcamp.passiveoperationsservice.utils.errorhandling.ElementBlockedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/accounts")
    public Flux<Account> findAllAccounts(){
        log.info("Get operation in /accounts");
        return accountService.findAll();
    }

    @GetMapping("/accounts/{id}")
    public Mono<ResponseEntity<Account>> findAccountById(@PathVariable("id") String id) {
        log.info("Get operation in /accounts/{}", id);
        return accountService.findById(id)
                .flatMap(retrievedCustomer -> Mono.just(ResponseEntity.ok(retrievedCustomer)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/accounts")
    public Mono<ResponseEntity<Account>> createAccount(@RequestBody AccountCreateRequestDTO accountDTO) {
        log.info("Post operation in /accounts");
        return accountService.create(accountDTO)
                .flatMap(createdAccount -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(createdAccount)))
                .onErrorResume(ElementBlockedException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.LOCKED).build()))
                .onErrorResume(BusinessLogicException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                .onErrorResume(IllegalArgumentException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null)));
    }

    @PutMapping("/accounts")
    public Mono<ResponseEntity<Account>> updateAccount(@RequestBody AccountUpdateRequestDTO accountDTO) {
        log.info("Put operation in /accounts");
        return accountService.update(accountDTO)
                .flatMap(updatedAccount -> Mono.just(ResponseEntity.ok(updatedAccount)))
                .onErrorResume(ElementBlockedException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.LOCKED).build()))
                .onErrorResume(IllegalArgumentException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/accounts/{id}")
    public Mono<ResponseEntity<Account>> deleteAccount(@PathVariable("id") String id) {
        log.info("Delete operation in /accounts/{}", id);
        return accountService.removeById(id)
                .flatMap(removedCustomer -> Mono.just(ResponseEntity.ok(removedCustomer)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/customers-service/{id}")
    public Mono<ResponseEntity<CustomerCustomerServiceResponseDTO>> findByIdCustomerService(@PathVariable("id") String id) {
        log.info("Get operation in /customers-service/{}", id);
        return accountService.findByIdCustomerService(id)
                .flatMap(retrievedCustomer -> Mono.just(ResponseEntity.ok(retrievedCustomer)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
     }

    @PostMapping("/accounts/operations")
    public Mono<ResponseEntity<Account>> doOperation(@RequestBody AccountDoOperationRequestDTO accountDTO) {
        log.info("Post operation in /accounts/operation");
        return accountService.doOperation(accountDTO)
                .flatMap(createdAccount -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(createdAccount)))
                .onErrorResume(ElementBlockedException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.LOCKED).build()))
                .onErrorResume(IllegalArgumentException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null)));
    }

    @GetMapping("/accounts/{id}/operations")
    public Flux<Operation> findOperationsByAccountId(@PathVariable("id") String id) {
        log.info("Get operation in /accounts/{}/operations", id);
        return accountService.findOperationsByAccountId(id);
    }

    @GetMapping("customers/{id}/accounts/balance")
    public Flux<AccountFindBalancesResponseDTO> findBalancesByCustomerId(@PathVariable("id") String id) {
        log.info("Get operation in /customers/{}/accounts/balance", id);
        return accountService.findBalancesByCustomerId(id);
    }
}
