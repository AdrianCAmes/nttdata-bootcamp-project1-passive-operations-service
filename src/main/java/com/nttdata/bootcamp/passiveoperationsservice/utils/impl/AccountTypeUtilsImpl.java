package com.nttdata.bootcamp.passiveoperationsservice.utils.impl;

import com.nttdata.bootcamp.passiveoperationsservice.model.AccountType;
import com.nttdata.bootcamp.passiveoperationsservice.model.dto.response.AccountTypeFindBalancesResponseDTO;
import com.nttdata.bootcamp.passiveoperationsservice.utils.AccountTypeUtils;
import org.springframework.stereotype.Component;

@Component
public class AccountTypeUtilsImpl implements AccountTypeUtils {
    @Override
    public AccountType accountTypeFindBalancesResponseDTOToAccountType(AccountTypeFindBalancesResponseDTO accountTypeDTO) {
        return AccountType.builder()
                .group(accountTypeDTO.getGroup())
                .subgroup(accountTypeDTO.getSubgroup())
                .build();
    }

    @Override
    public AccountTypeFindBalancesResponseDTO accountTypeToAccountTypeFindBalancesResponseDTO(AccountType accountType) {
        return AccountTypeFindBalancesResponseDTO.builder()
                .group(accountType.getGroup())
                .subgroup(accountType.getSubgroup())
                .build();
    }
}
