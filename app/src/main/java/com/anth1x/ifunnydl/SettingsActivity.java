package com.anth1x.ifunnydl;

import static com.anth1x.ifunnydl.fonts.fontAltText;
import static com.anth1x.ifunnydl.fonts.fontBodyText;
import static com.anth1x.ifunnydl.fonts.fontButton;
import static com.anth1x.ifunnydl.fonts.fontInput;
import static com.anth1x.ifunnydl.fonts.fontSubtitle;
import static com.anth1x.ifunnydl.fonts.fontTitle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
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
        sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);

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
//        sharedPrefPrivate = getPreferences(Context.MODE_PRIVATE);
        Switch switchNotif = findViewById(R.id.switchNotif);
        Switch switchLogs = findViewById(R.id.switchLogs);
        Switch switchImage = findViewById(R.id.switchImage);

        switchNotif.setChecked(sharedPref.getBoolean("DMNotif", true));
        switchLogs.setChecked(sharedPref.getBoolean("doLogging", true));
        switchImage.setChecked(sharedPref.getBoolean("imgAsiFunnyFormat", false));

        switchNotif.setOnCheckedChangeListener(new SwitchStateChangeListener("DMNotif"));
        switchLogs.setOnCheckedChangeListener(new SwitchStateChangeListener("doLogging"));
        switchImage.setOnCheckedChangeListener(new SwitchStateChangeListener("imgAsiFunnyFormat"));

//        !!Styling!!
        LinearLayout centerButtons = findViewById(R.id.centerButtons);
        setFont(centerButtons, fontInput);

        sendButton.setTypeface(fontButton);
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
    private void setFont(ViewGroup viewGroup, Typeface typeface) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTypeface(typeface);
            } else if (child instanceof ViewGroup) {
                setFont((ViewGroup) child, typeface);
            }
        }
    }
    private class SwitchStateChangeListener implements CompoundButton.OnCheckedChangeListener {
        private String key;
        public SwitchStateChangeListener(String key) {
            this.key = key;
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences.Editor prefEditor = sharedPref.edit();
            prefEditor.putBoolean(key, isChecked);
            prefEditor.apply();
        }
    }
}