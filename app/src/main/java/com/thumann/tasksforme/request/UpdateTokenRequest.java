package com.thumann.tasksforme.request;


import com.thumann.tasksforme.helper.LogHelper;
import com.thumann.tasksforme.request.exceptions.ExecuteRequestException;
import com.thumann.tasksforme.request.requests.user.CreateUserRequest;
import com.thumann.tasksforme.session.Session;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

public class UpdateTokenRequest extends AbstractRequest {

    private static OkHttpClient client;

    private final String url = "http://10.0.2.2:8080/springbootwildfly/oauth/token";

    private final Request request;

    public UpdateTokenRequest() {

        client = initClient();

        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        builder = builder.addQueryParameter("grant_type", "password")
                /**
                 .addQueryParameter("username" , "test") //TODO: Benutzer abhängig
                 .addQueryParameter("password" , "test"); //TODO: Benutzer abhängig
                 **/
                .addQueryParameter("username", Session.getLoggedInUserName())
              //  .addQueryParameter("password", Session.getLoggedInUserPassword());
                .addQueryParameter("password", "test");
        RequestBody requestBody = RequestBody.create(new byte[0]);
        request = new Request.Builder()
                .url(builder.build())
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
    }

    @Override
    public String execute() throws ExecuteRequestException {

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            LogHelper.logError(this.getClass(),e);
            throw new ExecuteRequestException(e.getMessage(), e);
        }
        try {
            JSONObject responseJson = new JSONObject(response.body().string());
            return responseJson.getString("access_token");
        } catch (Exception e) {
            LogHelper.logError(this.getClass(), e);
            throw new IllegalStateException("No Json returned"); //TODO convert to good exception
        }
    }


    private OkHttpClient initClient() {
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            Authenticator authenticator = new Authenticator() {
                @Override
                public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
                    String credentials = Credentials.basic("tutorialspoint", "{noop}secret");//TODO anpassen
                    return response.request().newBuilder().header("Authorization", credentials).build();
                }
            };
            builder.authenticator(authenticator);
            client = builder.build();
        }
        return client;
    }


    @Override
    protected String getRequestName() {
        return "Token-Aktualisierung";
    }
}
