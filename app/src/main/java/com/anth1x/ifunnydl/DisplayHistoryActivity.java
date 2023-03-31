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
import android.text.util.Linkify;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.Set;

public class DisplayHistoryActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String URL_KEY = "URL_KEY";
    private static final String TIME_KEY = "TIME_KEY";
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

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
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
        RelativeLayout mainFooter = findViewById(R.id.mainFooter);
        int statusBarHeight = getStatusBarHeight();
        int navigationBarHeight = getNavigationBarHeight();
        layout.setPadding(0, statusBarHeight, 0, 0);
        mainFooter.setPadding(0, 0, 0, navigationBarHeight);
    }

    public void initTables() {
        urlHistory history = new urlHistory(this);
        urlTable.removeAllViews();
        timeTable.removeAllViews();
        Set<String> urls = history.getURLs();
        Set<String> times = history.getTimes();

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        for (int i = 0; i < 40; i++) {
//            urls.add("https://www.example.com/page" + i);
//            times.add(new Date().toString());
//        }
//        editor.putStringSet(URL_KEY, urls);
//        editor.putStringSet(TIME_KEY, times);
//        editor.apply();

        if (urls.isEmpty()) {
            TableRow row = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText("Missing value");
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.RIGHT);
            textView.setTypeface(fontBodyText);
            row.addView(textView);
            urlTable.addView(row);
        } else {
            for (String url : urls) {
                TableRow row = new TableRow(this);
                TextView textView = new TextView(this);
                textView.setText(url);
                textView.setTextColor(Color.WHITE);
                textView.setGravity(Gravity.RIGHT);
                textView.setTypeface(fontBodyText);
                Linkify.addLinks(textView, Linkify.WEB_URLS);
                row.addView(textView);
                urlTable.addView(row);
            }
        }

        if (times.isEmpty()) {
            TableRow row = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText("Missing value");
            textView.setTextColor(Color.WHITE);
            textView.setTypeface(fontBodyText);
            row.addView(textView);
            timeTable.addView(row);
        } else {
            for (String time : times) {
                TableRow row = new TableRow(this);
                TextView textView = new TextView(this);
                textView.setText(time);
                textView.setTextColor(Color.WHITE);
                textView.setTypeface(fontBodyText);
                row.addView(textView);
                timeTable.addView(row);
            }
        }
    }
}