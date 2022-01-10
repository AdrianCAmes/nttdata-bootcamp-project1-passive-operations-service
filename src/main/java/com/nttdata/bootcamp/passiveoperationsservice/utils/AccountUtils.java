package com.nttdata.bootcamp.passiveoperationsservice.utils;

import com.nttdata.bootcamp.passiveoperationsservice.model.Account;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountCreateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountDoOperationRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.request.AccountUpdateRequestDTO;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.AccountFindBalancesResponseDTO;

public interface AccountUtils {
    Account accountCreateRequestDTOToAccount(AccountCreateRequestDTO accountDTO);
    Account accountUpdateRequestDTOToAccount(AccountUpdateRequestDTO accountDTO);
    Account accountDoOperationDTOToAccount(AccountDoOperationRequestDTO accountDTO);
    Account accountFindBalancesResponseDTOToAccount(AccountFindBalancesResponseDTO accountDTO);
    AccountCreateRequestDTO accountToAccountCreateRequestDTO(Account account);
    AccountUpdateRequestDTO accountToAccountUpdateCreateRequestDTO(Account account);
    AccountDoOperationRequestDTO accountToAccountDoOperationRequestDTO(Account account);
    AccountFindBalancesResponseDTO accountToAccountFindBalancesResponseDTO(Account account);
    Account fillAccountWithAccountUpdateRequestDTO(Account account, AccountUpdateRequestDTO accountDTO);
    Account fillAccountWithAccountDoOperationRequestDTO(Account account, AccountDoOperationRequestDTO accountDTO);
}
