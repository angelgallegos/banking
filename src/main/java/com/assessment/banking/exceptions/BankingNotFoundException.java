package com.assessment.banking.exceptions;

import java.util.UUID;

public class BankingNotFoundException extends RuntimeException {

    public BankingNotFoundException(String resource) {
        super(String.format("Could not find %s", resource));
    }
    public BankingNotFoundException(String resource, UUID id) {
        super(String.format("Could not find %s with id %s", resource, id));
    }
}