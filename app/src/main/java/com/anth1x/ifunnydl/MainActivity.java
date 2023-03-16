package com.anth1x.ifunnydl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private String initShare;
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // GIVE ME PERMISSIONS
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        }

//        !!Styling!!
//        Elements
        Button sendButton = findViewById(R.id.sendButton);
        TextView titlescool = findViewById(R.id.titlescool);
        TextView footer = findViewById(R.id.footer);
        TextView explain = findViewById(R.id.explain);

//        Fonts
        Typeface fontPorter = Typeface.createFromAsset(getAssets(), "fonts/Porter.ttf");
        Typeface fontNexa = Typeface.createFromAsset(getAssets(), "fonts/nexaheavy.ttf");
        Typeface fontBebas = Typeface.createFromAsset(getAssets(), "fonts/bebas.ttf");
        Typeface fontTheBoldFont = Typeface.createFromAsset(getAssets(), "fonts/tboldfont.ttf");

//        Further Styling
        sendButton.setTypeface(fontPorter);
        titlescool.setTypeface(fontTheBoldFont);
        footer.setTypeface(fontBebas);
        explain.setTypeface(fontNexa);

        Objects.requireNonNull(getSupportActionBar()).hide();

//      Opened intent
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            System.out.println("started for share");
            if ("text/plain".equals(type)) {
                // Get the shared text data
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    System.out.println("shared text = " + sharedText);

                    // start download service here with sharedText
                    startDLService(sharedText);
                    finishAndRemoveTask();
                }
            }
        } else {
            System.out.println("Doing buttons");
//            Button sendButton = findViewById(R.id.sendButton);
            EditText inputURL = findViewById(R.id.inputURL);

            sendButton.setOnClickListener(view -> {
                initShare = inputURL.getText().toString();
                if (!initShare.isEmpty() && initShare.contains("ifunny")) {
                    System.out.println("Send initshare = " + initShare);
                    startDLService(initShare);
                    finishAndRemoveTask();
               }
            });
        }
    }

    private void startDLService(String initShare) {
        System.out.println("Called startService");
        Intent intentSendOver = new Intent(this, DownloadService.class);
        intentSendOver.putExtra("initShare", initShare);
        startService(intentSendOver);
    }
}