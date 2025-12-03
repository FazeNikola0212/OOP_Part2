package org.example.exceptions;

public class NotFoundManagersException extends RuntimeException {
    public NotFoundManagersException(String message) {
        super(message);
    }
}
