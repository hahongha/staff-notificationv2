package com.istt.staff_notification_v2.service;

import java.util.regex.Pattern;

public class EmailValidation {

    private static final String DEFAULT_DOMAIN = "gmail.com";
    private static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    public static boolean patternMatches(String emailAddress) {
        return Pattern.compile(EMAIL_REGEX).matcher(emailAddress).matches();
    }

    public static String addDefaultDomainIfNeeded(String emailAddress) {
        if (!emailAddress.contains("@")) {
            emailAddress += "@" + DEFAULT_DOMAIN;
        }
        return emailAddress;
    }
}

