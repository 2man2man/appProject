package com.thumann.tasksforme.helper;

public class StringUtil {

    public  static  final String NEWLINE = "\n";

    public static boolean isEmpty(String string) {
        if (string == null) {
            return true;
        } else {
            return string.trim().isEmpty();
        }
    }

    public static boolean isDifferent(String string1, String string2) {
        if (string1 == null && string2 == null) {
            return false;
        } else if (string1 == null && string2 != null) {
            return true;
        } else {
            return  !string1.equals(string2);
        }
    }


}
