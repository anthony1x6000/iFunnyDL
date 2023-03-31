package com.anth1x.ifunnydl;

import static com.anth1x.ifunnydl.fonts.fontBodyText;
import static com.anth1x.ifunnydl.fonts.fontButton;
import static com.anth1x.ifunnydl.fonts.fontInput;
import static com.anth1x.ifunnydl.fonts.fontSubtitle;
import static com.anth1x.ifunnydl.fonts.fontTitle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class SettingsActivity extends AppCompatActivity {
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

    public boolean isValidFileName(String fileName) {
        String regex = "^[\\w\\-. ]+$";
        return fileName.matches(regex);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        fonts.init(this);

//      Elements
        TextView sendButton = findViewById(R.id.sendButton);
        TextView titlescool = findViewById(R.id.titlescool);
        TextView explain = findViewById(R.id.explain);
        EditText inputFileName = findViewById(R.id.inputFileName);
        TextView gotoother = findViewById(R.id.gotocute);
        TextView footer = findViewById(R.id.footer);

        int firstBttnBG = Color.argb((int) (0.45 * 255), 86, 102, 162);
        int secondBttnBG = Color.argb((int) (0.35 * 255), 86, 102, 162);

        sendButton.setOnClickListener(v -> {
            String fileName = inputFileName.getText().toString();
            if (isValidFileName(fileName)) {
                SharedPreferences sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                System.out.println("Customized file name = " + fileName);
                editor.putString("fileName", fileName);
                editor.apply();
            } else {
                sendButton.setBackgroundColor(Color.RED);
                sendButton.setText("INVALID FILE NAME");
                new Handler().postDelayed(() -> {
                    sendButton.setBackgroundColor(firstBttnBG);
                    sendButton.setText("Apply");
                }, 800);
            }
        });

        TextView notifPref = findViewById(R.id.changeNotifPref);
        SharedPreferences sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);
        AtomicBoolean currentDMNotifValue = new AtomicBoolean(sharedPref.getBoolean("DMNotif", true));
        if (currentDMNotifValue.get()) {
            notifPref.setText("Press to disable download notifications (kind of recommended)");
        } else {
            notifPref.setText("Press to enable download notifications");
        }
        notifPref.setOnClickListener(v -> {
            currentDMNotifValue.set(!currentDMNotifValue.get());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("DMNotif", currentDMNotifValue.get());
            editor.apply();

            if (currentDMNotifValue.get()) {
                notifPref.setText("Press to disable download notifications (kind of recommended)");
            } else {
                notifPref.setText("Press to enable download notifications");
            }
        });

        TextView prefImgFormattingID = findViewById(R.id.changeImageFormatting);
        AtomicBoolean prefImgiFunnyFormat = new AtomicBoolean(sharedPref.getBoolean("imgAsiFunnyFormat", true));
        if (prefImgiFunnyFormat.get()) {
            prefImgFormattingID.setText("Press to use video file naming format for images (UNIXTIME)");
        } else {
            prefImgFormattingID.setText("Press to use iFunny image file naming format for images (random text)");
        }
        prefImgFormattingID.setOnClickListener(v -> {
            prefImgiFunnyFormat.set(!prefImgiFunnyFormat.get());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("imgAsiFunnyFormat", prefImgiFunnyFormat.get());
            editor.apply();

            if (prefImgiFunnyFormat.get()) {
                prefImgFormattingID.setText("Press to use video file naming format for images (UNIXTIME)");
            } else {
                prefImgFormattingID.setText("Press to use iFunny image file naming format for images (random text)");
            }
        });

        TextView prefLogsID = findViewById(R.id.changeLogPref);
        AtomicBoolean doLoggingPref = new AtomicBoolean(sharedPref.getBoolean("doLogging", true));
        if (doLoggingPref.get()) {
            prefLogsID.setText("Press to disable download logs");
        } else {
            prefLogsID.setText("Press to enable download logs");
        }
        prefLogsID.setOnClickListener(v -> {
            doLoggingPref.set(!doLoggingPref.get());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("doLogging", doLoggingPref.get());
            editor.apply();

            if (doLoggingPref.get()) {
                prefLogsID.setText("Press to disable download logs");
            } else {
                prefLogsID.setText("Press to enable download logs");
            }
        });

        gotoother.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, DisplayHistoryActivity.class);
            startActivity(intent);
        });


//        !!Styling!!
        gotoother.setTypeface(fontBodyText);
        footer.setTypeface(fontBodyText);
        TextView imageFormatNote = findViewById(R.id.imageFormatNote);
        imageFormatNote.setTypeface(fontBodyText);
        inputFileName.setTypeface(fontInput);

        titlescool.setTypeface(fontTitle);
        explain.setTypeface(fontSubtitle);

        Objects.requireNonNull(getSupportActionBar()).hide(); // hide header with app title

        TextView[] buttons = {sendButton, notifPref, prefLogsID, prefImgFormattingID};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setTypeface(fontButton);
            if (i % 2 == 0) {
                buttons[i].setBackgroundColor(firstBttnBG);
            } else {
                buttons[i].setBackgroundColor(secondBttnBG);
            }
        }

//        Status and nav bar stuff
        LinearLayout layout = findViewById(R.id.baseRelLayout);
        RelativeLayout mainFooter = findViewById(R.id.mainFooter);
        int statusBarHeight = getStatusBarHeight();
        int navigationBarHeight = getNavigationBarHeight();
        layout.setPadding(0, statusBarHeight, 0, 0);
        mainFooter.setPadding(0, 0, 0, navigationBarHeight);
    }
}