package com.senla.cars.serviceImpl.exception.auth;

public class BlockedEmailException extends RuntimeException{
    public BlockedEmailException(String message) {
        super(message);
    }
}
