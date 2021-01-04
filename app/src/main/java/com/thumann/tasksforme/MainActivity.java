package com.thumann.tasksforme;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.thumann.tasksforme.createuser.CreateUserActivity;
import com.thumann.tasksforme.helper.LogHelper;
import com.thumann.tasksforme.helper.WidgetHelper;
import com.thumann.tasksforme.request.RequestHelper;
import com.thumann.tasksforme.request.exceptions.RequestException;
import com.thumann.tasksforme.request.requests.user.GetUserIdRequest;
import com.thumann.tasksforme.session.Session;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void createNewUser(View view) {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        LoginUserTask loginUserTask = new LoginUserTask();
        loginUserTask.execute((String) null); // TODO continue here
    }


    private class LoginUserTask extends AsyncTask<String, Integer, LOGIN_RESULT> {

        private ProgressDialog progDialog;

        private TextView userNameWidget;

        private TextView passwordWidget;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            progDialog = new ProgressDialog(MainActivity.this);
            progDialog.setMessage("Login...");
            progDialog.setIndeterminate(true);
            progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDialog.setCancelable(false);
            progDialog.show();


            userNameWidget = findViewById(R.id.LoginUserName);
            final String usernameString = WidgetHelper.getTextFromWidget(userNameWidget);
            Session.setLoggedInUserName(usernameString);

            passwordWidget = findViewById(R.id.LoginPassword);
            final String passwordString = WidgetHelper.getTextFromWidget(passwordWidget);
            Session.setLoggedInUserPassword(passwordString);
        }


        @Override
        protected void onPostExecute(LOGIN_RESULT result) {
            progDialog.dismiss();

            if (result == LOGIN_RESULT.SUCCESS) {
                Intent intent = new Intent(MainActivity.this, StartPageActivity.class);
                startActivity(intent);
            } else if (result == LOGIN_RESULT.USER_OR_PASSWORD_INVALID) {
                userNameWidget.setBackground(getResources().getDrawable(R.drawable.textredbackground));
                passwordWidget.setBackground(getResources().getDrawable(R.drawable.textredbackground));
            } else if (result == LOGIN_RESULT.NO_CONNECTION) {
                //TODO handle no Connection
            } else {
                throw new IllegalStateException();
            }
        }

        @Override
        protected LOGIN_RESULT doInBackground(String... strings) {
            GetUserIdRequest request = new GetUserIdRequest(Session.getLoggedInUserName());
            try {
                Long userId = Long.valueOf((String) RequestHelper.executeRequest(request));
                Session.setLoggedInUserId(userId);
                return LOGIN_RESULT.SUCCESS;
            } catch (RequestException e) {
                return LOGIN_RESULT.USER_OR_PASSWORD_INVALID;
            } catch (Exception e) {
                LogHelper.logError(this.getClass(), e);
                throw new IllegalStateException("Should never happen");
            }
        }
    }


    private enum LOGIN_RESULT {
        SUCCESS,
        NO_CONNECTION, // TODO: noConnection einbauen
        USER_OR_PASSWORD_INVALID;
    }

}
