package com.senla.cars.serviceImpl.exception;

public class NotEmptyException extends RuntimeException{
    public NotEmptyException(String message){
        super(message);
    }
}
