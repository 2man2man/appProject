package com.thumann.tasksforme.request.requests.user;

import com.thumann.tasksforme.helper.LogHelper;
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

public class CreateUserRequest extends AbstractRequest {

    private final String ENDPOINT = "signUp";

    private final Request request;

    public CreateUserRequest(JSONObject body) {
        StringBuilder url = new StringBuilder();
        url.append(getBaseUrl()).append(ENDPOINT);

        RequestBody requestBody = RequestBody.create( body.toString() , AbstractRequest.JSON);
        request = new Request.Builder().url(url.toString()).post(requestBody).build();
    }

    @Override
    public JSONObject execute() throws ExecuteRequestException {
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            int code = response.code();
            if (code == 409){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("error" , "Username already exits");
                return jsonObject;
            }
            JSONObject responseJson = new JSONObject(response.body().string());
            return responseJson;
        } catch (IOException e) {
            LogHelper.logError(this.getClass(), e);
            throw new ExecuteRequestException(e.getMessage(), e);
        } catch (JSONException e) {
            try {
                if (response != null && response.body() != null) {
                    LogHelper.logError(this.getClass(), response.body().string(), e);
                }
                else{
                    LogHelper.logError(this.getClass(), e);
                }
            } catch (IOException ex) {
                LogHelper.logError(this.getClass(), e);
            }
            throw new IllegalStateException("No Json returned"); //TODO convert to good exception
        }
    }


    @Override
    protected String getRequestName() {
        return "Benutzer anlegen";
    }

}
