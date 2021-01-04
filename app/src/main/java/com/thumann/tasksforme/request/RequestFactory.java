package com.thumann.tasksforme.request;

import com.thumann.tasksforme.helper.StringUtil;
import com.thumann.tasksforme.request.requests.user.CheckUsernameAvailableRequest;
import com.thumann.tasksforme.request.requests.user.CreateUserRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestFactory {


    public CheckUsernameAvailableRequest  checkUsernameAvailableRequest (String userName){
        if (StringUtil.isEmpty(userName)){
            throw new IllegalArgumentException("Username is Empty");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", userName);
        } catch (JSONException e) {
            throw  new IllegalArgumentException("Username or password invalid"); // Should never happen
        }
        return new CheckUsernameAvailableRequest(jsonObject);
    }



    public CreateUserRequest createUserRequest (String userName,String password){
        if (StringUtil.isEmpty(password)){
            throw new IllegalArgumentException("Password is Empty");
        }
        else if (StringUtil.isEmpty(userName)){
            throw new IllegalArgumentException("Username is Empty");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", userName);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            throw  new IllegalArgumentException("Username or password invalid"); // Should never happen
        }
        return new CreateUserRequest(jsonObject);
    }



}
