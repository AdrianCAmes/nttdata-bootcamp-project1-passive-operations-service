package com.nttdata.bootcamp.passiveoperationsservice.config;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
public class ReactiveCircuitBreakerConfig {
    private final Constants constants;

    @Bean
    public ReactiveCircuitBreaker customersServiceReactiveCircuitBreaker(ReactiveResilience4JCircuitBreakerFactory reactiveCircuitBreakerFactory) {
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(constants.getCustomersServiceCircuitBreakerTimeout()))
                .build();

        reactiveCircuitBreakerFactory.configure(builder -> builder
                .timeLimiterConfig(timeLimiterConfig), constants.getCustomersServiceCircuitBreakerName());

        return reactiveCircuitBreakerFactory.create(constants.getCustomersServiceCircuitBreakerName());
    }

    @Bean
    public ReactiveCircuitBreaker activesServiceReactiveCircuitBreaker(ReactiveResilience4JCircuitBreakerFactory reactiveCircuitBreakerFactory) {
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(constants.getActivesServiceCircuitBreakerTimeout()))
                .build();

        reactiveCircuitBreakerFactory.configure(builder -> builder
                .timeLimiterConfig(timeLimiterConfig), constants.getActivesServiceCircuitBreakerName());

        return reactiveCircuitBreakerFactory.create(constants.getActivesServiceCircuitBreakerName());
    }
}
