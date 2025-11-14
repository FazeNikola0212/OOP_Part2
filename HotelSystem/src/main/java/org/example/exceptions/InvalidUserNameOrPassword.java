package org.example.exceptions;

public class InvalidUserNameOrPassword extends RuntimeException {
    public InvalidUserNameOrPassword(String message) {
        super(message);
    }
}
