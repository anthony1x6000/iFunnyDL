package com.anth1x.ifunnydl;

import static com.anth1x.ifunnydl.fonts.fontBodyText;
import static com.anth1x.ifunnydl.fonts.fontButton;
import static com.anth1x.ifunnydl.fonts.fontInput;
import static com.anth1x.ifunnydl.fonts.fontSubtitle;
import static com.anth1x.ifunnydl.fonts.fontTitle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

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
    private static final String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public boolean checkPowerSaving() {
        boolean finalDecision = true;

        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isPowerSaveMode = powerManager.isPowerSaveMode();
        System.out.println("isPowerSaveMode = " + isPowerSaveMode);

        boolean isIgnoringBatteryOptimizations = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        boolean isUnrestricted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            isUnrestricted = !activityManager.isBackgroundRestricted();
        }
        System.out.println("isIgnoringBatteryOptimizations = " + isIgnoringBatteryOptimizations + " isUnrestricted = " + isUnrestricted);

        if (!isIgnoringBatteryOptimizations || !isUnrestricted) {
            if (isPowerSaveMode) {
                System.out.println("In power savings. Complaining");

                SharedPreferences sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);
                boolean showBatteryMessage = sharedPref.getBoolean("showBatteryMessage", true);

                finalDecision = true;

                if (showBatteryMessage) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("App doesn't work with Battery Saver enabled. Please disable or set app Battery Optimization to Unrestricted (recommended) or Optimized (sometimes breaks). Please see the GitHub repo for more details.")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> dialog.dismiss())
                            .setNegativeButton("Never show again", (dialog, id) -> {
                                new AlertDialog.Builder(this)
                                        .setMessage("Are you sure you sure you never want to see this dialog again?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", (dialog1, id1) -> {
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putBoolean("showBatteryMessage", false);
                                            editor.apply();
                                            dialog1.dismiss();
                                        })
                                        .setNegativeButton("No", (dialog1, id1) -> dialog.dismiss())
                                        .show();
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        } else {
            finalDecision = false;
        }
        return finalDecision;
    }


    public void startIntent(TextView sendButton, EditText inputURL, urlHistory history, Intent intent, String action, String type) {
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            System.out.println("Shared a link");
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    System.out.println("shared text = " + sharedText);
                    history.addURL(sharedText);

                    startDLService(sharedText);
                    finishAndRemoveTask();
                }
            }
        } else {
            System.out.println("Doing buttons");
            sendButton.setOnClickListener(view -> {
                initShare = inputURL.getText().toString();
                if (!initShare.isEmpty() && initShare.contains("ifunny.co")) {
                    System.out.println("Send initshare = " + initShare);
                    history.addURL(initShare);

                    startDLService(initShare);
                    // finishAndRemoveTask();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Created!!!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fonts.init(this);
        urlHistory history = new urlHistory(this);

//        Give Permissions
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), requestCode);
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
        int sendBG = Color.argb((int) (0.45 * 255), 45, 50, 80);
        int inputBG = Color.argb((int) (0.30 * 255), 45, 50, 80);
        sendButton.setBackgroundColor(sendBG);
        inputURL.setBackgroundColor(inputBG);
        Objects.requireNonNull(getSupportActionBar()).hide();

        String currentText = footer.getText().toString();
        Date buildDate = new Date(BuildConfig.BUILD_TIME);
        long unixTime = buildDate.getTime() / 1000;
        String newText = currentText + " - Build Version: " + unixTime;
        footer.setText(newText);

//        Status and nav bar stuff
        LinearLayout layout = findViewById(R.id.baseRelLayout);
        int statusBarHeight = getStatusBarHeight();
        int navigationBarHeight = getNavigationBarHeight();
        layout.setPadding(0, statusBarHeight, 0, navigationBarHeight);
        if (checkPowerSaving() == false) {
            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();
            if (Intent.ACTION_SEND.equals(action) && type != null) {
                System.out.println("it is!");
                finishAffinity();
            }
            startIntent(sendButton, inputURL, history, intent, action, type);
        }
    }

    private void startDLService(String initShare) {
        System.out.println("Calling Downloadservice...");
        Intent intentSendOver = new Intent(this, DownloadService.class);
        intentSendOver.putExtra("initShare", initShare);
        startService(intentSendOver);
    }

}