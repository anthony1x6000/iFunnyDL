package com.anth1x.ifunnydl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private String initShare;
    private static final int REQUEST_CODE = 1;

    public int getNavigationBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
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
        TextView sendButton = findViewById(R.id.sendButton);
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
        int backgroundColor = Color.argb((int) (0.65 * 255), 86, 102, 162);
        sendButton.setBackgroundColor(backgroundColor);
        Objects.requireNonNull(getSupportActionBar()).hide();
////      Hide status bar
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        String currentText = footer.getText().toString();
        Date buildDate = new Date(BuildConfig.BUILD_TIME);
        long unixTime = buildDate.getTime() / 1000;
        String newText = currentText + " - Build Version: " + unixTime;
        footer.setText(newText);

//        Status and nav bar stuff
        int statusBarHeight = getStatusBarHeight();
        int navigationBarHeight = getNavigationBarHeight();

        ConstraintLayout layout = findViewById(R.id.appbody);
        layout.setPadding(0, statusBarHeight, 0, navigationBarHeight);

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