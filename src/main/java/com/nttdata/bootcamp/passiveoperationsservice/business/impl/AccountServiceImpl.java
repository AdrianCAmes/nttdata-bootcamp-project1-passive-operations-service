package com.nttdata.bootcamp.passiveoperationsservice.business.impl;

import com.nttdata.bootcamp.passiveoperationsservice.business.AccountService;
import com.nttdata.bootcamp.passiveoperationsservice.model.*;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountCreateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountUpdateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.OperationDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.AccountFindBalancesResponseDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.CreditActiveServiceResponseDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.CustomerCustomerServiceResponseDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.OperationCommissionResponseDTO;
import com.nttdata.bootcamp.passiveoperationsservice.repository.AccountRepository;
import com.nttdata.bootcamp.passiveoperationsservice.utils.AccountSpecificationsUtils;
import com.nttdata.bootcamp.passiveoperationsservice.utils.AccountUtils;
import com.nttdata.bootcamp.passiveoperationsservice.utils.CustomerUtils;
import com.nttdata.bootcamp.passiveoperationsservice.config.Constants;
import com.nttdata.bootcamp.passiveoperationsservice.utils.OperationUtils;
import com.nttdata.bootcamp.passiveoperationsservice.utils.errorhandling.BusinessLogicException;
import com.nttdata.bootcamp.passiveoperationsservice.utils.errorhandling.ElementBlockedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final WebClient.Builder webClientBuilder;
    private final AccountUtils accountUtils;
    private final CustomerUtils customerUtils;
    private final OperationUtils operationUtils;
    private final AccountSpecificationsUtils accountSpecificationsUtils;
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
                    Account accountToUpdate = accountUtils.fillAccountWithAccountUpdateRequestDTO(validatedAccount, accountDTO);

                    log.info("Updating account: [{}]", accountToUpdate.toString());
                    Mono<Account> nestedUpdatedAccount = accountRepository.save(accountToUpdate);
                    log.info("Account with id: [{}] was successfully updated", accountToUpdate.getId());

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
        log.info("Account with id: [{}] was successfully deleted", id);

        log.info("End of operation to remove a customer");
        return removedAccount;
    }

    @Override
    public Mono<CustomerCustomerServiceResponseDTO> findByIdCustomerService(String id) {
        log.info("Start of operation to retrieve customer with id [{}] from customer-info-service", id);

        log.info("Retrieving customer");
        String url = constants.getCUSTOMER_INFO_SERVICE_URL() + "/api/v1/customers/" + id;
        Mono<CustomerCustomerServiceResponseDTO> retrievedCustomer = webClientBuilder.build().get()
                .uri(uriBuilder -> uriBuilder
                        .host(constants.getGATEWAY_SERVICE_URL())
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
    public Flux<CreditActiveServiceResponseDTO> findCreditsByCustomerIdActiveService(String id) {
        log.info("Start of operation to retrieve credits of customer with id [{}] from active-operation-service", id);

        log.info("Retrieving credits");
        String url = constants.getACTIVE_OPERATIONS_SERVICE_URL() + "/api/v1/customers/" + id + "/credits";
        Flux<CreditActiveServiceResponseDTO> retrievedCredits = webClientBuilder.build().get()
                .uri(uriBuilder -> uriBuilder
                        .host(constants.getGATEWAY_SERVICE_URL())
                        .path(url)
                        .build())
                .retrieve()
                .onStatus(httpStatus -> httpStatus == HttpStatus.NOT_FOUND, clientResponse -> Mono.empty())
                .bodyToFlux(CreditActiveServiceResponseDTO.class);
        log.info("Customer retrieved successfully");

        log.info("End of operation to retrieve customer with id: [{}]", id);
        return retrievedCredits;
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
                        .flatMap(validatedAccount -> storeOperationIntoAccount(accountDTO, validatedAccount))
                        .flatMap(transformedAccount -> {
                            if (accountDTO.getOperation().getType().contentEquals(constants.getOPERATION_TRANSFER_OUT_TYPE())) {
                                // Creates the operation for the transfer in
                                AccountDoOperationRequestDTO targetAccountOperation = AccountDoOperationRequestDTO.builder()
                                                .id(accountDTO.getTargetId())
                                                .operation(OperationDoOperationRequestDTO.builder()
                                                        .amount(accountDTO.getOperation().getAmount())
                                                        .type(constants.getOPERATION_TRANSFER_IN_TYPE())
                                                        .build())
                                                .build();
                                log.info("Sending operation to receiver account with id: [{}]", accountDTO.getTargetId());
                                return doOperation(targetAccountOperation)
                                        .flatMap(transferInAccount -> accountRepository.save(transformedAccount));
                            } else {
                                log.info("Saving operation into account: [{}]", transformedAccount.toString());
                                return accountRepository.save(transformedAccount);
                            }
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
    public Flux<OperationCommissionResponseDTO> findCommissionsBetweenDatesByAccountId(Date dateFrom, Date dateTo, String id) {
        log.info("Start of operation to retrieve all commissions from account with id: [{}] from [] to []", id, dateFrom, dateTo);

        log.info("Retrieving all commissions");
        Flux<OperationCommissionResponseDTO> retrievedOperations = findById(id)
                .filter(retrievedAccount -> retrievedAccount.getOperations() != null)
                .flux()
                .flatMap(retrievedAccount -> Flux.fromIterable(retrievedAccount.getOperations()))
                .filter(retrievedOperation -> retrievedOperation.getCommission() != null &&
                                                retrievedOperation.getCommission() > 0 &&
                                                retrievedOperation.getTime().after(dateFrom) &&
                                                retrievedOperation.getTime().before(dateTo))
                .map(retrievedOperation -> operationUtils.operationToOperationCommissionResponseDTO(retrievedOperation));
        log.info("Commissions retrieved successfully");

        log.info("End of operation to retrieve commissions from account with id: [{}]", id);
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

    @Override
    public Mono<Account> resetDoneOperationsInMonth(String id) {
        log.info("Start to reset done operations in month");

        Mono<Account> updatedAccount = findById(id)
                .flatMap(retrievedAccount -> {
                    retrievedAccount.setDoneOperationsInMonth(0);

                    log.info("Resetting done operations in month for account wit id: [{}]", id);
                    Mono<Account> nestedUpdatedAccount = accountRepository.save(retrievedAccount);
                    log.info("Done operations in month successfully reseted for account with id: [{}]", id);

                    return nestedUpdatedAccount;
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("Account does not exist")));

        log.info("End to reset done operations in month");
        return updatedAccount;
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

        if (customerFromMicroservice.getCustomerType().getGroup().contentEquals(constants.getCUSTOMER_PERSONAL_GROUP()))
        {
            if (customerFromMicroservice.getCustomerType().getSubgroup().contentEquals(constants.getCUSTOMER_PERSONAL_STANDARD_SUBGROUP()) &&
            accountToCreate.getAccountType().getSubgroup().contentEquals(constants.getACCOUNT_SAVINGS_VIP_SUBGROUP())) {
                log.warn("Standard customers can not create vip accounts");
                log.warn("Proceeding to abort create account");
                return Mono.error(new BusinessLogicException("Standard customer can not create vip accounts"));
            }

            return findByCustomerId(customerFromMicroservice.getId())
                    .filter(retrievedAccount -> retrievedAccount.getStatus().contentEquals(constants.getSTATUS_ACTIVE()))
                    .hasElements()
                    .flatMap(haveAnAccount -> {
                        if (haveAnAccount) {
                            log.warn("Can not create more than one account for a personal customer");
                            log.warn("Proceeding to abort create account");
                            return Mono.error(new BusinessLogicException("Customer have more than one account"));
                        }

                        if (accountToCreate.getAccountType().getSubgroup().contentEquals(constants.getACCOUNT_SAVINGS_VIP_SUBGROUP())) {
                            return findCreditsByCustomerIdActiveService(customerFromMicroservice.getId())
                                    .filter(retrievedCredit -> retrievedCredit.getStatus().contentEquals(constants.getSTATUS_ACTIVE()))
                                    .hasElements()
                                    .flatMap(haveACredit -> {
                                        if (!haveACredit) {
                                            log.warn("Can not create savings vip account because customer does not have a credit product");
                                            log.warn("Proceeding to abort create account");
                                            return Mono.error(new BusinessLogicException("Customer does not have a credit product"));
                                        }

                                        log.info("Account successfully validated");
                                        return Mono.just(customerFromMicroservice);
                                    });
                        } else {
                            log.info("Account successfully validated");
                            return Mono.just(customerFromMicroservice);
                        }
                    });
        } else if (customerFromMicroservice.getCustomerType().getGroup().contentEquals(constants.getCUSTOMER_BUSINESS_GROUP())) {
            if (!accountToCreate.getAccountType().getGroup().contentEquals(constants.getACCOUNT_CURRENT_GROUP()))
            {
                log.warn("Can not create {} account for business customers. Accounts must be of current type", accountToCreate.getAccountType().getGroup());
                log.warn("Proceeding to abort create account");
                return Mono.error(new BusinessLogicException("Account is not of current type"));
            }

            if (customerFromMicroservice.getCustomerType().getSubgroup().contentEquals(constants.getCUSTOMER_BUSINESS_STANDARD_SUBGROUP()) &&
                    accountToCreate.getAccountType().getSubgroup().contentEquals(constants.getACCOUNT_CURRENT_PYME_SUBGROUP())) {
                log.warn("Standard customers can not create pyme accounts");
                log.warn("Proceeding to abort create account");
                return Mono.error(new BusinessLogicException("Standard customer can not create pyme accounts"));
            }

            if (accountToCreate.getAccountType().getSubgroup().contentEquals(constants.getACCOUNT_CURRENT_PYME_SUBGROUP())) {
                return findCreditsByCustomerIdActiveService(customerFromMicroservice.getId())
                        .filter(retrievedCredit -> retrievedCredit.getStatus().contentEquals(constants.getSTATUS_ACTIVE()))
                        .hasElements()
                        .flatMap(haveACredit -> {
                            if (!haveACredit) {
                                log.warn("Can not create current pyme account because customer does not have a credit product");
                                log.warn("Proceeding to abort create account");
                                return Mono.error(new BusinessLogicException("Customer does not have a credit product"));
                            }

                            log.info("Account successfully validated");
                            return Mono.just(customerFromMicroservice);
                        });
            } else {
                log.info("Account successfully validated");
                return Mono.just(customerFromMicroservice);
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

        if (accountToUpdateOperation.getOperation().getType().contentEquals(constants.getOPERATION_WITHDRAWAL_TYPE()) ||
                accountToUpdateOperation.getOperation().getType().contentEquals(constants.getOPERATION_TRANSFER_OUT_TYPE())) {

            Double amountToTake = accountToUpdateOperation.getOperation().getAmount();
            if (accountInDatabase.getAccountSpecifications() != null &&
                    accountInDatabase.getAccountSpecifications().getOperationCommissionPercentage() != null &&
                    accountInDatabase.getAccountSpecifications().getMaximumNumberOfOperations() != null &&
                    accountInDatabase.getDoneOperationsInMonth() != null &&
                    accountInDatabase.getAccountSpecifications().getMaximumNumberOfOperations() <= accountInDatabase.getDoneOperationsInMonth()) {
                amountToTake = accountSpecificationsUtils.roundDouble(accountSpecificationsUtils.applyInterests(amountToTake, accountInDatabase.getAccountSpecifications().getOperationCommissionPercentage()), 2);
            }

            if (accountInDatabase.getCurrentBalance() < amountToTake) {
                log.info("Account has insufficient funds");
                log.warn("Proceeding to abort do operation");
                return Mono.error(new IllegalArgumentException("The account has insufficient funds"));
            }
        }

        if (accountInDatabase.getAccountType().getGroup().contentEquals(constants.getACCOUNT_LONG_TERM_GROUP()) &&
            !accountInDatabase.getAccountSpecifications().getAllowedDayForOperations().equals(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))) {
            log.info("Allowed day [{}] for operations in this account does not match with current day of the month [{}]",
                    accountInDatabase.getAccountSpecifications().getAllowedDayForOperations(),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            log.warn("Proceeding to abort do operation");
            return Mono.error(new BusinessLogicException("Allowed day for operations in this account does not match with current day of the month"));
        }

        log.info("Operation successfully validated");
        return Mono.just(accountInDatabase);
    }

    private Mono<Account> storeOperationIntoAccount(AccountDoOperationRequestDTO accountDTO, Account accountInDatabase) {
        Double commission = 0.0;
        if (!accountDTO.getOperation().getType().contentEquals(constants.getOPERATION_TRANSFER_IN_TYPE())) {
            // Increments the number of done operations
            accountInDatabase.setDoneOperationsInMonth(accountInDatabase.getDoneOperationsInMonth() == null ? 1 : accountInDatabase.getDoneOperationsInMonth() + 1);

            // Calculates the commission
            if (accountInDatabase.getAccountSpecifications() != null &&
                    accountInDatabase.getAccountSpecifications().getMaximumNumberOfOperations() != null &&
                    accountInDatabase.getDoneOperationsInMonth() > accountInDatabase.getAccountSpecifications().getMaximumNumberOfOperations())
            {
                commission = accountSpecificationsUtils.roundDouble(accountSpecificationsUtils.calculateCommission(accountDTO.getOperation().getAmount(), accountInDatabase.getAccountSpecifications().getOperationCommissionPercentage()), 2);
            }
        }

        // Calculates the new current balance
        Double newCurrentBalance = accountSpecificationsUtils.roundDouble((accountDTO.getOperation().getType().contentEquals(constants.getOPERATION_DEPOSIT_TYPE()) ||
                accountDTO.getOperation().getType().contentEquals(constants.getOPERATION_TRANSFER_IN_TYPE())) ?
                accountInDatabase.getCurrentBalance() + accountDTO.getOperation().getAmount() - commission :
                accountInDatabase.getCurrentBalance() - accountDTO.getOperation().getAmount() - commission, 2);
        accountInDatabase.setCurrentBalance(newCurrentBalance);

        // Creates the new operation
        Operation operation = operationUtils.operationDoOperationRequestDTOToOperation(accountDTO.getOperation());
        operation.setTime(new Date());
        operation.setCommission(commission);
        operation.setFinalBalance(newCurrentBalance);

        ArrayList<Operation> operations = accountInDatabase.getOperations() == null ? new ArrayList<>() : accountInDatabase.getOperations();
        operations.add(operation);

        accountInDatabase.setOperations(operations);

        return Mono.just(accountInDatabase);
    }
    //endregion
}
