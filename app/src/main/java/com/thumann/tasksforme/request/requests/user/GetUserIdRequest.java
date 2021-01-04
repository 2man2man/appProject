package com.thumann.tasksforme.request.requests.user;

import com.thumann.tasksforme.helper.LogHelper;
import com.thumann.tasksforme.request.AbstractRequest;
import com.thumann.tasksforme.request.exceptions.ExecuteRequestException;
import com.thumann.tasksforme.request.exceptions.UpdateTokenException;
import com.thumann.tasksforme.session.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetUserIdRequest extends AbstractRequest {

    private final String ENDPOINT = "user/getIdByUsername";

    private final Request.Builder request;

    public GetUserIdRequest(String username) {
        StringBuilder url = new StringBuilder();
        url.append(getBaseUrl()).append(ENDPOINT);
        HttpUrl.Builder builder = HttpUrl.parse(url.toString()).newBuilder();
        builder = builder.addQueryParameter("username", username);
        request = new Request.Builder().url(builder.build()).get();
    }

    @Override
    public String execute() throws ExecuteRequestException, UpdateTokenException {
        JSONObject responseJson = executeStandardRequest(request);
        try {
            return responseJson.getString("value");
        } catch (JSONException e) {
            throw new IllegalStateException("Should never happen");
        }
    }

    @Override
    protected String getRequestName() {
        return "Finde Benutzer Id über Benutzer Namen";
    }

}
