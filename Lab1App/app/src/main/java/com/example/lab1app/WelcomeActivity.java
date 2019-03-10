package com.example.lab1app;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void jump_to_cat(View view) {
        Intent k = new Intent(WelcomeActivity.this, CatActivity.class);
        startActivity(k);
    }

    public void jump_to_other(View view) {
        Intent k = new Intent(WelcomeActivity.this, UIHellActivity.class);
        startActivity(k);
    }
}
