package com.thumann.tasksforme.request.exceptions;

import androidx.annotation.Nullable;

import com.thumann.tasksforme.helper.StringUtil;

public class ExecuteRequestException extends RequestException {

    private String requestName;

    private String generealError;

    private String responseBody;

    private String responseStatus;

    public ExecuteRequestException(String message, Throwable e) {
        super(message, e);
    }

    public ExecuteRequestException() {
        this(null, null);
    }


    @Nullable
    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        if (!StringUtil.isEmpty(getRequestName())) {
            sb.append(getRequestName()).append(StringUtil.NEWLINE);
        }
        if (!StringUtil.isEmpty(getGenerealError())) {
            sb.append(getGenerealError()).append(StringUtil.NEWLINE);
        }

        if (!StringUtil.isEmpty(getResponseStatus())) {
            sb.append(getResponseStatus()).append(StringUtil.NEWLINE);
        }

        if (!StringUtil.isEmpty(getResponseBody())) {
            sb.append(getResponseBody()).append(StringUtil.NEWLINE);
        }

        String message = super.getMessage();
        if (!StringUtil.isEmpty(message)) {
            sb.append(message);
        }
        return sb.toString();
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getGenerealError() {
        return generealError;
    }

    public void setGeneralError(String generealError) {
        this.generealError = generealError;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }
}
