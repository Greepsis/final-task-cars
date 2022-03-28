package com.senla.cars.serviceImpl.utils;


import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class Validator {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,16}$",
            Pattern.CASE_INSENSITIVE) ;
    private static final Pattern TELEPHONE_PATTERN = Pattern.compile("^((\\+?375)([0-9]{9}))$",
            Pattern.CASE_INSENSITIVE);

    public boolean validatePassword(final String password) {
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }

    public boolean validateTelephoneNumber(final String number) {
        Matcher matcher = TELEPHONE_PATTERN.matcher(number);
        return matcher.matches();
    }

}
