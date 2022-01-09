package com.nttdata.bootcamp.passiveoperationsservice.utils;

import com.nttdata.bootcamp.passiveoperationsservice.model.Account;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountCreateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountUpdateRequestDTO;

public interface AccountUtils {
    Account accountCreateRequestDTOToAccount(AccountCreateRequestDTO accountDTO);
    Account accountUpdateRequestDTOToAccount(AccountUpdateRequestDTO accountDTO);
    Account accountDoOperationDTOToAccount(AccountDoOperationRequestDTO accountDTO);
    AccountCreateRequestDTO accountToAccountCreateRequestDTO(Account account);
    AccountUpdateRequestDTO accountToAccountUpdateCreateRequestDTO(Account account);
    AccountDoOperationRequestDTO accountToAccountDoOperationRequestDTO(Account account);
    Account fillAccountWithAccountUpdateCreateRequestDTO(Account account, AccountUpdateRequestDTO accountDTO);
    Account fillAccountWithAccountDoOperationRequestDTO(Account account, AccountDoOperationRequestDTO accountDTO);
}
