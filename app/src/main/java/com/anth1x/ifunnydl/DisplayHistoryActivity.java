package com.anth1x.ifunnydl;

import static com.anth1x.ifunnydl.fonts.fontBodyText;
import static com.anth1x.ifunnydl.fonts.fontButton;
import static com.anth1x.ifunnydl.fonts.fontSubtitle;
import static com.anth1x.ifunnydl.fonts.fontTitle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        final RelativeLayout mainFooter = findViewById(R.id.mainFooter);
        int statusBarHeight = getStatusBarHeight();
        int navigationBarHeight = getNavigationBarHeight();
        layout.setPadding(0, statusBarHeight, 0, 0);
        mainFooter.setPadding(0, 0, 0, navigationBarHeight);

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int screenHeight = displayMetrics.heightPixels;

        final ScrollView scrollView = findViewById(R.id.scrollView);

        mainFooter.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mainFooter.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int mainFooterHeight = mainFooter.getHeight() * 2;
                int viewHeight = screenHeight - mainFooterHeight;
                System.out.println("foothe = " + mainFooterHeight);

                ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
                layoutParams.height = viewHeight;
                scrollView.setLayoutParams(layoutParams);
            }
        });
    }

    @SuppressLint({"SetTextI18n", "RtlHardcoded"})
    public void initTables() {
        urlHistory history = new urlHistory(this);
        urlTable.removeAllViews();
        timeTable.removeAllViews();
        List<UrlTime> urlTimes = history.getUrlTimes();

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