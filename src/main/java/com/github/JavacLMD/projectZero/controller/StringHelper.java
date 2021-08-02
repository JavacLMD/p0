package com.github.JavacLMD.projectZero.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper {

    public static boolean isEmail(String text) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (text == null)
            return false;
        return pat.matcher(text).matches();
    }

    public static boolean isInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
