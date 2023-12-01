package com.anth1x.ifunnydl;

import static com.anth1x.ifunnydl.globalDefaults.iFunnyDIR;
import static com.anth1x.ifunnydl.globalDefaults.iFunnyTMP;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadService extends IntentService {

    public DownloadService() {
        super("DownloadService");
    }

    private String fileNamingScheme;
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

    public void downloadWith(String fileURL, int handleSend) {
        File dir = null;
        String imageExtension;
        System.out.println("Starting fileURL = " + fileURL);

        String fileName = getFileName();
        if (handleSend == 0) { // picture DIR
            int index = fileURL.lastIndexOf(".");
            imageExtension = "." + fileURL.substring(index + 1);
            System.out.println("imageExtension = " + imageExtension);
            if (imgAsiFunnyFormat) {
                fileName = fileURL.replaceAll(".*/images/", "");
            } else {
                fileName += imageExtension;
            }
            System.out.println("Finalname = " + fileName);

            dir = iFunnyTMP;
            System.out.println("send to Directory = " + dir);
        } else if (handleSend == 1) { // gif
            int index = fileURL.lastIndexOf(".");
            imageExtension = "." + fileURL.substring(index + 1);
            System.out.println("imageExtension = " + imageExtension);
            if (imgAsiFunnyFormat) {
                fileName = fileURL.replaceAll(".*/images/", "");
            } else {
                fileName += imageExtension;
            }
            System.out.println("Final Name = " + fileName);

            dir = iFunnyDIR;
            System.out.println("send to Directory = " + dir);
        } else {
            // video DIR
            fileName += ".mp4";
            System.out.println("Final Name = " + fileName);
            String vidDest = "/iFunnyDL";
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + vidDest);
        }

        System.out.println("fileURL before download manager request = " + fileURL);

        File mediaFile = new File(dir, fileName);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileURL));
        request.setDestinationUri(Uri.fromFile(mediaFile));
        if (DMNotif) request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        System.out.println("Seemed to download well. Deleting temp directory for logs.");

        if (handleSend == 0) {
            System.out.println("Downloaded file is a picture. Asking for fileListener");
            Intent intent = new Intent(this, fileListener.class);
            startService(intent);
        }
        CropService.deleteDirectory();
    }

    public void downloadMeth(String finalURL, int mHandle) {
        try {
            Element mediaLink;
            Document doc = Jsoup.connect(finalURL).timeout(10 * 1000).get();

            if (mHandle == 0) {
                System.out.println("Handling as picture.");
                mediaLink = doc.select("img[src~=(?i)\\.(webp|png|jpe?g|gif)]").first();
                if (mediaLink != null) {
                    String mediaRL = mediaLink.attr("src");
                    System.out.println(mediaRL);
                    downloadWith(mediaRL, 0);
                } else {
                    System.out.println("media link is null.");
                }
            } else if (mHandle == 1) {
                System.out.println("Handling as gif.");
                DMNotif = false;
                System.out.println("DMNotif set to false temporarily.");
                mediaLink = doc.select("link[rel='preload'][href$='.gif']").first();
                if (mediaLink != null) {
                    String mediaRL = mediaLink.attr("href");
                    System.out.println(mediaRL);
                    downloadWith(mediaRL, 1);
                } else {
                    System.out.println("media link is null.");
                    System.out.println("Doc = " + doc);
                }
            } else if (mHandle == 2) {
                System.out.println("Handling as video.");
                mediaLink = doc.select("video[data-src~=(?i)\\.mp4]").first();
                if (mediaLink != null) {
                    String mediaRL = mediaLink.attr("data-src");
                    System.out.println(mediaRL);
                    downloadWith(mediaRL, 2);
                } else {
                    System.out.println("media link is null.");
                }
            } else {
                System.out.println("Unspecified handling");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
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
            System.out.println("Not null, handling.");
            if (finalURL.contains("picture")) {
                System.out.println("URL is Picture");
                downloadMeth(finalURL, 0);
            } else if (finalURL.contains("gif")) {
                System.out.println("URL is GIF");
                downloadMeth(finalURL, 1);
            } else {
                System.out.println("URL is other, video.");
                downloadMeth(finalURL, 2);
            }
        } else {
            System.out.println("URL is " + finalURL + " | Probably null.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Killed Download Service.");
    }
}