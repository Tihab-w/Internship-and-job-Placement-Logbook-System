package com.example.logbooksystem.utils;

import android.util.Patterns;
import java.util.regex.Pattern;

public class Validations {

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        // Accept formats: +1234567890, 1234567890, 123-456-7890
        return phone != null && Pattern.matches("^[+]?[0-9]{10,13}$", phone.replaceAll("[\\s\\-()]", ""));
    }

    public static boolean isValidPassword(String password) {
        // At least 8 chars, 1 uppercase, 1 lowercase, 1 number
        return password != null && password.length() >= 8 &&
                Pattern.compile("[A-Z]").matcher(password).find() &&
                Pattern.compile("[a-z]").matcher(password).find() &&
                Pattern.compile("[0-9]").matcher(password).find();
    }

    public static boolean isValidName(String name) {
        return name != null && name.trim().length() >= 2 && name.matches("[a-zA-Z\\s]+");
    }

    public static String sanitizeInput(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("[<>\"']", "");
    }
}