package com.nttdata.bootcamp.passiveoperationsservice.business.impl;

import com.nttdata.bootcamp.passiveoperationsservice.business.AccountService;
import com.nttdata.bootcamp.passiveoperationsservice.model.Account;
import com.nttdata.bootcamp.passiveoperationsservice.model.Customer;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountCreateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountUpdateRequestDTO;
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

        log.info("Validating customer existence");
        if (!accountDTO.getCustomerId().isBlank()) {
            Mono<Account> createdAccount = findByIdCustomerService(accountDTO.getCustomerId())
                    .flatMap(retrievedCustomer -> {
                        log.info("Customer existence successfully validated");

                        log.info("Checking customer's status");
                        if (retrievedCustomer.getStatus().contentEquals(constants.getSTATUS_BLOCKED()))
                        {
                            log.warn("Customer have blocked status");
                            log.warn("Proceeding to abort create account");
                            return Mono.error(new ElementBlockedException("The customer have blocked status"));
                        }

                        Account account = accountUtils.accountCreateRequestDTOToAccount(accountDTO);
                        Customer customer = customerUtils.customerCustomerServiceDTOToCustomer(retrievedCustomer);

                        account.setCustomer(customer);
                        account.setStatus(constants.getSTATUS_ACTIVE());
                        account.setCurrentBalance(0.0);

                        log.info("Checking the new account's holders");
                        if (account.getHolders() == null || account.getHolders().isEmpty()) {
                            log.warn("Account does not contain holders, must have at least one");
                            log.warn("Proceeding to abort create account");
                            return Mono.error(new IllegalArgumentException("Account does not contain holders"));
                        }

                        if (customer.getType().contentEquals(constants.getCUSTOMER_PERSONAL_TYPE()))
                        {
                            log.info("Checking customer's accounts");
                            return findByCustomerId(customer.getId())
                                    .filter(retrievedAccount -> retrievedAccount.getStatus().contentEquals(constants.getSTATUS_ACTIVE()))
                                    .hasElements()
                                    .flatMap(haveAnAccount -> {
                                        if (haveAnAccount) {
                                            log.warn("Can not create more than one account for a personal customer");
                                            log.warn("Proceeding to abort create account");
                                            return Mono.error(new BusinessLogicException("Customer have more than one account"));
                                        }
                                        else {
                                            log.info("Creating new account: [{}]", account.toString());
                                            return accountRepository.insert(account);
                                        }
                                    });
                        } else if (customer.getType().contentEquals(constants.getCUSTOMER_BUSINESS_TYPE())) {
                            log.info("Checking the new account's type");
                            if (account.getAccountType().getDescription().contentEquals(constants.getACCOUNT_CURRENT_TYPE()))
                            {
                                log.info("Creating new account: [{}]", account.toString());
                                return accountRepository.insert(account);
                            } else {
                                log.warn("Can not create {} account for business customers. Accounts must be of current type", account.getAccountType().getDescription());
                                log.warn("Proceeding to abort create account");
                                return Mono.error(new BusinessLogicException("Account is not of current type"));
                            }
                        } else {
                            log.warn("Customer's type is not supported");
                            log.warn("Proceeding to abort create account");
                            return Mono.error(new BusinessLogicException("Not supported customer type"));
                        }
                    }).switchIfEmpty(Mono.error(new NoSuchElementException("Customer does not exist")));

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

        log.info("Validating account existence");
        Mono<Account> updatedAccount = findById(accountDTO.getId())
                .flatMap(retrievedAccount -> {
                    log.info("Customer existence successfully validated");

                    log.info("Checking customer's status");
                    if (retrievedAccount.getCustomer().getStatus().contentEquals(constants.getSTATUS_BLOCKED())) {
                        log.warn("Customer have blocked status");
                        log.warn("Proceeding to abort update account");
                        return Mono.error(new ElementBlockedException("The account have blocked status"));
                    }

                    log.info("Checking the new account's holders");
                    if (accountDTO.getHolders() == null || accountDTO.getHolders().isEmpty()) {
                        log.warn("Account does not contain holders, must have at least one");
                        log.warn("Proceeding to abort update account");
                        return Mono.error(new IllegalArgumentException("Account does not contain holders"));
                    }

                    Account accountToUpdate = accountUtils.fillAccountWithAccountUpdateCreateRequestDTO(retrievedAccount, accountDTO);

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
}
