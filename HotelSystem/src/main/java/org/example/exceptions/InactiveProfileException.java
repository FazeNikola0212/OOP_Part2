package org.example.exceptions;

public class InactiveProfileException extends RuntimeException {
    public InactiveProfileException(String message) {
        super(message);
    }
}
