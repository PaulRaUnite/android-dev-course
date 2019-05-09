package com.example.bautifulnetworkdataservice;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.PersistableBundle;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.bautifulnetworkdataservice.NetActivity.URL_NUMBER_KEY;

public class PeriodicNetService extends JobService {

    private LoadMarkdownTask downloadTask = null;
    private int urlNumber = 0;
    private ArrayList<URL> urls = new ArrayList<>();

    public PeriodicNetService() {
        super();
        try {
            urls.add(new URL("https://raw.githubusercontent.com/noties/Markwon/master/README.md"));
            urls.add(new URL("https://raw.githubusercontent.com/PaulRaUnite/network-simulation/master/readme_ua.md"));
        } catch (MalformedURLException e) {
        }
    }

    @Override
    public boolean onStartJob(final JobParameters params) {
        PersistableBundle extras = params.getExtras();
        if (extras.containsKey(URL_NUMBER_KEY)) {
            this.urlNumber = extras.getInt(URL_NUMBER_KEY);
        }
        Log.i("PeriodicNetService", Integer.toString(this.urlNumber));

        downloadTask = new LoadMarkdownTask(urls.get(PeriodicNetService.this.urlNumber)) {
            @Override
            protected void onPostExecute(String result) {
                if (result == null) {
                    jobFinished(params, true);
                    return;
                }
                Intent intent = new Intent(NetActivity.BROADCAST_ACTION);
                intent.putExtra(NetActivity.PARAM_RESULT, result);
                sendBroadcast(intent);
                NetActivity.reschedule(PeriodicNetService.this, urlNumber);
                jobFinished(params, true);
            }
        };

        this.urlNumber += 1;
        if (urls.size() == this.urlNumber) {
            this.urlNumber = 0;
        }
        downloadTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        downloadTask.cancel(true);
        return false;
    }
}
