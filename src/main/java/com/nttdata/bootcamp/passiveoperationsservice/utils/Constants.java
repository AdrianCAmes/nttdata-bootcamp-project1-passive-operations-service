package com.nttdata.bootcamp.passiveoperationsservice.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Constants {
    @Value("${constants.eureka.service_url.customer_info_service}")
    private String CUSTOMER_INFO_SERVICE_URL;
}