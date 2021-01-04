package com.thumann.tasksforme.request;

import com.thumann.tasksforme.helper.StringUtil;
import com.thumann.tasksforme.request.exceptions.ExecuteRequestException;
import com.thumann.tasksforme.request.exceptions.UpdateTokenException;
import com.thumann.tasksforme.request.requests.user.CreateUserRequest;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;

public abstract class AbstractRequest {

    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    private static final Logger LOGGER = Logger.getLogger(CreateUserRequest.class.getName());

    private static OkHttpClient client;

    public abstract Object execute() throws UpdateTokenException, ExecuteRequestException;

    protected abstract String getRequestName();

    protected JSONObject executeStandardRequest(Request.Builder requestBuilder) throws ExecuteRequestException, UpdateTokenException {

        client = initClient();
        addStandardHeaders(requestBuilder);
        Request request = requestBuilder.build();
        Response response;
        JSONObject responseJson = null;
        try {
            response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (body != null) {
                responseJson = new JSONObject(body.string());
            }
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            ExecuteRequestException ex = new ExecuteRequestException(e.getMessage(), e);
            ex.setRequestName(getRequestName());
            ex.setGeneralError(e.getMessage());
            throw new ExecuteRequestException(e.getMessage(), e);
        }
         catch (JSONException e) {
            throw new IllegalStateException("No Json returned"); //TODO convert to good exception
        }
        handleError( response, responseJson );
        return responseJson;
    }

    private void handleError( Response response, JSONObject responseJson) throws UpdateTokenException, ExecuteRequestException {
        String statusCode = String.valueOf(response.code() );
        if (statusCode.startsWith("2") ) {
            return;
        } else if ( statusCode.equals("401"))  {
            throw new UpdateTokenException(); //TODO: Genauer auf invalides Token überprüfen;
        }

        ExecuteRequestException ex = new ExecuteRequestException();
        ex.setRequestName(getRequestName());
        ex.setResponseStatus(statusCode);

        if (responseJson!= null) {
            String bodyRespString = responseJson.toString();
            if (!StringUtil.isEmpty(bodyRespString)) {
                ex.setResponseBody(bodyRespString);
            }
        }
        throw ex;
    }


    protected String getBaseUrl() {
        return "http://10.0.2.2:8080/springbootwildfly/rest/";
    }


    protected void addStandardHeaders(Request.Builder request) throws UpdateTokenException {
        if (StringUtil.isEmpty(RequestHelper.getTOKEN())) {
            throw new UpdateTokenException();
        }
        request.header("Authorization", "Bearer " + RequestHelper.getTOKEN());
    }



    private OkHttpClient initClient(){
        if (client == null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            Authenticator authenticator = new Authenticator() {
                @Override
                public Request authenticate(@Nullable Route route, @NotNull Response response) {
                    String credentials = Credentials.basic("test", "test");//TODO anpassen
                    return response.request().newBuilder().header("Authorization", credentials).build();
                }
            };
            builder.authenticator(authenticator);
            client = builder.build();
        }
        return client;
    }
}
