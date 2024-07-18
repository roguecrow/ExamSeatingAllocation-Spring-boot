package com.chainsys.examease.validator;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidation {

    private UserValidation() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean validateInt(int value) {
        return value > 0;
    }

    public static boolean validateString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean validateDate(Date value) {
        return value != null;
    }

    public static boolean validateGender(char gender) {
        return gender == 'M' || gender == 'F' || gender == 'O';
    }

    public static boolean validateAadharNumber(String aadharNumber) {
        return validateString(aadharNumber) && aadharNumber.length() == 12 && aadharNumber.matches("\\d+");
    }

    public static boolean validateEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validateFilledCapacity(int filledCapacity, int capacity) {
        return filledCapacity >= 0 && filledCapacity <= capacity;
    }
}
