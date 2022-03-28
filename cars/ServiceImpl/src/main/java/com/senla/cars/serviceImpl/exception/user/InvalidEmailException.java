package com.senla.cars.serviceImpl.exception.user;

public class InvalidEmailException extends RuntimeException{
    public InvalidEmailException() {
        super("Invalid email!");
    }

}
