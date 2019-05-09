package com.example.bautifulnetworkdataservice;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class LoadMarkdownTask extends AsyncTask<Void, Void, String> {
    private URL url;

    public LoadMarkdownTask(URL url) {
        this.url = url;
    }

    @Override
    protected String doInBackground(Void... params) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;

            String newLine = System.getProperty("line.separator");
            while ((inputLine = in.readLine()) != null) {
                if (this.isCancelled()) {
                    return null;
                }
                response.append(inputLine + newLine);
            }
            in.close();


            return response.toString();
        } catch (IOException e) {
            return null;
        }
    }
}