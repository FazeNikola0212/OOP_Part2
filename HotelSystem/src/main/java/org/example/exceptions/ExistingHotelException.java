package org.example.exceptions;

public class ExistingHotelException extends RuntimeException {
    public ExistingHotelException(String message) {
        super(message);
    }
}
