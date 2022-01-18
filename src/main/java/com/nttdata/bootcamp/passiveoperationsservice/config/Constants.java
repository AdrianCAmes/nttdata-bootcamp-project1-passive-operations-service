package com.nttdata.bootcamp.passiveoperationsservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Constants {
    @Value("${constants.eureka.service_url.customer_info_service:}")
    private String CUSTOMER_INFO_SERVICE_URL;

    @Value("${constants.eureka.service_url.active_operations_service:}")
    private String ACTIVE_OPERATIONS_SERVICE_URL;

    @Value("${constants.eureka.service_url.gateway_service:}")
    private String GATEWAY_SERVICE_URL;

    @Value("${constants.customer.personal_group.name:}")
    private String CUSTOMER_PERSONAL_GROUP;

    @Value("${constants.customer.personal_group.subgroup.standard:}")
    private String CUSTOMER_PERSONAL_STANDARD_SUBGROUP;

    @Value("${constants.customer.personal_group.subgroup.vip:}")
    private String CUSTOMER_PERSONAL_VIP_SUBGROUP;

    @Value("${constants.customer.business_group.name:}")
    private String CUSTOMER_BUSINESS_GROUP;

    @Value("${constants.customer.business_group.subgroup.standard:}")
    private String CUSTOMER_BUSINESS_STANDARD_SUBGROUP;

    @Value("${constants.customer.business_group.subgroup.pyme:}")
    private String CUSTOMER_BUSINESS_PYME_SUBGROUP;

    @Value("${constants.account.current_group.name:}")
    private String ACCOUNT_CURRENT_GROUP;

    @Value("${constants.account.current_group.subgroup.standard:}")
    private String ACCOUNT_CURRENT_STANDARD_SUBGROUP;

    @Value("${constants.account.current_group.subgroup.pyme:}")
    private String ACCOUNT_CURRENT_PYME_SUBGROUP;

    @Value("${constants.account.savings_group.name:}")
    private String ACCOUNT_SAVINGS_GROUP;

    @Value("${constants.account.savings_group.subgroup.standard:}")
    private String ACCOUNT_SAVINGS_STANDARD_SUBGROUP;

    @Value("${constants.account.savings_group.subgroup.vip:}")
    private String ACCOUNT_SAVINGS_VIP_SUBGROUP;

    @Value("${constants.account.long_term_group.name:}")
    private String ACCOUNT_LONG_TERM_GROUP;

    @Value("${constants.account.long_term_group.subgroup.standard:}")
    private String ACCOUNT_LONG_TERM_STANDARD_SUBGROUP;

    @Value("${constants.status.blocked:}")
    private String STATUS_BLOCKED;

    @Value("${constants.status.active:}")
    private String STATUS_ACTIVE;

    @Value("${constants.operation.deposit_type:}")
    private String OPERATION_DEPOSIT_TYPE;

    @Value("${constants.operation.withdrawal_type:}")
    private String OPERATION_WITHDRAWAL_TYPE;

    @Value("${constants.operation.transfer_out_type:}")
    private String OPERATION_TRANSFER_OUT_TYPE;

    @Value("${constants.operation.transfer_in_type:}")
    private String OPERATION_TRANSFER_IN_TYPE;
}