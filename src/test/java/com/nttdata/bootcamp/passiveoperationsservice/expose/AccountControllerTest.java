package com.nttdata.bootcamp.passiveoperationsservice.expose;

import com.nttdata.bootcamp.passiveoperationsservice.business.AccountService;
import com.nttdata.bootcamp.passiveoperationsservice.config.Constants;
import com.nttdata.bootcamp.passiveoperationsservice.model.*;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountCreateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountUpdateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.OperationDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.*;
import com.nttdata.bootcamp.passiveoperationsservice.utils.AccountUtils;
import com.nttdata.bootcamp.passiveoperationsservice.utils.OperationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.ArrayList;
import java.util.Date;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private AccountUtils accountUtils;
    @Autowired
    private OperationUtils operationUtils;
    @Autowired
    private Constants constants;
    @MockBean
    private AccountService accountService;

    private static CustomerCustomerServiceResponseDTO customerMock1 = new CustomerCustomerServiceResponseDTO();
    private static CustomerCustomerServiceResponseDTO customerMock2 = new CustomerCustomerServiceResponseDTO();

    private static Account accountMock1 = new Account();
    private static Account accountMock2 = new Account();

    private static Operation operationMock1 = new Operation();

    private static CreditActiveServiceResponseDTO creditMock1 = new CreditActiveServiceResponseDTO();

    @BeforeEach
    void setUpEach() {
        PersonDetailsCustomerServiceResponseDTO representative =  PersonDetailsCustomerServiceResponseDTO.builder()
                .name("Marco")
                .lastname("Cruz")
                .identityNumber("74854687")
                .address(AddressCustomerServiceResponseDTO.builder().
                        number(144)
                        .street("Av. Proceres")
                        .city("Lima")
                        .country("Peru")
                        .build())
                .email("marco.cruz@gmail.com")
                .phoneNumber("5412458")
                .mobileNumber("947854120")
                .birthdate(new Date(2000, 10, 25))
                .build();

        customerMock1 = CustomerCustomerServiceResponseDTO.builder()
                .id("1")
                .customerType(CustomerTypeCustomerServiceResponseDTO
                        .builder()
                        .group(constants.getCustomerPersonalGroup())
                        .subgroup(constants.getCustomerPersonalStandardSubgroup())
                        .build())
                .status(constants.getStatusActive())
                .personDetails(representative)
                .build();

        ArrayList<PersonDetailsCustomerServiceResponseDTO> representatives = new ArrayList<>();
        representatives.add(representative);
        customerMock2 = CustomerCustomerServiceResponseDTO.builder()
                .id("2")
                .customerType(CustomerTypeCustomerServiceResponseDTO
                        .builder()
                        .group(constants.getCustomerBusinessGroup())
                        .subgroup(constants.getCustomerBusinessStandardSubgroup())
                        .build())
                .status(constants.getStatusActive())
                .businessDetails(BusinessDetailsCustomerServiceResponseDTO.builder()
                        .name("NTT Data Peru")
                        .ruc("20874563347")
                        .address(AddressCustomerServiceResponseDTO.builder().
                                number(258)
                                .street("Av. Javier Prado")
                                .city("Lima")
                                .country("Peru")
                                .build())
                        .representatives(representatives)
                        .build())
                .build();

        Person person =  Person.builder()
                .name("Marco")
                .lastname("Cruz")
                .identityNumber("74854687")
                .address(Address.builder().
                        number(144)
                        .street("Av. Proceres")
                        .city("Lima")
                        .country("Peru")
                        .build())
                .email("marco.cruz@gmail.com")
                .phoneNumber("5412458")
                .mobileNumber("947854120")
                .birthdate(new Date(2000, 10, 25))
                .build();
        ArrayList<Person> persons = new ArrayList<>();
        persons.add(person);

        operationMock1 = Operation.builder()
                .operationNumber("123")
                .time(new Date())
                .type(constants.getOperationDepositType())
                .amount(100.0)
                .commission(10.0)
                .finalBalance(500.0)
                .build();
        ArrayList<Operation> operationListMock = new ArrayList<>();
        operationListMock.add(operationMock1);

        accountMock1 = Account.builder()
                .id("1")
                .status(constants.getStatusActive())
                .customer(Customer.builder()
                        .id(customerMock1.getId())
                        .customerType(CustomerType.builder()
                                .group(customerMock1.getCustomerType().getGroup())
                                .subgroup(customerMock1.getCustomerType().getSubgroup())
                                .build())
                        .status(customerMock1.getStatus())
                        .build())
                .accountType(AccountType.builder()
                        .group(constants.getAccountSavingsGroup())
                        .subgroup(constants.getAccountSavingsStandardSubgroup())
                        .build())
                .accountSpecifications(AccountSpecifications.builder()
                        .maximumNumberOfOperations(5)
                        .operationCommissionPercentage(10.0)
                        .build())
                .accountNumber("123")
                .issueDate(new Date())
                .dueDate(new Date())
                .currentBalance(500.0)
                .doneOperationsInMonth(1)
                .operations(operationListMock)
                .holders(persons)
                .signers(persons)
                .build();

        accountMock2 = Account.builder()
                .id("2")
                .status(constants.getStatusActive())
                .customer(Customer.builder()
                        .id(customerMock2.getId())
                        .customerType(CustomerType.builder()
                                .group(customerMock2.getCustomerType().getGroup())
                                .subgroup(customerMock2.getCustomerType().getSubgroup())
                                .build())
                        .status(customerMock2.getStatus())
                        .build())
                .accountType(AccountType.builder()
                        .group(constants.getAccountCurrentGroup())
                        .subgroup(constants.getAccountCurrentStandardSubgroup())
                        .build())
                .accountSpecifications(AccountSpecifications.builder()
                        .maintenanceCommissionPercentage(10.0)
                        .build())
                .accountNumber("456")
                .issueDate(new Date())
                .dueDate(new Date())
                .currentBalance(600.0)
                .doneOperationsInMonth(1)
                .holders(persons)
                .signers(persons)
                .build();

        OperationActiveServiceResponseDTO operationServiceMock1 = OperationActiveServiceResponseDTO.builder()
                .operationNumber("123")
                .time(new Date())
                .type("mock")
                .amount(100.0)
                .billingOrder(BillingOrderActiveServiceResponseDTO.builder()
                        .id("1")
                        .calculatedAmount(220.0)
                        .amountToRefund(200.0)
                        .cycle("01/2022")
                        .status("mock")
                        .build())
                .build();
        ArrayList<OperationActiveServiceResponseDTO> operationServiceListMock = new ArrayList<>();
        operationServiceListMock.add(operationServiceMock1);

        creditMock1 = CreditActiveServiceResponseDTO.builder()
                .id("1")
                .status(constants.getStatusActive())
                .customer(CustomerActiveServiceResponseDTO.builder()
                        .id(customerMock1.getId())
                        .customerType(CustomerTypeActiveServiceResponseDTO.builder()
                                .group(customerMock1.getCustomerType().getGroup())
                                .subgroup(customerMock1.getCustomerType().getSubgroup())
                                .build())
                        .status(customerMock1.getStatus())
                        .build())
                .fullGrantedAmount(700.0)
                .availableAmount(600.0)
                .creditCardNumber("123")
                .issueDate(new Date())
                .dueDate(new Date())
                .operations(operationServiceListMock)
                .billingDetails(BillingDetailsActiveServiceResponseDTO.builder()
                        .interestPercentage(10.0)
                        .closingDay(1)
                        .build())
                .build();
    }

    @Test
    void findAllAccounts() {
        when(accountService.findAll()).thenReturn(Flux.just(accountMock1, accountMock2));

        Flux<Account> retrievedAccounts = webTestClient
                .get()
                .uri("/api/v1/accounts")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Account.class)
                .getResponseBody();

        StepVerifier.create(retrievedAccounts)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(accountMock1.toString()))
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(accountMock2.toString()))
                .verifyComplete();
    }

    @Test
    void findAccountById() {
        when(accountService.findById(accountMock1.getId())).thenReturn(Mono.just(accountMock1));

        Flux<Account> retrievedAccount = webTestClient
                .get()
                .uri("/api/v1/accounts/" + accountMock1.getId())
                .exchange()
                .expectStatus().isOk()
                .returnResult(Account.class)
                .getResponseBody();

        StepVerifier.create(retrievedAccount)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(accountMock1.toString()))
                .verifyComplete();
    }

    @Test
    void createAccount() {
        when(accountService.create(any(AccountCreateRequestDTO.class))).thenReturn(Mono.just(accountMock1));

        AccountCreateRequestDTO accountToSave = accountUtils.accountToAccountCreateRequestDTO(accountMock1);
        Flux<Account> savedAccount = webTestClient
                .post()
                .uri("/api/v1/accounts")
                .body(Mono.just(accountToSave), AccountCreateRequestDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(Account.class)
                .getResponseBody();

        StepVerifier.create(savedAccount)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(accountMock1.toString()))
                .verifyComplete();
    }

    @Test
    void updateAccount() {
        when(accountService.update(any(AccountUpdateRequestDTO.class))).thenReturn(Mono.just(accountMock1));

        AccountUpdateRequestDTO accountToUpdate = accountUtils.accountToAccountUpdateRequestDTO(accountMock1);
        Flux<Account> updatedAccount = webTestClient
                .put()
                .uri("/api/v1/accounts")
                .body(Mono.just(accountToUpdate), AccountUpdateRequestDTO.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Account.class)
                .getResponseBody();

        StepVerifier.create(updatedAccount)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(accountMock1.toString()))
                .verifyComplete();
    }

    @Test
    void deleteAccount() {
        when(accountService.removeById(accountMock1.getId())).thenReturn(Mono.just(accountMock1));

        Flux<Account> removedAccount = webTestClient
                .delete()
                .uri("/api/v1/accounts/" + accountMock1.getId())
                .exchange()
                .expectStatus().isOk()
                .returnResult(Account.class)
                .getResponseBody();

        StepVerifier.create(removedAccount)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(accountMock1.toString()))
                .verifyComplete();
    }

    @Test
    void findAccountsByCustomerId() {
        when(accountService.findByCustomerId(customerMock1.getId())).thenReturn(Flux.just(accountMock1));

        Flux<Account> retrievedAccounts = webTestClient
                .get()
                .uri("/api/v1/customers/" + customerMock1.getId() + "/accounts")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Account.class)
                .getResponseBody();

        StepVerifier.create(retrievedAccounts)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(accountMock1.toString()))
                .verifyComplete();
    }

    @Test
    void findByIdCustomerService() {
        when(accountService.findByIdCustomerService(customerMock1.getId())).thenReturn(Mono.just(customerMock1));

        Flux<CustomerCustomerServiceResponseDTO> retrievedCustomer = webTestClient
                .get()
                .uri("/api/v1/customers-service/" + customerMock1.getId())
                .exchange()
                .expectStatus().isOk()
                .returnResult(CustomerCustomerServiceResponseDTO.class)
                .getResponseBody();

        StepVerifier.create(retrievedCustomer)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(customerMock1.toString()))
                .verifyComplete();
    }

    @Test
    void findCreditsByCustomerIdActiveService() {
        when(accountService.findCreditsByCustomerIdActiveService(customerMock1.getId())).thenReturn(Flux.just(creditMock1));

        Flux<CreditActiveServiceResponseDTO> retrievedCredits = webTestClient
                .get()
                .uri("/api/v1/active-service/customers/" + customerMock1.getId() + "/credits")
                .exchange()
                .expectStatus().isOk()
                .returnResult(CreditActiveServiceResponseDTO.class)
                .getResponseBody();

        StepVerifier.create(retrievedCredits)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(creditMock1.toString()))
                .verifyComplete();
    }

    @Test
    void doOperation() {
        when(accountService.doOperation(any(AccountDoOperationRequestDTO.class))).thenReturn(Mono.just(accountMock1));

        AccountDoOperationRequestDTO accountToUpdate = AccountDoOperationRequestDTO.builder()
                .id(accountMock1.getId())
                .operation(OperationDoOperationRequestDTO.builder()
                        .type(constants.getOperationWithdrawalType())
                        .amount(100.0)
                        .build())
                .build();
        Flux<Account> updatedAccount = webTestClient
                .post()
                .uri("/api/v1/accounts/operations")
                .body(Mono.just(accountToUpdate), AccountDoOperationRequestDTO.class)
                .exchange()
                .expectStatus().isCreated()
                .returnResult(Account.class)
                .getResponseBody();

        StepVerifier.create(updatedAccount)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(accountMock1.toString()))
                .verifyComplete();
    }

    @Test
    void resetDoneOperationsInMonth() {
        when(accountService.resetDoneOperationsInMonth(accountMock1.getId())).thenReturn(Mono.just(accountMock1));

        Flux<Account> updatedAccount = webTestClient
                .put()
                .uri("api/v1/accounts/" + accountMock1.getId() + "/reset-operations-number")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Account.class)
                .getResponseBody();

        StepVerifier.create(updatedAccount)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(accountMock1.toString()))
                .verifyComplete();
    }

    @Test
    void findOperationsByAccountId() {
        when(accountService.findOperationsByAccountId(accountMock1.getId())).thenReturn(Flux.just(operationMock1));

        Flux<Operation> updatedAccount = webTestClient
                .get()
                .uri("/api/v1/accounts/" + accountMock1.getId() + "/operations")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Operation.class)
                .getResponseBody();

        StepVerifier.create(updatedAccount)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(operationMock1.toString()))
                .verifyComplete();
    }

    @Test
    void findCommissionsBetweenDatesByAccountId() {
        when(accountService.findCommissionsBetweenDatesByAccountId(any(Date.class), any(Date.class), eq(accountMock1.getId()))).thenReturn(Flux.just(operationUtils.operationToOperationCommissionResponseDTO(operationMock1)));

        Flux<OperationCommissionResponseDTO> retrievedAccounts = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/accounts/" + accountMock1.getId() + "/commissions")
                        .queryParam("date-from", "2022-11-10")
                        .queryParam("date-to", "2022-11-10")
                        .build())

                .exchange()
                .expectStatus().isOk()
                .returnResult(OperationCommissionResponseDTO.class)
                .getResponseBody();

        StepVerifier.create(retrievedAccounts)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(operationUtils.operationToOperationCommissionResponseDTO(operationMock1).toString()))
                .verifyComplete();
    }

    @Test
    void findBalancesByCustomerId() {
        when(accountService.findBalancesByCustomerId(customerMock1.getId())).thenReturn(Flux.just(accountUtils.accountToAccountFindBalancesResponseDTO(accountMock1)));

        Flux<AccountFindBalancesResponseDTO> retrievedAccounts = webTestClient
                .get()
                .uri("/api/v1/customers/" + customerMock1.getId() + "/accounts/balance")
                .exchange()
                .expectStatus().isOk()
                .returnResult(AccountFindBalancesResponseDTO.class)
                .getResponseBody();

        StepVerifier.create(retrievedAccounts)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(accountUtils.accountToAccountFindBalancesResponseDTO(accountMock1).toString()))
                .verifyComplete();
    }
}