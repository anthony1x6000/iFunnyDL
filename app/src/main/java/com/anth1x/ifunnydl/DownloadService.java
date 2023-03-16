package com.anth1x.ifunnydl;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
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

    public String parseLink(String initShare) {
        String url = null;
        if (initShare != null) {
            Pattern pattern = Pattern.compile("(https?://\\S+)");
            Matcher matcher = pattern.matcher(initShare);
            if (matcher.find()) {
                url = matcher.group(1);
            }
        }
        return url;
    }

    public String getFileName() {
        long unixTime = System.currentTimeMillis() / 1000L;
        return unixTime + "-iFunny.mp4";
    }

    public void downloadWith(String fileURL) {
        String fileName = getFileName();
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/iFunnyDL");
        File image = new File(dir, fileName);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileURL));
        request.setDestinationUri(Uri.fromFile(image));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String initShare = intent.getStringExtra("initShare");
        String finalURL = parseLink(initShare);
        System.out.println("handLing intent");
        System.out.println("finalURL = " + finalURL);
        try {
            Document doc = Jsoup.connect(finalURL).timeout(10*1000).get();

            Element vidLink = doc.select("video[data-src~=(?i)\\.mp4]").first();
            if (vidLink != null) {
                String mediaRL = vidLink.attr("data-src");
                System.out.println(mediaRL);
                downloadWith(mediaRL);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}