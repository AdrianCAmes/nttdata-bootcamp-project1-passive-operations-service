package com.nttdata.bootcamp.passiveoperationsservice.business.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nttdata.bootcamp.passiveoperationsservice.business.AccountService;
import com.nttdata.bootcamp.passiveoperationsservice.config.Constants;
import com.nttdata.bootcamp.passiveoperationsservice.model.*;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountCreateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountUpdateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.OperationDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.*;
import com.nttdata.bootcamp.passiveoperationsservice.repository.AccountRepository;
import com.nttdata.bootcamp.passiveoperationsservice.utils.AccountUtils;
import com.nttdata.bootcamp.passiveoperationsservice.utils.OperationUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.lang.time.DateUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest()
class AccountServiceImplTest {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountUtils accountUtils;
    @Autowired
    private OperationUtils operationUtils;
    @Autowired
    private Constants constants;
    @MockBean
    private AccountRepository accountRepository;

    private static MockWebServer mockBackEnd;

    private static CustomerCustomerServiceResponseDTO customerMock1 = new CustomerCustomerServiceResponseDTO();
    private static CustomerCustomerServiceResponseDTO customerMock2 = new CustomerCustomerServiceResponseDTO();

    private static Account accountMock1 = new Account();
    private static Account accountMock2 = new Account();

    private static Operation operationMock1 = new Operation();

    private static CreditActiveServiceResponseDTO creditMock1 = new CreditActiveServiceResponseDTO();

