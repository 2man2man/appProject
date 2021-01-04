package com.thumann.tasksforme.request.exceptions;

public class RequestException extends Exception {


    public RequestException(String message, Throwable e) {
        super(message, e);
    }
}
