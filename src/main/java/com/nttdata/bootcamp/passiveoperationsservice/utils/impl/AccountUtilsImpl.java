package com.nttdata.bootcamp.passiveoperationsservice.utils.impl;

import com.nttdata.bootcamp.passiveoperationsservice.model.Account;
import com.nttdata.bootcamp.passiveoperationsservice.model.Customer;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountCreateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountUpdateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class AccountUtilsImpl implements AccountUtils {
    public Account accountCreateRequestDTOToAccount(AccountCreateRequestDTO accountDTO){
        return Account.builder()
                .customer(Customer.builder().id(accountDTO.getCustomerId()).build())
                .accountType(accountDTO.getAccountType())
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
                .operations(accountDTO.getOperations())
                .holders(accountDTO.getHolders())
                .signers(accountDTO.getSigners())
                .build();
    }

    public AccountCreateRequestDTO accountToAccountCreateRequestDTO(Account account) {
        return AccountCreateRequestDTO.builder()
                .customerId(account.getCustomer().getId())
                .accountType(account.getAccountType())
                .issueDate(account.getIssueDate())
                .dueDate(account.getDueDate())
                .holders(account.getHolders())
                .signers(account.getSigners())
                .build();
    }

    public AccountUpdateRequestDTO accountToAccountUpdateCreateRequestDTO(Account account) {
        return AccountUpdateRequestDTO.builder()
                .id(account.getId())
                .dueDate(account.getDueDate())
                .currentBalance(account.getCurrentBalance())
                .operations(account.getOperations())
                .holders(account.getHolders())
                .signers(account.getSigners())
                .build();
    }

    public Account fillAccountWithAccountUpdateCreateRequestDTO(Account account, AccountUpdateRequestDTO accountDTO) {
        account.setId(accountDTO.getId());
        account.setStatus(accountDTO.getStatus());
        account.setDueDate(accountDTO.getDueDate());
        account.setCurrentBalance(accountDTO.getCurrentBalance());
        account.setOperations(accountDTO.getOperations());
        account.setHolders(accountDTO.getHolders());
        account.setSigners(accountDTO.getSigners());
        return account;
    }
}
