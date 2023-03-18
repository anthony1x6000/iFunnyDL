package com.anth1x.ifunnydl;

import static com.anth1x.ifunnydl.fonts.*;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private String initShare;
    private static final int REQUEST_CODE = 1;

    private int getStatusBarHeight() {
        int result = 0;
        @SuppressLint({"InternalInsetResource", "DiscouragedApi"}) int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getNavigationBarHeight() {
        int result = 0;
        @SuppressLint({"InternalInsetResource", "DiscouragedApi"}) int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fonts.init(this);

        // Give permissions
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_CODE);
        }
        TextView gotosettings = findViewById(R.id.gotosettings);
        gotosettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

//      Elements
        TextView sendButton = findViewById(R.id.sendButton);
        TextView bigTitle = findViewById(R.id.titlescool);
        TextView footer = findViewById(R.id.footer);
        TextView explain = findViewById(R.id.explain);
        EditText inputURL = findViewById(R.id.inputURL);

//      Styling
        sendButton.setTypeface(fontButton);
        bigTitle.setTypeface(fontTitle);
        footer.setTypeface(fontBodyText);
        gotosettings.setTypeface(fontBodyText);
        explain.setTypeface(fontSubtitle);
        inputURL.setTypeface(fontInput);
        int sendButtBackgroundColor = Color.argb((int) (0.65 * 255), 86, 102, 162);
        sendButton.setBackgroundColor(sendButtBackgroundColor);
        Objects.requireNonNull(getSupportActionBar()).hide();

        String currentText = footer.getText().toString();
        Date buildDate = new Date(BuildConfig.BUILD_TIME);
        long unixTime = buildDate.getTime() / 1000;
        String newText = currentText + " - Build Version: " + unixTime;
        footer.setText(newText);

//        Status and nav bar stuff
        RelativeLayout layout = findViewById(R.id.baseRelLayout);
        int statusBarHeight = getStatusBarHeight();
        int navigationBarHeight = getNavigationBarHeight();
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