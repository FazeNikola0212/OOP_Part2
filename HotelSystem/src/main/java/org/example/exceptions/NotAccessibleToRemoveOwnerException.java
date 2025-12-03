package org.example.exceptions;

public class NotAccessibleToRemoveOwnerException extends RuntimeException {
    public NotAccessibleToRemoveOwnerException(String message) {
        super(message);
    }
}
