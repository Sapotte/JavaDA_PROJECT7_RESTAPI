package com.nnk.springboot.utils;

import org.springframework.stereotype.Service;

@Service
public class RegexValidation {
    public static void isNumeric(String str) {
        if (!str.matches("-?\\d+(\\.\\d+)?")) {
            throw new IllegalArgumentException("Invalid number: " + str);
        }
    }

    public static void checkPassword(String password) {
        if (!password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new IllegalArgumentException("Password must have at least 8 characters, one capital letter, one number and one symbol");
        }
    }
}
