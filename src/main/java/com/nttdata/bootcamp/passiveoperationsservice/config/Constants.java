package com.nttdata.bootcamp.passiveoperationsservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Constants {
    @Value("${constants.eureka.service-url.customer-info-service}")
    private String customerInfoServiceUrl;

    @Value("${constants.eureka.service-url.active-operations-service}")
    private String activeOperationsServiceUrl;

    @Value("${constants.eureka.service-url.gateway-service}")
    private String gatewayServiceUrl;

    @Value("${constants.customer.personal-group.name}")
    private String customerPersonalGroup;

    @Value("${constants.customer.personal-group.subgroup.standard}")
    private String customerPersonalStandardSubgroup;

    @Value("${constants.customer.personal-group.subgroup.vip}")
    private String customerPersonalVipSubgroup;

    @Value("${constants.customer.business-group.name}")
    private String customerBusinessGroup;

    @Value("${constants.customer.business-group.subgroup.standard}")
    private String customerBusinessStandardSubgroup;

    @Value("${constants.customer.business-group.subgroup.pyme}")
    private String customerBusinessPymeSubgroup;

    @Value("${constants.account.current-group.name}")
    private String accountCurrentGroup;

    @Value("${constants.account.current-group.subgroup.standard}")
    private String accountCurrentStandardSubgroup;

    @Value("${constants.account.current-group.subgroup.pyme}")
    private String accountCurrentPymeSubgroup;

    @Value("${constants.account.savings-group.name}")
    private String accountSavingsGroup;

    @Value("${constants.account.savings-group.subgroup.standard}")
    private String accountSavingsStandardSubgroup;

    @Value("${constants.account.savings-group.subgroup.vip}")
    private String accountSavingsVipSubgroup;

    @Value("${constants.account.long-term-group.name}")
    private String accountLongTermGroup;

    @Value("${constants.account.long-term-group.subgroup.standard}")
    private String accountLongTermStandardSubgroup;

    @Value("${constants.status.blocked}")
    private String statusBlocked;

    @Value("${constants.status.active}")
    private String statusActive;

    @Value("${constants.operation.deposit-type}")
    private String operationDepositType;

    @Value("${constants.operation.withdrawal-type}")
    private String operationWithdrawalType;

    @Value("${constants.operation.transfer-out-type}")
    private String operationTransferOutType;

    @Value("${constants.operation.transfer-in-type}")
    private String operationTransferInType;

    @Value("${constants.circuit-breaker.customer-info-service.name}")
    private String customersServiceCircuitBreakerName;

    @Value("${constants.circuit-breaker.customer-info-service.timeout}")
    private Integer customersServiceCircuitBreakerTimeout;

    @Value("${constants.circuit-breaker.active-operations-service.name}")
    private String activesServiceCircuitBreakerName;

    @Value("${constants.circuit-breaker.active-operations-service.timeout}")
    private Integer activesServiceCircuitBreakerTimeout;
}