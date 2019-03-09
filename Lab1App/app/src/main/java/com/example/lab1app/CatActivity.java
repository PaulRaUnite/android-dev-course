package com.example.lab1app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class CatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);
    }

    public void like_cat(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Purr";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void next(View view) {
        Intent k = new Intent(CatActivity.this, UIHellActivity.class);
        startActivity(k);
    }
}
