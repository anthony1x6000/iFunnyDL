package com.anth1x.ifunnydl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class urlHistory {
    private final SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME = "urlHistory";
    private static final String URL_KEY = "urls";
    private static final String TIME_KEY = "times";

    public urlHistory(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void addURL(String url) {
        Set<String> urls = getURLs();
        urls.add(url);
        sharedPreferences.edit().putStringSet(URL_KEY, urls).apply();
        System.out.println("Added new url = " + url);

        Set<String> times = getTimes();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd | HH:mm:ss");
        String currentTime = dateFormat.format(new Date());
        times.add(currentTime);
        sharedPreferences.edit().putStringSet(TIME_KEY, times).apply();
        System.out.println("Added new time = " + currentTime);
    }

    public Set<String> getURLs() {
        return sharedPreferences.getStringSet(URL_KEY, new HashSet<>());
    }

    public Set<String> getTimes() {
        return sharedPreferences.getStringSet(TIME_KEY, new HashSet<>());
    }

    public void clearHistory() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(URL_KEY);
        editor.remove(TIME_KEY);
        editor.apply();
    }
}