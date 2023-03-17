package com.anth1x.ifunnydl;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class DownloadService extends IntentService {


    public DownloadService() {
        super("DownloadService");
    }
    private String fileName;
    private boolean DMNotif;

    public String parseLink(String initShare) {
        String url = null;
        if (initShare != null) {
            Pattern pattern = Pattern.compile("(https?://.*ifunny.*\\S*)");
            Matcher matcher = pattern.matcher(initShare);
            if (matcher.find()) {
                url = matcher.group(1);
            }
        }
        return url;
    }

    public String getFileName() {
        long unixTime = System.currentTimeMillis() / 1000L;
        @SuppressLint("DefaultLocale") String finalName = String.format("%d-%s.mp4", unixTime, fileName);
        System.out.println("Finalname = " + finalName);
        return finalName;
    }

    public void downloadWith(String fileURL) {
        String fileName = getFileName();
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/iFunnyDL");
        File image = new File(dir, fileName);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileURL));
        request.setDestinationUri(Uri.fromFile(image));
        if (DMNotif) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);
        fileName = sharedPref.getString("fileName", "iFunny");
        DMNotif = sharedPref.getBoolean("DMNotif", Boolean.parseBoolean("true"));
        String initShare = intent.getStringExtra("initShare");
        String finalURL = parseLink(initShare);
        System.out.println("handLing intent");
        System.out.println("finalURL = " + finalURL);
        if (finalURL != null) {
            try {
                Document doc = Jsoup.connect(finalURL).timeout(10 * 1000).get();

                Element vidLink = doc.select("video[data-src~=(?i)\\.mp4]").first();
                if (vidLink != null) {
                    String mediaRL = vidLink.attr("data-src");
                    System.out.println(mediaRL);
                    downloadWith(mediaRL);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("URL is " + finalURL + " | Probably null.");
        }
    }
}