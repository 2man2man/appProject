package com.thumann.tasksforme.request.exceptions;

public class UpdateTokenException extends RequestException {
    
    public UpdateTokenException(String message, Throwable e) {
        super(message, e);
    }

    public UpdateTokenException() {
        this(null,null);
    }
}
