package com.nttdata.bootcamp.passiveoperationsservice.business.impl;

import com.nttdata.bootcamp.passiveoperationsservice.business.AccountService;
import com.nttdata.bootcamp.passiveoperationsservice.model.Account;
import com.nttdata.bootcamp.passiveoperationsservice.model.Customer;
import com.nttdata.bootcamp.passiveoperationsservice.model.Operation;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountCreateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountUpdateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.AccountFindBalancesResponseDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.CustomerCustomerServiceResponseDTO;
import com.nttdata.bootcamp.passiveoperationsservice.repository.AccountRepository;
import com.nttdata.bootcamp.passiveoperationsservice.utils.AccountUtils;
import com.nttdata.bootcamp.passiveoperationsservice.utils.CustomerUtils;
import com.nttdata.bootcamp.passiveoperationsservice.config.Constants;
import com.nttdata.bootcamp.passiveoperationsservice.utils.errorhandling.BusinessLogicException;
import com.nttdata.bootcamp.passiveoperationsservice.utils.errorhandling.ElementBlockedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final WebClient.Builder webClientBuilder;
    private final AccountUtils accountUtils;
    private final CustomerUtils customerUtils;
    private final Constants constants;

    @Override
    public Mono<Account> create(AccountCreateRequestDTO accountDTO) {
        log.info("Start of operation to create an account");

        if (accountDTO.getCustomerId() == null || !accountDTO.getCustomerId().isBlank()) {
            Mono<Account> createdAccount = findByIdCustomerService(accountDTO.getCustomerId())
                    .flatMap(retrievedCustomer -> {
                        log.info("Validating account");
                        return accountToCreateValidation(accountDTO, retrievedCustomer);
                    })
                    .flatMap(validatedCustomer -> {
                        Account accountToCreate = accountUtils.accountCreateRequestDTOToAccount(accountDTO);
                        Customer customer = customerUtils.customerCustomerServiceDTOToCustomer(validatedCustomer);

                        accountToCreate.setCustomer(customer);
                        accountToCreate.setStatus(constants.getSTATUS_ACTIVE());
                        accountToCreate.setCurrentBalance(0.0);

                        log.info("Creating new account: [{}]", accountToCreate.toString());
                        return accountRepository.insert(accountToCreate);
                    })
                    .switchIfEmpty(Mono.error(new NoSuchElementException("Customer does not exist")));

            log.info("End of operation to create an account");
            return createdAccount;
        } else {
            log.warn("Account does not contain a customer id");
            log.warn("Proceeding to abort create account");
            return Mono.error(new IllegalArgumentException("Account does not contain customer id"));
        }
    }

    @Override
    public Mono<Account> findById(String id) {
        log.info("Start of operation to find an account by id");

        log.info("Retrieving account with id: [{}]", id);
        Mono<Account> retrievedAccount = accountRepository.findById(id);
        log.info("Account with id: [{}] was retrieved successfully", id);

        log.info("End of operation to find an account by id");
        return retrievedAccount;
    }

    @Override
    public Flux<Account> findAll() {
        log.info("Start of operation to retrieve all accounts");

        log.info("Retrieving all accounts");
        Flux<Account> retrievedAccount = accountRepository.findAll();
        log.info("All accounts retrieved successfully");

        log.info("End of operation to retrieve all accounts");
        return retrievedAccount;
    }

    @Override
    public Mono<Account> update(AccountUpdateRequestDTO accountDTO) {
        log.info("Start of operation to update an account");

        Mono<Account> updatedAccount = findById(accountDTO.getId())
                .flatMap(retrievedAccount -> {
                    log.info("Validating account");
                    return accountToUpdateValidation(accountDTO, retrievedAccount);
                })
                .flatMap(validatedAccount -> {
                    Account accountToUpdate = accountUtils.fillAccountWithAccountUpdateCreateRequestDTO(validatedAccount, accountDTO);

                    log.info("Updating customer: [{}]", accountToUpdate.toString());
                    Mono<Account> nestedUpdatedAccount = accountRepository.save(accountToUpdate);
                    log.info("Customer with id: [{}] was successfully updated", accountToUpdate.getId());

                    return nestedUpdatedAccount;
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("Account does not exist")));

        log.info("End of operation to update an account");
        return updatedAccount;
    }

    @Override
    public Mono<Account> removeById(String id) {
        log.info("Start of operation to remove an account");

        log.info("Deleting account with id: [{}]", id);
        Mono<Account> removedAccount = findById(id)
                .flatMap(retrievedAccount -> accountRepository.deleteById(retrievedAccount.getId()).thenReturn(retrievedAccount));
        log.info("Customer with id: [{}] was successfully deleted", id);

        log.info("End of operation to remove a customer");
        return removedAccount;
    }

    @Override
    public Mono<CustomerCustomerServiceResponseDTO> findByIdCustomerService(String id) {
        log.info("Start of operation to retrieve customer with id [{}] from customer-info-service", id);

        log.info("Retrieving customer");
        String url = constants.getCUSTOMER_INFO_SERVICE_URL() + "/customers/" + id;
        Mono<CustomerCustomerServiceResponseDTO> retrievedCustomer = webClientBuilder.build().get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .build())
                .retrieve()
                .onStatus(httpStatus -> httpStatus == HttpStatus.NOT_FOUND, clientResponse -> Mono.empty())
                .bodyToMono(CustomerCustomerServiceResponseDTO.class);
        log.info("Customer retrieved successfully");

        log.info("End of operation to retrieve customer with id: [{}]", id);
        return retrievedCustomer;
    }

    @Override
    public Flux<Account> findByCustomerId(String id) {
        log.info("Start of operation to retrieve all accounts of the customer with id: [{}]", id);

        log.info("Retrieving accounts");
        Flux<Account> retrievedAccount = accountRepository.findAccountsByCustomerId(id);
        log.info("Accounts retrieved successfully");

        log.info("End of operation to retrieve accounts of the customer with id: [{}]", id);
        return retrievedAccount;
    }

    @Override
    public Mono<Account> doOperation(AccountDoOperationRequestDTO accountDTO) {
        log.info("Start to save a new account operation for the account with id: [{}]", accountDTO.getId());

        Mono<Account> updatedAccount = accountRepository.findById(accountDTO.getId())
                        .flatMap(retrievedAccount -> {
                            log.info("Validating operation");
                            return operationValidation(accountDTO, retrievedAccount);
                        })
                        .flatMap(validatedAccount -> {
                            Double amountToUpdate = accountDTO.getOperation().getType().contentEquals(constants.getOPERATION_DEPOSIT_TYPE()) ?
                                    validatedAccount.getCurrentBalance() + accountDTO.getOperation().getAmount() :
                                    validatedAccount.getCurrentBalance() - accountDTO.getOperation().getAmount();
                            validatedAccount.setCurrentBalance(amountToUpdate);

                            Account accountToUpdate = accountUtils.fillAccountWithAccountDoOperationRequestDTO(validatedAccount, accountDTO);

                            log.info("Doing deposit of [{}] to account with id [{}]", accountDTO.getOperation().getAmount(), accountDTO.getId());
                            log.info("Saving operation into account: [{}]", accountToUpdate.toString());
                            Mono<Account> nestedUpdatedAccount = accountRepository.save(accountToUpdate);
                            log.info("Operation was successfully updated");

                            return nestedUpdatedAccount;
                        })
                        .switchIfEmpty(Mono.error(new NoSuchElementException("Account does not exist")));

        log.info("End to save a new account operation for the account with id: [{}]", accountDTO.getId());
        return updatedAccount;
    }

    @Override
    public Flux<Operation> findOperationsByAccountId(String id) {
        log.info("Start of operation to retrieve all operations from account with id: [{}]", id);

        log.info("Retrieving all operations");
        Flux<Operation> retrievedOperations = findById(id)
                .filter(retrievedAccount -> retrievedAccount.getOperations() != null)
                .flux()
                .flatMap(retrievedAccount -> Flux.fromIterable(retrievedAccount.getOperations()));
        log.info("Operations retrieved successfully");

        log.info("End of operation to retrieve operations from account with id: [{}]", id);
        return retrievedOperations;
    }

    @Override
    public Flux<AccountFindBalancesResponseDTO> findBalancesByCustomerId(String id) {
        log.info("Start of operation to retrieve account balances from customer with id: [{}]", id);

        log.info("Retrieving account balances");
        Flux<AccountFindBalancesResponseDTO> retrievedBalances = findByCustomerId(id)
                .map(retrievedAccount -> accountUtils.accountToAccountFindBalancesResponseDTO(retrievedAccount));
        log.info("Balances retrieved successfully");

        log.info("End of operation to retrieve account balances from customer with id: [{}]", id);
        return retrievedBalances;
    }

    //region Private Helper Functions

    private Mono<CustomerCustomerServiceResponseDTO> accountToCreateValidation(AccountCreateRequestDTO accountToCreate, CustomerCustomerServiceResponseDTO customerFromMicroservice) {
        log.info("Customer exists in database");

        if (customerFromMicroservice.getStatus().contentEquals(constants.getSTATUS_BLOCKED()))
        {
            log.warn("Customer have blocked status");
            log.warn("Proceeding to abort create account");
            return Mono.error(new ElementBlockedException("The customer have blocked status"));
        }


        if (accountToCreate.getHolders() == null || accountToCreate.getHolders().isEmpty()) {
            log.warn("Account does not contain holders, must have at least one");
            log.warn("Proceeding to abort create account");
            return Mono.error(new IllegalArgumentException("Account does not contain holders"));
        }

        if (customerFromMicroservice.getType().contentEquals(constants.getCUSTOMER_PERSONAL_TYPE()))
        {
            return findByCustomerId(customerFromMicroservice.getId())
                    .filter(retrievedAccount -> retrievedAccount.getStatus().contentEquals(constants.getSTATUS_ACTIVE()))
                    .hasElements()
                    .flatMap(haveAnAccount -> {
                        if (haveAnAccount) {
                            log.warn("Can not create more than one account for a personal customer");
                            log.warn("Proceeding to abort create account");
                            return Mono.error(new BusinessLogicException("Customer have more than one account"));
                        }
                        else {
                            log.info("Account successfully validated");
                            return Mono.just(customerFromMicroservice);
                        }
                    });
        } else if (customerFromMicroservice.getType().contentEquals(constants.getCUSTOMER_BUSINESS_TYPE())) {
            if (accountToCreate.getAccountType().getDescription().contentEquals(constants.getACCOUNT_CURRENT_TYPE()))
            {
                log.info("Account successfully validated");
                return Mono.just(customerFromMicroservice);
            } else {
                log.warn("Can not create {} account for business customers. Accounts must be of current type", accountToCreate.getAccountType().getDescription());
                log.warn("Proceeding to abort create account");
                return Mono.error(new BusinessLogicException("Account is not of current type"));
            }
        } else {
            log.warn("Customer's type is not supported");
            log.warn("Proceeding to abort create account");
            return Mono.error(new BusinessLogicException("Not supported customer type"));
        }
    }

    private Mono<Account> accountToUpdateValidation(AccountUpdateRequestDTO accountForUpdate, Account accountInDatabase) {
        log.info("Account exists in database");

        if (accountInDatabase.getCustomer().getStatus().contentEquals(constants.getSTATUS_BLOCKED())) {
            log.warn("Customer have blocked status");
            log.warn("Proceeding to abort update account");
            return Mono.error(new ElementBlockedException("The customer have blocked status"));
        }

        if (accountForUpdate.getHolders() == null || accountForUpdate.getHolders().isEmpty()) {
            log.warn("Account does not contain holders, must have at least one");
            log.warn("Proceeding to abort update account");
            return Mono.error(new IllegalArgumentException("Account does not contain holders"));
        }

        log.info("Account successfully validated");
        return Mono.just(accountInDatabase);
    }

    private Mono<Account> operationValidation(AccountDoOperationRequestDTO accountToUpdateOperation, Account accountInDatabase) {
        log.info("Account exists in database");

        if (accountInDatabase.getStatus().contentEquals(constants.getSTATUS_BLOCKED())) {
            log.warn("Account have blocked status");
            log.warn("Proceeding to abort do operation");
            return Mono.error(new ElementBlockedException("The account have blocked status"));
        }

        if (accountToUpdateOperation.getOperation().getType().contentEquals(constants.getOPERATION_WITHDRAWAL_TYPE()) &&
            accountInDatabase.getCurrentBalance() < accountToUpdateOperation.getOperation().getAmount()) {
            log.info("Account has insufficient funds");
            log.warn("Proceeding to abort do operation");
            return Mono.error(new IllegalArgumentException("The account has insufficient funds"));
        }

        log.info("Operation successfully validated");
        return Mono.just(accountInDatabase);
    }
    //endregion
}
