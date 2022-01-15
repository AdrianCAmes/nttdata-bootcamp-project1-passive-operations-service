package com.nttdata.bootcamp.passiveoperationsservice.utils;

import com.nttdata.bootcamp.passiveoperationsservice.model.AccountType;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.AccountTypeFindBalancesResponseDTO;

public interface AccountTypeUtils {
    AccountType accountTypeFindBalancesResponseDTOToAccountType(AccountTypeFindBalancesResponseDTO accountTypeDTO);
    AccountTypeFindBalancesResponseDTO accountTypeToAccountTypeFindBalancesResponseDTO(AccountType accountType);
}
