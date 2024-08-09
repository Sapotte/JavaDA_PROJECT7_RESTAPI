package com.nnk.springboot.utils;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class RegexValidation {

    /**
     * Adds an error to the given BindingResult if the password is not valid.
     *
     * @param result   the BindingResult object to add the error to
     * @param password the password to validate
     * @return the updated BindingResult object
     */
    public static BindingResult addErrorIfPasswordNotValid(BindingResult result, String password) {
        if(isInvalidPassword(password)) {
            FieldError error = new FieldError("user", "password", "Password must be at least 8 characters long, and include an uppercase letter, a lowercase letter, a number, and a special character");
            result.addError(error);
        }
        return result;
    }

    /**
     * Checks if the given password is invalid.
     *
     * @param password the password to validate
     * @return true if the password is invalid, false otherwise
     */
    private static boolean isInvalidPassword(String password) {
        return !password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$");
    }
}
