package com.nttdata.bootcamp.passiveoperationsservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Constants {
    @Value("${constants.eureka.service_url.customer_info_service}")
    private String CUSTOMER_INFO_SERVICE_URL;

    @Value("${constants.customer.personal_type}")
    private String CUSTOMER_PERSONAL_TYPE;

    @Value("${constants.customer.business_type}")
    private String CUSTOMER_BUSINESS_TYPE;

    @Value("${constants.account.current_type}")
    private String ACCOUNT_CURRENT_TYPE;

    @Value("${constants.account.savings_type}")
    private String ACCOUNT_SAVINGS_TYPE;

    @Value("${constants.account.long_term_type}")
    private String ACCOUNT_LONG_TERM_TYPE;

    @Value("${constants.status.blocked}")
    private String STATUS_BLOCKED;

    @Value("${constants.status.active}")
    private String STATUS_ACTIVE;

    @Value("${constants.operation.deposit_type}")
    private String OPERATION_DEPOSIT_TYPE;

    @Value("${constants.operation.withdrawal_type}")
    private String OPERATION_WITHDRAWAL_TYPE;
}