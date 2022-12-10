package org.passwordBreaker.exceptions;

public class PasswordsNotFoundException extends RuntimeException {
    public  PasswordsNotFoundException(String message) {
        super(message);
    }
}