package org.example.exceptions;

public class PhoneNumberIsExistingException extends RuntimeException {
    public PhoneNumberIsExistingException(String message) {
        super(message);
    }
}
