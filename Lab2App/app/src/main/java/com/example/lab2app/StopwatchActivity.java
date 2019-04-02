package com.example.lab2app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StopwatchActivity extends AppCompatActivity {
    TextView clock_field;

    Button control_button, reset;
    boolean running;

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;

    Handler handler;

    int Seconds, Minutes, MilliSeconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        clock_field = findViewById(R.id.clock_field);
        control_button = findViewById(R.id.start);
        reset = findViewById(R.id.reset);
        handler = new Handler();
        running = false;
    }

    public void switchTimer(View view) {
        if (running) {
            TimeBuff += MillisecondTime;
            handler.removeCallbacks(runnable);
            control_button.setText(R.string._continue);
        } else {
            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
            control_button.setText(R.string.stop);
        }
        running = !running;
        reset.setEnabled(true);
    }

    public void resetTimer(View view) {
        reset.setEnabled(false);
        if (!running) {
            control_button.setText(R.string.start);
        }

        MillisecondTime = 0;
        StartTime = SystemClock.uptimeMillis();
        TimeBuff = 0;
        UpdateTime = 0;
        Seconds = 0;
        Minutes = 0;
        MilliSeconds = 0;

        clock_field.setText(R.string.time_constant);
    }

    protected Runnable runnable = new Runnable() {
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);

            clock_field.setText(String.format("%d:%02d:%03d", Minutes, Seconds, MilliSeconds));
            handler.postDelayed(this, 0);
        }
    };

}