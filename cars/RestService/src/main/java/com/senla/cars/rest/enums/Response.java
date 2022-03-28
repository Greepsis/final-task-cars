package com.senla.cars.rest.enums;

public enum Response {
    HAS_BEEN_DELETED("%s - has successfully deleted"),
    SAVED("%s - has successfully saved"),
    HAS_BEEN_UPDATED("%s - has successfully updated"),
    NO_ERROR(""),
    HAS_NOT_BEEN_DELETED("%s - has not deleted"),
    SUCCESS, FAILED;

    private String message;

    Response() {
    }

    Response(String message) {
        this.message = message;
    }

    public String getText() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