    @BeforeAll
    static void setUp(@Value("${server.port}") int port) throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start(port);
    }

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

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void create() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();
        mockBackEnd.enqueue(new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(gson.toJson(customerMock1))
                .setResponseCode(HttpStatus.OK.value()));

        when(accountRepository.findAccountsByCustomerId(accountMock1.getCustomer().getId())).thenReturn(Flux.empty());
        when(accountRepository.insert(any(Account.class))).thenReturn(Mono.just(accountMock1));

        AccountCreateRequestDTO accountToSave = accountUtils.accountToAccountCreateRequestDTO(accountMock1);
        Mono<Account> savedAccount = accountService.create(accountToSave);

        StepVerifier.create(savedAccount)
                .expectSubscription()
                .expectNext(accountMock1)
                .verifyComplete();
    }

    @Test
    void findById() {
        when(accountRepository.findById(accountMock1.getId())).thenReturn(Mono.just(accountMock1));

        Mono<Account> retrievedAccount = accountService.findById(accountMock1.getId());

        StepVerifier.create(retrievedAccount)
                .expectSubscription()
                .expectNext(accountMock1)
                .verifyComplete();
    }

    @Test
    void findAll() {
        when(accountRepository.findAll()).thenReturn(Flux.just(accountMock1, accountMock2));

        Flux<Account> retrievedAccounts = accountService.findAll() ;

        StepVerifier.create(retrievedAccounts)
                .expectSubscription()
                .expectNext(accountMock1)
                .expectNext(accountMock2)
                .verifyComplete();
    }

    @Test
    void update() {
        when(accountRepository.findById(accountMock1.getId())).thenReturn(Mono.just(accountMock1));
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(accountMock1));

        AccountUpdateRequestDTO accountToUpdate = accountUtils.accountToAccountUpdateRequestDTO(accountMock1);
        Mono<Account> updatedAccount = accountService.update(accountToUpdate);

        StepVerifier.create(updatedAccount)
                .expectSubscription()
                .expectNext(accountMock1)
                .verifyComplete();
    }

    @Test
    void removeById() {
        when(accountRepository.findById(accountMock1.getId())).thenReturn(Mono.just(accountMock1));
        when(accountRepository.deleteById(accountMock1.getId())).thenReturn(Mono.empty());

        Mono<Account> removedAccount = accountService.removeById(accountMock1.getId());

        StepVerifier.create(removedAccount)
                .expectSubscription()
                .expectNext(accountMock1)
                .verifyComplete();
    }

    @Test
    void findByIdCustomerService() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();
        mockBackEnd.enqueue(new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(gson.toJson(customerMock1))
                .setResponseCode(HttpStatus.OK.value()));

        Mono<CustomerCustomerServiceResponseDTO> retrievedCustomer = accountService.findByIdCustomerService(customerMock1.getId());

        StepVerifier.create(retrievedCustomer)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(customerMock1.toString()))
                .verifyComplete();
    }

    @Test
    void findCreditsByCustomerIdActiveService() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();
        mockBackEnd.enqueue(new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(gson.toJson(creditMock1))
                .setResponseCode(HttpStatus.OK.value()));

        Flux<CreditActiveServiceResponseDTO> retrievedCredit = accountService.findCreditsByCustomerIdActiveService(customerMock1.getId());

        StepVerifier.create(retrievedCredit)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(creditMock1.toString()))
                .verifyComplete();
    }

    @Test
    void findByCustomerId() {
        when(accountRepository.findAccountsByCustomerId(accountMock1.getCustomer().getId())).thenReturn(Flux.just(accountMock1));

        Flux<Account> retrievedAccount = accountService.findByCustomerId(accountMock1.getCustomer().getId());

        StepVerifier.create(retrievedAccount)
                .expectSubscription()
                .expectNext(accountMock1)
                .verifyComplete();
    }

    @Test
    void doOperation() {
        when(accountRepository.findById(accountMock1.getId())).thenReturn(Mono.just(accountMock1));
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(accountMock1));

        AccountDoOperationRequestDTO accountToUpdate = AccountDoOperationRequestDTO.builder()
                .id(accountMock1.getId())
                .operation(OperationDoOperationRequestDTO.builder()
                        .type(constants.getOperationWithdrawalType())
                        .amount(100.0)
                        .build())
                .build();
        Mono<Account> updatedAccount = accountService.doOperation(accountToUpdate);

        StepVerifier.create(updatedAccount)
                .expectSubscription()
                .expectNext(accountMock1)
                .verifyComplete();
    }

    @Test
    void findOperationsByAccountId() {
        when(accountRepository.findById(accountMock1.getId())).thenReturn(Mono.just(accountMock1));

        Flux<Operation> retrievedOperations = accountService.findOperationsByAccountId(accountMock1.getId());

        StepVerifier.create(retrievedOperations)
                .expectSubscription()
                .expectNext(operationMock1)
                .verifyComplete();
    }

    @Test
    void findCommissionsBetweenDatesByAccountId() {
        when(accountRepository.findById(accountMock1.getId())).thenReturn(Mono.just(accountMock1));
        Date dateFrom = DateUtils.addDays(new Date(), -2);
        Date dateTo = DateUtils.addDays(new Date(), 2);

        Flux<OperationCommissionResponseDTO> retrievedCommissions = accountService.findCommissionsBetweenDatesByAccountId(dateFrom, dateTo, accountMock1.getCustomer().getId());

        StepVerifier.create(retrievedCommissions)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(operationUtils.operationToOperationCommissionResponseDTO(operationMock1).toString()))
                .verifyComplete();
    }

    @Test
    void findBalancesByCustomerId() {
        when(accountRepository.findAccountsByCustomerId(accountMock1.getCustomer().getId())).thenReturn(Flux.just(accountMock1));

        Flux<AccountFindBalancesResponseDTO> retrievedBalances = accountService.findBalancesByCustomerId(accountMock1.getCustomer().getId());

        StepVerifier.create(retrievedBalances)
                .expectSubscription()
                .expectNextMatches(retrieved -> retrieved.toString().contentEquals(accountUtils.accountToAccountFindBalancesResponseDTO(accountMock1).toString()))
                .verifyComplete();
    }

    @Test
    void resetDoneOperationsInMonth() {
        when(accountRepository.findById(accountMock1.getId())).thenReturn(Mono.just(accountMock1));
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(accountMock1));

        AccountUpdateRequestDTO accountToUpdate = accountUtils.accountToAccountUpdateRequestDTO(accountMock1);
        Mono<Account> updatedAccount = accountService.update(accountToUpdate);

        StepVerifier.create(updatedAccount)
                .expectSubscription()
                .expectNext(accountMock1)
                .verifyComplete();
    }
}