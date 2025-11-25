package org.example.exceptions;

public class EmailIsExistingException extends RuntimeException {
    public EmailIsExistingException(String message) {
        super(message);
    }
}
