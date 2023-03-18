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

    private String fileNamingScheme;

    public DownloadService() {
        super("DownloadService");
    }
    private String fileName;
    private boolean DMNotif;
    private boolean imgAsiFunnyFormat;

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
        @SuppressLint("DefaultLocale") String finalName = String.format("%d-%s", unixTime, fileNamingScheme);
        return finalName;
    }

    public void downloadWith(String fileURL, int destination) {
        File dir = null;
        String imgFileFormat = null;
        System.out.println("Starting fileURL = " + fileURL);
        if (destination == 2) { // picture DIR
            int index = fileURL.lastIndexOf(".");
            imgFileFormat = "." + fileURL.substring(index + 1);
            System.out.println("imgFileFOrmat = " + imgFileFormat);
            if (imgAsiFunnyFormat) {
                fileName = fileURL.replaceAll(".*/images/", "");
            } else {
                fileName = (getFileName() + imgFileFormat);
            }
            System.out.println("Finalname = " + fileName);
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/iFunny");
        } else if (destination == 1) { // video DIR
            fileName = (getFileName() + ".mp4");
            System.out.println("Finalname = " + fileName);
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/iFunnyDL");
        }
        System.out.println("fileURL before DM request = " + fileURL);

        File mediaFile = new File(dir, fileName);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileURL));
        request.setDestinationUri(Uri.fromFile(mediaFile));
        if (DMNotif) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public void downloadMeth(String finalURL, Boolean doPicture) {
        try {
            Element mediaLink = null;
            Document doc = Jsoup.connect(finalURL).timeout(10 * 1000).get();
            if (doPicture) {
                mediaLink = doc.select("img[src~=(?i)\\.(webp|png|jpe?g|gif)]").first();
                if (mediaLink != null) {
                    String mediaRL = mediaLink.attr("src");
                    System.out.println(mediaRL);
                    downloadWith(mediaRL, 2); // 2: send to iFunny', the main iFunny pictures folder.
                }
            } else {
                mediaLink = doc.select("video[data-src~=(?i)\\.mp4]").first();
                if (mediaLink != null) {
                    String mediaRL = mediaLink.attr("data-src");
                    System.out.println(mediaRL);
                    downloadWith(mediaRL, 1); // 1: send to iFunnyDL.
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);
        imgAsiFunnyFormat = sharedPref.getBoolean("imgAsiFunnyFormat", Boolean.parseBoolean("true"));
        fileNamingScheme = sharedPref.getString("fileName", "iFunny");
        DMNotif = sharedPref.getBoolean("DMNotif", Boolean.parseBoolean("true"));
        String initShare = intent.getStringExtra("initShare");
        String finalURL = parseLink(initShare);
        System.out.println("handLing intent");
        System.out.println("finalURL = " + finalURL);
        if (finalURL != null) {
            if (finalURL.contains("picture")){
                downloadMeth(finalURL, true);
            } else {
                downloadMeth(finalURL, false);
            }
        } else {
            System.out.println("URL is " + finalURL + " | Probably null.");
        }
    }
}