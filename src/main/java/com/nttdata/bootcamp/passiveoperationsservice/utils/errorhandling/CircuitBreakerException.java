package com.nttdata.bootcamp.passiveoperationsservice.utils.errorhandling;

public class CircuitBreakerException extends RuntimeException {
    private static final long serialVersionUID = -5713584292717311040L;

    public CircuitBreakerException(String s) {
        super(s);
    }
}
