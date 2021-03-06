package com.nttdata.bootcamp.passiveoperationsservice.utils.impl;

import com.nttdata.bootcamp.passiveoperationsservice.model.Account;
import com.nttdata.bootcamp.passiveoperationsservice.model.Customer;
import com.nttdata.bootcamp.passiveoperationsservice.model.Operation;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountCreateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountUpdateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.AccountFindBalancesResponseDTO;
import com.nttdata.bootcamp.passiveoperationsservice.utils.AccountTypeUtils;
import com.nttdata.bootcamp.passiveoperationsservice.utils.AccountUtils;
import com.nttdata.bootcamp.passiveoperationsservice.utils.OperationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountUtilsImpl implements AccountUtils {
    private final OperationUtils operationUtils;
    private final AccountTypeUtils accountTypeUtils;

    public Account accountCreateRequestDTOToAccount(AccountCreateRequestDTO accountDTO){
        return Account.builder()
                .customer(Customer.builder().id(accountDTO.getCustomerId()).build())
                .accountType(accountDTO.getAccountType())
                .accountSpecifications(accountDTO.getAccountSpecifications())
                .accountNumber(UUID.randomUUID().toString())
                .issueDate(accountDTO.getIssueDate())
                .dueDate(accountDTO.getDueDate())
                .holders(accountDTO.getHolders())
                .signers(accountDTO.getSigners())
                .build();
    }

    public Account accountUpdateRequestDTOToAccount(AccountUpdateRequestDTO accountDTO){
        return Account.builder()
                .id(accountDTO.getId())
                .status(accountDTO.getStatus())
                .dueDate(accountDTO.getDueDate())
                .currentBalance(accountDTO.getCurrentBalance())
                .doneOperationsInMonth(accountDTO.getDoneOperationsInMonth())
                .operations(accountDTO.getOperations())
                .holders(accountDTO.getHolders())
                .signers(accountDTO.getSigners())
                .build();
    }

    @Override
    public Account accountDoOperationDTOToAccount(AccountDoOperationRequestDTO accountDTO) {
        Operation operation = operationUtils.operationDoOperationRequestDTOToOperation(accountDTO.getOperation());
        return Account.builder()
                .id(accountDTO.getId())
                .operations(new ArrayList<>(Arrays.asList(operation)))
                .build();
    }

    @Override
    public Account accountFindBalancesResponseDTOToAccount(AccountFindBalancesResponseDTO accountDTO) {
        return Account.builder()
                .id(accountDTO.getId())
                .currentBalance(accountDTO.getCurrentBalance())
                .accountType(accountTypeUtils.accountTypeFindBalancesResponseDTOToAccountType(accountDTO.getAccountType()))
                .build();
    }

    @Override
    public AccountCreateRequestDTO accountToAccountCreateRequestDTO(Account account) {
        return AccountCreateRequestDTO.builder()
                .customerId(account.getCustomer().getId())
                .accountType(account.getAccountType())
                .accountSpecifications(account.getAccountSpecifications())
                .issueDate(account.getIssueDate())
                .dueDate(account.getDueDate())
                .holders(account.getHolders())
                .signers(account.getSigners())
                .build();
    }

    @Override
    public AccountUpdateRequestDTO accountToAccountUpdateRequestDTO(Account account) {
        return AccountUpdateRequestDTO.builder()
                .id(account.getId())
                .dueDate(account.getDueDate())
                .currentBalance(account.getCurrentBalance())
                .doneOperationsInMonth(account.getDoneOperationsInMonth())
                .operations(account.getOperations())
                .holders(account.getHolders())
                .signers(account.getSigners())
                .build();
    }

    @Override
    public AccountDoOperationRequestDTO accountToAccountDoOperationRequestDTO(Account account) {
        return AccountDoOperationRequestDTO.builder()
                .id(account.getId())
                .operation(operationUtils.operationToOperationDoOperationRequestDTO(account.getOperations().get(0)))
                .build();
    }

    @Override
    public AccountFindBalancesResponseDTO accountToAccountFindBalancesResponseDTO(Account account) {
        return AccountFindBalancesResponseDTO.builder()
                .id(account.getId())
                .currentBalance(account.getCurrentBalance())
                .accountType(accountTypeUtils.accountTypeToAccountTypeFindBalancesResponseDTO(account.getAccountType()))
                .build();
    }

    @Override
    public Account fillAccountWithAccountUpdateRequestDTO(Account account, AccountUpdateRequestDTO accountDTO) {
        account.setId(accountDTO.getId());
        account.setStatus(accountDTO.getStatus());
        account.setDueDate(accountDTO.getDueDate());
        account.setCurrentBalance(accountDTO.getCurrentBalance());
        account.setDoneOperationsInMonth(accountDTO.getDoneOperationsInMonth());
        account.setOperations(accountDTO.getOperations());
        account.setHolders(accountDTO.getHolders());
        account.setSigners(accountDTO.getSigners());
        return account;
    }
}