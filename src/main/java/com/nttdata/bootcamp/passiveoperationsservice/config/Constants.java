package com.nttdata.bootcamp.passiveoperationsservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Constants {
    @Value("${constants.eureka.service_url.customer_info_service:}")
    private String customerInfoServiceUrl;

    @Value("${constants.eureka.service_url.active_operations_service:}")
    private String activeOperationsServiceUrl;

    @Value("${constants.eureka.service_url.gateway_service:}")
    private String gatewayServiceUrl;

    @Value("${constants.customer.personal_group.name:}")
    private String customerPersonalGroup;

    @Value("${constants.customer.personal_group.subgroup.standard:}")
    private String customerPersonalStandardSubgroup;

    @Value("${constants.customer.personal_group.subgroup.vip:}")
    private String customerPersonalVipSubgroup;

    @Value("${constants.customer.business_group.name:}")
    private String customerBusinessGroup;

    @Value("${constants.customer.business_group.subgroup.standard:}")
    private String customerBusinessStandardSubgroup;

    @Value("${constants.customer.business_group.subgroup.pyme:}")
    private String customerBusinessPymeSubgroup;

    @Value("${constants.account.current_group.name:}")
    private String accountCurrentGroup;

    @Value("${constants.account.current_group.subgroup.standard:}")
    private String accountCurrentStandardSubgroup;

    @Value("${constants.account.current_group.subgroup.pyme:}")
    private String accountCurrentPymeSubgroup;

    @Value("${constants.account.savings_group.name:}")
    private String accountSavingsGroup;

    @Value("${constants.account.savings_group.subgroup.standard:}")
    private String accountSavingsStandardSubgroup;

    @Value("${constants.account.savings_group.subgroup.vip:}")
    private String accountSavingsVipSubgroup;

    @Value("${constants.account.long_term_group.name:}")
    private String accountLongTermGroup;

    @Value("${constants.account.long_term_group.subgroup.standard:}")
    private String accountLongTermStandardSubgroup;

    @Value("${constants.status.blocked:}")
    private String statusBlocked;

    @Value("${constants.status.active:}")
    private String statusActive;

    @Value("${constants.operation.deposit_type:}")
    private String operationDepositType;

    @Value("${constants.operation.withdrawal_type:}")
    private String operationWithdrawalType;

    @Value("${constants.operation.transfer_out_type:}")
    private String operationTransferOutType;

    @Value("${constants.operation.transfer_in_type:}")
    private String operationTransferInType;
}