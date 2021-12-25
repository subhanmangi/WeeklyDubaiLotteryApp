package com.example.fridaydubailottery.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Extension {
    public static void showToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isEmailValid(String email, Context context) {
        if (email.isEmpty()) {
            showToast("Please enter a valid email", context);
            return false;
        }
        if (email.contains(" ")) {
            showToast("Email cannot contain any space", context);
            return false;
        }
        if (email.startsWith(" ") || email.endsWith(" ") || email.length() < 3 || !validateEmail(email, context)) {
            showToast("Invalid email", context);
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String data, Context context) {
        if (data.isEmpty()) {
            showToast("Please Fill the all fields", context);
            return false;
        }

        return true;
    }

    public static boolean isPasswordValid(String password, Context context) {
        if (password.isEmpty()) {
            showToast("Please enter your password", context);
            return false;
        }
        if (password.contains(" ")) {
            showToast("Password cannot contain any space", context);
            return false;
        }
        if (password.length() < 8) {
            showToast("Password must be 8 characters long", context);
            return false;
        }
        return true;
    }


    private static boolean validateEmail(String email, Context context) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }
}
