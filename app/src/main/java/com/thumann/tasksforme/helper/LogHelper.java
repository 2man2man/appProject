package com.thumann.tasksforme.helper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHelper {

    public static void logError(Class clazz, Throwable e) {
        logError(clazz, e.getMessage(), e);
    }

    public static void logError(Class clazz, String message, Throwable e) {
        Logger.getLogger(clazz.getName()).log(Level.SEVERE, message, e);
    }


}
