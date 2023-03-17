package com.anth1x.ifunnydl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
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
        TextView notifPref = findViewById(R.id.changeNotifPref);
        TextView sendButton = findViewById(R.id.sendButton);
        TextView titlescool = findViewById(R.id.titlescool);
        TextView explain = findViewById(R.id.explain);
        EditText inputFileName = findViewById(R.id.inputFileName);
        TextView gotoother = findViewById(R.id.gotocute);
        TextView footer = findViewById(R.id.footer);

        int sendButtBackgroundColor = Color.argb((int) (0.35 * 255), 86, 102, 162);
        int notifPrefBackgroundColor = Color.argb((int) (0.55 * 255), 86, 102, 162);

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
                    sendButton.setBackgroundColor(sendButtBackgroundColor);
                    sendButton.setText("Apply");
                }, 800);
            }
        });

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
        sendButton.setTypeface(fontPorter);
        notifPref.setTypeface(fontPorter);
        titlescool.setTypeface(fontTheBoldFont);
        explain.setTypeface(fontNexa);
        sendButton.setBackgroundColor(sendButtBackgroundColor);
        notifPref.setBackgroundColor(notifPrefBackgroundColor);
        Objects.requireNonNull(getSupportActionBar()).hide();


//        Status and nav bar stuff
        ConstraintLayout layout = findViewById(R.id.appbody);
        int statusBarHeight = getStatusBarHeight();
        int navigationBarHeight = getNavigationBarHeight();
        layout.setPadding(0, statusBarHeight, 0, navigationBarHeight);
    }
}