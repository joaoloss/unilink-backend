package com.unilink.api.exception;

public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException(String message) {
        super(message);
    }

    public InvalidFieldException() {
        super("Invalid field provided");
    }
}
