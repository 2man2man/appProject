package com.thumann.tasksforme.createuser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.thumann.tasksforme.MainActivity;
import com.thumann.tasksforme.R;
import com.thumann.tasksforme.helper.ColorHelper;
import com.thumann.tasksforme.helper.StringUtil;
import com.thumann.tasksforme.helper.WidgetHelper;
import com.thumann.tasksforme.request.RequestFactory;
import com.thumann.tasksforme.request.exceptions.ExecuteRequestException;
import com.thumann.tasksforme.request.requests.user.CheckUsernameAvailableRequest;
import com.thumann.tasksforme.request.requests.user.CreateUserRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateUserActivity extends AppCompatActivity {

    private final int ERROR_BACKGROUND_COLOR = ColorHelper.LIGHT_RED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        final EditText usernameWidget = (EditText) findViewById(R.id.CreateUserUsername);
        usernameWidget.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                final String usernameString = WidgetHelper.getTextFromWidget(usernameWidget);
                if (StringUtil.isEmpty(usernameString)) {
                    return;
                } else if (hasFocus) {
                    return;
                }
                CheckUserNameAvailableTask task = new CheckUserNameAvailableTask();
                task.execute(usernameString);
            }
        });
    }


    public void createUser(View view) {

        boolean allGood = true;

        EditText usernameWidget = findViewById(R.id.CreateUserUsername);
        final String usernameString = WidgetHelper.getTextFromWidget(usernameWidget);

        if (StringUtil.isEmpty(usernameString)) {
            usernameWidget.setBackground(getResources().getDrawable(R.drawable.textredbackground));
            allGood = false;
        }

        EditText userpasswordWidget = findViewById(R.id.CreateUserPassword);
        final String passwordString = WidgetHelper.getTextFromWidget(userpasswordWidget);

        if (StringUtil.isEmpty(passwordString)) {
            allGood = false;
            userpasswordWidget.setBackground(getResources().getDrawable(R.drawable.textredbackground));
        } else {
            userpasswordWidget.setBackground(getResources().getDrawable(R.drawable.textgreenbackground));
        }


        EditText userpasswordConfirmWidget = findViewById(R.id.CreateUserPasswordConfirm);
        final String passwordConfirmString = WidgetHelper.getTextFromWidget(userpasswordConfirmWidget);
        if (StringUtil.isDifferent(passwordConfirmString, passwordString) || StringUtil.isEmpty(passwordConfirmString)) {
            allGood = false;
            userpasswordConfirmWidget.setBackground(getResources().getDrawable(R.drawable.textredbackground));
        } else {
            userpasswordConfirmWidget.setBackground(getResources().getDrawable(R.drawable.textgreenbackground));
        }


        if (!allGood) {
            return;
        }

        CreateUserTask createUserTask = new CreateUserTask();
        createUserTask.execute(usernameString, passwordString);
    }


    private void backToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private class CreateUserTask extends AsyncTask<String, Integer, String> {

        private AlertDialog.Builder alertDialog;

        private ProgressDialog progDialog;

        private boolean userCreationSucces = false;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progDialog = new ProgressDialog(CreateUserActivity.this);
            progDialog.setMessage("User is created...");
            progDialog.setIndeterminate(true);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setCancelable(false);
            progDialog.show();

            alertDialog = new AlertDialog.Builder(CreateUserActivity.this);
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (userCreationSucces){
                        backToMainActivity();
                    }
                }
            });
        }

        @Override
        protected void onPostExecute(String s) {
            progDialog.dismiss();
            if (userCreationSucces) {
                alertDialog.setTitle("Success");
            } else {
                alertDialog.setTitle("Failed");
                EditText usernameWidget = findViewById(R.id.CreateUserUsername);
                usernameWidget.setBackground(getResources().getDrawable(R.drawable.textredbackground));
            }
            alertDialog.setMessage(s);
            alertDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestFactory requestFactory = new RequestFactory();
            CreateUserRequest userRequest = requestFactory.createUserRequest(strings[0], strings[1]);
            String result;
            try {
                JSONObject response = userRequest.execute();
                if (response.has("error")) {
                    String error = response.getString("error");
                    result = error;
                    userCreationSucces = false;
                } else {
                    String username = response.getString("username");
                    result = "User '" + username + "' was created";
                    userCreationSucces = true;
                }
            } catch (ExecuteRequestException | JSONException e) {
                result = "User could not be created";
                userCreationSucces = false;
            }
            return result;
        }
    }


    private class CheckUserNameAvailableTask extends AsyncTask<String, Integer, Boolean> {

        private boolean errorOccured = false;

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (errorOccured) {
                return;
            }
            EditText usernameWidget = findViewById(R.id.CreateUserUsername);
            if (aBoolean) {
                usernameWidget.setBackground(getResources().getDrawable(R.drawable.textgreenbackground));
            } else {
                usernameWidget.setBackground(getResources().getDrawable(R.drawable.textredbackground));
            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            RequestFactory requestFactory = new RequestFactory();
            CheckUsernameAvailableRequest checkRequest = requestFactory.checkUsernameAvailableRequest(strings[0]);
            String result;
            try {
                Boolean response = checkRequest.execute();
                return response;
            } catch (ExecuteRequestException e) {
                errorOccured = true;
                return false;
            }
        }
    }
}
