package com.nttdata.bootcamp.passiveoperationsservice.config;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ReactiveCircuitBreakerConfig {
    @Bean
    public ReactiveCircuitBreaker customersServiceReactiveCircuitBreaker(ReactiveResilience4JCircuitBreakerFactory reactiveCircuitBreakerFactory) {
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(2))
                .build();

        reactiveCircuitBreakerFactory.configure(builder -> builder
                .timeLimiterConfig(timeLimiterConfig), "customersServiceCircuitBreaker");

        return reactiveCircuitBreakerFactory.create("customersServiceCircuitBreaker");
    }

    @Bean
    public ReactiveCircuitBreaker activesServiceReactiveCircuitBreaker(ReactiveResilience4JCircuitBreakerFactory reactiveCircuitBreakerFactory) {
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(2))
                .build();

        reactiveCircuitBreakerFactory.configure(builder -> builder
                .timeLimiterConfig(timeLimiterConfig), "activesServiceCircuitBreaker");

        return reactiveCircuitBreakerFactory.create("activesServiceCircuitBreaker");
    }
}
