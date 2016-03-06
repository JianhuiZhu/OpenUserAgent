package com.jianhui_zhu.openuseragent.util;

/**
 * Created by jianhuizhu on 2016-03-06.
 */
public final class DataValidationUtil {
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
