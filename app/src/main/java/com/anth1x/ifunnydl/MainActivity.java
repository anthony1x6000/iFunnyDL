package com.anth1x.ifunnydl;

import static com.anth1x.ifunnydl.fonts.*;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
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
// ey bro dont make fun of ~~my~~ code. 50% is AI generated, 29% is stackoverflow,
// 10% is "heavily referenced and inspired" from other github repos (https://github.com/vidovichb/iFunny_Downloader, https://github.com/switchswap/iFunny-Cropper),
// 5% from russian forums, another 5% from chinese forums, and the final 1% is incoherent nonsense that I made.
    private String initShare;
    private static final int requestCode = 1;

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

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    // Call this method from onCreate() or onResume() of your activity


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
                    requestCode);
        }
        verifyStoragePermissions(this);

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

            sendButton.setOnClickListener(view -> {
                initShare = inputURL.getText().toString();
                if (initShare.contains("g0")) {
                    System.out.println("g0 - Bypass download, do crop");
                    Intent cropIntent = new Intent(this, CropService.class);
                    cropIntent.putExtra("input_path", Environment.DIRECTORY_PICTURES);
                    startService(cropIntent);
                } else if (!initShare.isEmpty() && initShare.contains("ifunny")) {
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