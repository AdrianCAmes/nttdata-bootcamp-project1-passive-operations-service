package com.nttdata.bootcamp.passiveoperationsservice.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountFindBalancesResponseDTO {
    private String id;
    private AccountTypeFindBalancesResponseDTO accountType;
    private Double currentBalance;
}
