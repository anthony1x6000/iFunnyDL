package com.anth1x.ifunnydl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class urlHistory {
    private final SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME = "urlHistory";
    private static final String URL_TIME_KEY = "urlTimes";

    public urlHistory(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void addURL(String url) {

        if (!(sharedPreferences.getBoolean("doLogging", true))) {
            System.out.println("Logging disabled.");
            return;
        }

        List<UrlTime> urlTimes = getUrlTimes();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd | HH:mm:ss");
        String currentTime = dateFormat.format(new Date());
        urlTimes.add(new UrlTime(url, currentTime));
        saveUrlTimes(urlTimes);
        System.out.println("Added new url = " + url + " with time = " + currentTime);
    }

    public List<UrlTime> getUrlTimes() {
        String json = sharedPreferences.getString(URL_TIME_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        } else {
            Type type = new TypeToken<List<UrlTime>>(){}.getType();
            return new Gson().fromJson(json, type);
        }
    }

    private void saveUrlTimes(List<UrlTime> urlTimes) {
        String json = new Gson().toJson(urlTimes);
        sharedPreferences.edit().putString(URL_TIME_KEY, json).apply();
    }

    public void clearHistory() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(URL_TIME_KEY);
        editor.apply();
    }
}