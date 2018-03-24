package com.codegene.femicodes.cscprojectadmin.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by femicodes on 2/15/2018.
 */

public class CommonUtils {


    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }



}
