package com.anth1x.ifunnydl;

import static com.anth1x.ifunnydl.fonts.fontBodyText;
import static com.anth1x.ifunnydl.fonts.fontButton;
import static com.anth1x.ifunnydl.fonts.fontSubtitle;
import static com.anth1x.ifunnydl.fonts.fontTitle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class DisplayHistoryActivity extends AppCompatActivity {

    private TableLayout urlTable;
    private TableLayout timeTable;

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
        setContentView(R.layout.activity_display_history);

        urlHistory history = new urlHistory(this);

        Objects.requireNonNull(getSupportActionBar()).hide();

        urlTable = findViewById(R.id.url_table);
        timeTable = findViewById(R.id.time_table);

        initTables();

        // Styling
        int buttonBG = Color.argb((int) (0.63 * 255), 17, 33, 59);

        TextView sendButton = findViewById(R.id.sendButton);
        TextView bigTitle = findViewById(R.id.titlescool);
        TextView footer = findViewById(R.id.footer);
        TextView explain = findViewById(R.id.explain);
        TextView tableTitle = findViewById(R.id.tableTitle);
        TextView tableTitleTimes = findViewById(R.id.tableTitleTimes);
        TextView gotoother = findViewById(R.id.gotocute);

        bigTitle.setTypeface(fontTitle);
        footer.setTypeface(fontBodyText);
        explain.setTypeface(fontSubtitle);  tableTitle.setTypeface(fontSubtitle);   tableTitleTimes.setTypeface(fontSubtitle);
        sendButton.setTypeface(fontButton);

        sendButton.setBackgroundColor(buttonBG);

        sendButton.setOnClickListener(v -> {
            history.clearHistory();
            initTables();
        });

        gotoother.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayHistoryActivity.this, feelingLikeActivity.class);
            startActivity(intent);
        });

        //        Status and nav bar stuff
        LinearLayout layout = findViewById(R.id.baseRelLayout);
        LinearLayout mainFooter = findViewById(R.id.mainFooter);
        int statusBarHeight = getStatusBarHeight();
        int navigationBarHeight = getNavigationBarHeight();
        layout.setPadding(0, statusBarHeight, 0, 0);
        mainFooter.setPadding(0, 0, 0, navigationBarHeight);

        updateHeight();
    }

    public void updateHeight() {
        final View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                RelativeLayout header = findViewById(R.id.header);
                LinearLayout centerButtons = findViewById(R.id.centerButtons);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                int screenHeight = displayMetrics.heightPixels;

                LinearLayout mainFooter = findViewById(R.id.mainFooter);
                int mainFooterHeight = mainFooter.getHeight();
                int headerHeight = header.getHeight();
                int marginTop = (int) getResources().getDimension(R.dimen.centerButtons_marginTop);
                int availableHeight = Math.abs(screenHeight - mainFooterHeight - headerHeight - marginTop);
                System.out.println("mfoot = " + mainFooterHeight + "headerhe = " + headerHeight  + "marg top = " + marginTop + "screenhei = " + screenHeight);
                System.out.println("available height = " + availableHeight);
                centerButtons.getLayoutParams().height = availableHeight;
                centerButtons.requestLayout();
            }
        });
    }

    @SuppressLint({"SetTextI18n", "RtlHardcoded"})
    public void initTables() {

        urlHistory history = new urlHistory(this);
        urlTable.removeAllViews();
        timeTable.removeAllViews();
        List<UrlTime> urlTimes = history.getUrlTimes();

        Gson gson = new Gson();
        String jsonString = gson.toJson(urlTimes);

        File outputDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/iFunnyDL");
        if (!outputDirectory.exists()) {
            Log.d("generic pr", "making pictures dir.");
            outputDirectory.mkdirs();
        }
        File outputFile = new File(outputDirectory, "Download-Logs_Make-extension-txt.png");
        try {
            FileWriter writer = new FileWriter(outputFile);
            writer.write(jsonString);
            writer.close();
            Log.d("generic print", "did print file?");
        } catch (IOException e) {
            Log.d("generic print", "did NOT print file");
            Log.e("BADDD!!", String.valueOf(e));
        }

        if (urlTimes.isEmpty()) {
            TableRow row = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText("Missing value");
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.RIGHT);
            textView.setTypeface(fontBodyText);
            row.addView(textView);
            urlTable.addView(row);

            row = new TableRow(this);
            textView = new TextView(this);
            textView.setText("Missing value");
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(fontBodyText);
            row.addView(textView);
            timeTable.addView(row);
        } else {
            for (UrlTime urlTime : urlTimes) {
                TableRow row = new TableRow(this);
                TextView textView = new TextView(this);
                textView.setText(urlTime.getUrl());
                textView.setTextColor(Color.WHITE);
                textView.setGravity(Gravity.RIGHT);
                textView.setTypeface(fontBodyText);
                Linkify.addLinks(textView, Linkify.WEB_URLS);
                row.addView(textView);
                urlTable.addView(row);

                row = new TableRow(this);
                textView = new TextView(this);
                textView.setText(urlTime.getTime());
                textView.setTextColor(Color.WHITE);
                textView.setTypeface(fontBodyText);
                row.addView(textView);
                timeTable.addView(row);
            }
        }
    }
}