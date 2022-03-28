package com.senla.cars.serviceImpl.exception.auth;

public class DeletedUserException extends RuntimeException {
    public DeletedUserException(String message){
        super(message);
    }
}
