package com.thumann.tasksforme.session;

public class Session {

    private static String loggedInUserName;

    private static  String loggedInUserPassword;

    private  static  long loggedInUserId;

    public static String getLoggedInUserName() {
        return loggedInUserName;
    }

    public static void setLoggedInUserName(String loggedInUserName) {
        Session.loggedInUserName = loggedInUserName;
    }

    public static String getLoggedInUserPassword() {
        return loggedInUserPassword;
    }

    public static void setLoggedInUserPassword(String loggedInUserPassword) {
        Session.loggedInUserPassword = loggedInUserPassword;
    }

    public static long getLoggedInUserId() {
        return loggedInUserId;
    }

    public static void setLoggedInUserId(long loggedInUserId) {
        Session.loggedInUserId = loggedInUserId;
    }
}
