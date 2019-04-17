package com.example.bautifulnetworkdata;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.commonmark.node.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import ru.noties.markwon.Markwon;

public class NetActivity extends AppCompatActivity {

    TextView content;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);

        content = findViewById(R.id.content);
        progress = findViewById(R.id.progressBar);

        try_load();
    }

    protected void try_load() {
        try {
            AsyncTask<Void, Void, Spanned> task = new LoadMarkdownTask(new URL("https://raw.githubusercontent.com/noties/Markwon/master/README.md"));
            task.execute();
        } catch (MalformedURLException e) {
        }
    }

    private class LoadMarkdownTask extends AsyncTask<Void, Void, Spanned> {
        private URL url;
        final Markwon markwon;

        private LoadMarkdownTask(URL url) {
            this.url = url;
            this.markwon = Markwon.create(NetActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
            content.setText("");
        }

        @Override
        protected Spanned doInBackground(Void... params) {
            try {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                String newLine = System.getProperty("line.separator");
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine + newLine);
                }
                in.close();

                final Node node = markwon.parse(response.toString());
                return markwon.render(node);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Spanned result) {
            super.onPostExecute(result);
            if (result instanceof Exception) {
                new AlertDialog.Builder(NetActivity.this)
                        .setTitle("Error")
                        .setMessage(result.toString())
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setNeutralButton("Try again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NetActivity.this.try_load();
                            }
                        })
                        .create()
                        .show();
                return;
            }
            progress.setVisibility(View.INVISIBLE);
            markwon.setParsedMarkdown(content, result);
        }
    }
}
