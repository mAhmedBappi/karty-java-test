package com.karty.kartyjavatest.exceptions;

public class AuthTokenExpiredException extends RuntimeException {
    public AuthTokenExpiredException(String message) {
        super(message);
    }
}
