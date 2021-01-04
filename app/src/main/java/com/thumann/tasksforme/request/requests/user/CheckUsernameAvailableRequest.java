package com.thumann.tasksforme.request.requests.user;

import com.thumann.tasksforme.request.AbstractRequest;
import com.thumann.tasksforme.request.exceptions.ExecuteRequestException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckUsernameAvailableRequest extends AbstractRequest {

    private final String ENDPOINT = "signUp/usernameAvailable";

    private final Request request;

    public CheckUsernameAvailableRequest(JSONObject body) {
        StringBuilder url = new StringBuilder();
        url.append(getBaseUrl()).append(ENDPOINT);

        RequestBody requestBody = RequestBody.create( body.toString() , AbstractRequest.JSON);
        request = new Request.Builder().url(url.toString()).post(requestBody).build();
    }

    @Override
    public Boolean execute() throws ExecuteRequestException {
        OkHttpClient client = new OkHttpClient();
        Response response;
        try {
            response = client.newCall(request).execute();
            if (response.code() == 200){
                return true;
            }
            else {
                return false;
            }
        } catch (IOException e) {
            Logger logger = Logger.getLogger(CheckUsernameAvailableRequest.class.getName());
            logger.log(Level.SEVERE, e.getMessage(),e);
            throw new ExecuteRequestException(e.getMessage(), e);
        }
    }


    @Override
    protected String getRequestName() {
        return "Benutzername überprüfen";
    }

}
