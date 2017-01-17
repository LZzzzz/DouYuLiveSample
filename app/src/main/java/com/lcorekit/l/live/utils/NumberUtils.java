package com.lcorekit.l.live.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by l on 17-1-6.
 */

public class NumberUtils {
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
