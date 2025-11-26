package org.example.exceptions;

public class AmenityNameAlreadyExistsException extends RuntimeException {
    public AmenityNameAlreadyExistsException(String message) {
        super(message);
    }
}
