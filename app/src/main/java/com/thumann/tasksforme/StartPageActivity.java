package com.thumann.tasksforme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.thumann.tasksforme.createuser.CreateUserActivity;
import com.thumann.tasksforme.friends.AddFrirendsActivity;

public class StartPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
    }

    public void gotoAddFriendsActivity(View view) {
        Intent intent = new Intent(this, AddFrirendsActivity.class);
        startActivity(intent);
    }

}
