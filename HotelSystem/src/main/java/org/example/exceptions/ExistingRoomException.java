package org.example.exceptions;

public class ExistingRoomException extends RuntimeException {
    public ExistingRoomException(String message) {
        super(message);
    }
}
