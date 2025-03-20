package com.assessment.banking.exceptions;

import java.util.UUID;

public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String reason) {
        super(String.format("Could not perform the operation, %s", reason));
    }
}
