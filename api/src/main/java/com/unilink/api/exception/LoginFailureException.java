package com.unilink.api.exception;

public class LoginFailureException extends RuntimeException {
    public LoginFailureException(String message) {
        super(message);
    }

    public LoginFailureException() {
        super("Invalid login credentials");
    }
}
