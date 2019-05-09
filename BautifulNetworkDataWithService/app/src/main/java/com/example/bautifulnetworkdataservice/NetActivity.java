package com.example.bautifulnetworkdataservice;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.noties.markwon.Markwon;

public class NetActivity extends AppCompatActivity {
    static int LOAD_MARKDOWN_JOB_ID = 1;
    static public final String URL_NUMBER_KEY = "urlNumber";

    protected TextView content;
    protected ProgressBar progress;

    public final static String PARAM_RESULT = "result";
    BroadcastReceiver br;

    public final static String BROADCAST_ACTION = "com.example.beautifulnetworkdataservice.baction";

    private Markwon markwon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);

        content = findViewById(R.id.content);
        progress = findViewById(R.id.progressBar);

        progress.setVisibility(View.VISIBLE);
        content.setText("");

        this.markwon = Markwon.create(this);
        reschedule(this, null);
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                String result = intent.getStringExtra(PARAM_RESULT);
                progress.setVisibility(View.INVISIBLE);
                markwon.setParsedMarkdown(content, markwon.render(markwon.parse(result)));
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        registerReceiver(br, intFilt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // дерегистрируем (выключаем) BroadcastReceiver
        unregisterReceiver(br);
    }

    static protected void reschedule(ContextWrapper cw, Integer urlNumber) {
        JobScheduler jobScheduler =
                (JobScheduler) cw.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder jobInfoBuilder = new JobInfo.Builder(LOAD_MARKDOWN_JOB_ID,
                new ComponentName(cw, PeriodicNetService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setBackoffCriteria(10 * 1000, JobInfo.BACKOFF_POLICY_LINEAR)
                .setMinimumLatency(5 * 1000)
                .setOverrideDeadline(10 * 1000);
        if (urlNumber != null) {
            PersistableBundle bundle = new PersistableBundle();
            bundle.putInt(URL_NUMBER_KEY, urlNumber);

            jobInfoBuilder.setExtras(bundle);
        }
        jobScheduler.schedule(jobInfoBuilder.build());
    }


}
