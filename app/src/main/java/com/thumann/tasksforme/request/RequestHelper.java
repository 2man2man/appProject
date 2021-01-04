package com.thumann.tasksforme.request;

import com.thumann.tasksforme.request.exceptions.RequestException;
import com.thumann.tasksforme.request.exceptions.UpdateTokenException;

public class RequestHelper {

    private static final  Object synchronizeObj = new Object();

    private static String TOKEN;

    public static Object executeRequest(AbstractRequest request) throws RequestException {
        try {
            return request.execute();
        } catch (UpdateTokenException e) {
            updateToken();
            return request.execute();
        }
    }

    private static  void updateToken() throws UpdateTokenException {
        synchronized (synchronizeObj) {
            try {
                UpdateTokenRequest updateTokenRequest = new UpdateTokenRequest();
                TOKEN = updateTokenRequest.execute();
            } catch (Exception e) {
                throw new UpdateTokenException(e.getMessage(), e);
            }
        }
    }


    public static String getTOKEN(){
        synchronized(synchronizeObj){
            return TOKEN;
        }
    }
}
