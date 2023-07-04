package com.anth1x.ifunnydl;

import static com.anth1x.ifunnydl.fonts.fontBodyText;
import static com.anth1x.ifunnydl.fonts.fontButton;
import static com.anth1x.ifunnydl.fonts.fontInput;
import static com.anth1x.ifunnydl.fonts.fontSubtitle;
import static com.anth1x.ifunnydl.fonts.fontTitle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
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

        int firstBttnBG = Color.argb((int) (0.63 * 255), 17, 33, 59);
        int secondBttnBG = Color.argb((int) (0.45 * 255), 17, 33, 59);
        int inputBG = Color.argb((int) (0.45 * 255), 17, 33, 63);
        inputFileName.setBackgroundColor(inputBG);

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

        gotoother.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, DisplayHistoryActivity.class);
            startActivity(intent);
        });

//        Start of switches
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        Switch switchNotif = (Switch) findViewById(R.id.switchNotif);
        boolean savedStateNotif = sharedPref.getBoolean("DMNotif", true);
        switchNotif.setChecked(savedStateNotif);

        Switch switchLogs = (Switch) findViewById(R.id.switchLogs);
        boolean savedStateLogs = sharedPref.getBoolean("doLogging", true);
        switchLogs.setChecked(savedStateLogs);

        Switch switchImage = (Switch) findViewById(R.id.switchImage);
        boolean savedStateImage = sharedPref.getBoolean("imgAsiFunnyFormat", false);
        switchImage.setChecked(savedStateImage);

        switchNotif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor prefEditor = sharedPref.edit();
            prefEditor.putBoolean("DMNotif", isChecked);
            prefEditor.apply();
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

//        Status and nav bar stuff
        LinearLayout layout = findViewById(R.id.baseRelLayout);
        RelativeLayout mainFooter = findViewById(R.id.mainFooter);
        int statusBarHeight = getStatusBarHeight();
        int navigationBarHeight = getNavigationBarHeight();
        layout.setPadding(0, statusBarHeight, 0, 0);
        mainFooter.setPadding(0, 0, 0, navigationBarHeight);
    }
}