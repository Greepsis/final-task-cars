package com.senla.cars.serviceImpl.enums;

public enum ExceptionEnums {
    USER("USER "),USERS("USERS "),
    USER_NAME("USER_NAME "),
    USER_EMAIL("USER_EMAIL "),
    MODEL("MODEL "),MODELS("MODELS "),
    COMMENT("COMMENT "),COMMENTS("COMMENTS "),
    BRAND("BRAND "),BRANDS("BRANDS "),
    MODEL_NAME("MODEL NAME "),
    MODEL_ID("MODEL_ID "),
    BRAND_NAME("BRAND NAME "),
    BRAND_ID("BRAND_ID "),
    AD("AD "),ADS("ADS "),
    EMAIL("EMAIL "),EMAILS("EMAILS "),
    ID("ID "),
    BOOKMARK("BOOKMARK "),
    PASSWORD("PASSWORD "),
    PARAMETER("PARAMETER "),
    PROMOTION("PROMOTION"),
    SEARCH_CRITERIA("SEARCH CRITERIA"),
    PHONE_NUMBER("PHONE_NUMBER ");

    private final String description;

    ExceptionEnums(String description){
        this.description = description;
    }

    public String getText(){
        return description;
    }
}
