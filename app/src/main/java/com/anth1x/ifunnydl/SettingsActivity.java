package com.anth1x.ifunnydl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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


//        Elements
        TextView sendButton = findViewById(R.id.sendButton);
        TextView titlescool = findViewById(R.id.titlescool);
        TextView explain = findViewById(R.id.explain);
        EditText inputFileName = findViewById(R.id.inputFileName);
        TextView gotoother = findViewById(R.id.gotocute);
        TextView footer = findViewById(R.id.footer);

        int firstBttnBG = Color.argb((int) (0.35 * 255), 86, 102, 162);
        int secondBttnBG = Color.argb((int) (0.45 * 255), 86, 102, 162);

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
            notifPref.setText("Press to disable download notifications");
        } else {
            notifPref.setText("Press to enable download notifications");
        }
        notifPref.setOnClickListener(v -> {
            currentDMNotifValue.set(!currentDMNotifValue.get());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("DMNotif", currentDMNotifValue.get());
            editor.apply();

            if (currentDMNotifValue.get()) {
                notifPref.setText("Press to disable download notifications");
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

        gotoother.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, feelingLikeActivity.class);
            startActivity(intent);
        });

//        !!Styling!!
//        Fonts
        Typeface fontPorter = Typeface.createFromAsset(getAssets(), "fonts/Porter.ttf");
        Typeface fontNexa = Typeface.createFromAsset(getAssets(), "fonts/nexaheavy.ttf");
        Typeface fontBebas = Typeface.createFromAsset(getAssets(), "fonts/bebas.ttf");
        Typeface fontTheBoldFont = Typeface.createFromAsset(getAssets(), "fonts/tboldfont.ttf");

//        Further Styling
        gotoother.setTypeface(fontBebas);
        footer.setTypeface(fontBebas);
        TextView imageFormatNote = findViewById(R.id.imageFormatNote);
        imageFormatNote.setTypeface(fontBebas);

        titlescool.setTypeface(fontTheBoldFont);
        explain.setTypeface(fontNexa);

        Objects.requireNonNull(getSupportActionBar()).hide(); // hide header with app title

        TextView[] buttons = {sendButton, notifPref, prefImgFormattingID};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setTypeface(fontPorter);
            if (i % 2 == 0) {
                buttons[i].setBackgroundColor(firstBttnBG);
            } else {
                buttons[i].setBackgroundColor(secondBttnBG);
            }
        }

//        Status and nav bar stuff
        RelativeLayout layout = findViewById(R.id.baseRelLayout);
        int statusBarHeight = getStatusBarHeight();
        int navigationBarHeight = getNavigationBarHeight();
        layout.setPadding(0, statusBarHeight, 0, navigationBarHeight);
    }
}