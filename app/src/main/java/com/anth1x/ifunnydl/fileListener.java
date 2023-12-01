package com.anth1x.ifunnydl;

import static com.anth1x.ifunnydl.globalDefaults.iFunnyTMPString;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class fileListener extends Service {
    private BroadcastReceiver fileListener;

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("FILE LISTENER STARTED");
        registerFileListener();
    }

    private void registerFileListener() {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

        fileListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                    processDownloadComplete(context, intent);
                }
            }
        };

        registerReceiver(fileListener, filter);
    }

    private void processDownloadComplete(Context context, Intent intent) {
        long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);

        Cursor cursor = downloadManager.query(query);

        if (cursor.moveToFirst()) {
            processCursor(context, cursor);
        }

        cursor.close();
    }

    private void processCursor(Context context, Cursor cursor) {
        int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);

        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(statusIndex)) {
            String inputImageDestination = iFunnyTMPString + "/";
            int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);

            String downloadedFilePath = cursor.getString(uriIndex);
            String fileName = downloadedFilePath.substring(downloadedFilePath.lastIndexOf('/') + 1);

            System.out.println("Download completed: " + fileName);
            System.out.println("Asking for crop service");

            inputImageDestination += fileName;
            System.out.println("temp file location = " + inputImageDestination);

            startCropService(context, inputImageDestination);

            stopSelf();
        }
    }

    private void startCropService(Context context, String inputImageDestination) {
        Intent cropIntent = new Intent(context, CropService.class);
        cropIntent.putExtra("input_path", inputImageDestination);
        context.startService(cropIntent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Killed file listener.");
        unregisterReceiver(fileListener);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
