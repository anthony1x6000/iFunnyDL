package com.anth1x.ifunnydl;

import static com.anth1x.ifunnydl.globalDefaults.tmpDestString;

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
            private String inputImageDestination;

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor cursor = downloadManager.query(query);
                    if (cursor.moveToFirst()) {
                        int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(statusIndex)) {
                            inputImageDestination = tmpDestString + "/";
                            int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                            String downloadedFilePath = cursor.getString(uriIndex);
                            String fileName = downloadedFilePath.substring(downloadedFilePath.lastIndexOf('/') + 1);
                            String message = "Download completed: " + fileName;
                            System.out.println(message);
                            System.out.println("Asking for crop service");
                            Intent cropIntent = new Intent(context, CropService.class);
                            inputImageDestination += fileName;
                            System.out.println("temp file location = " + inputImageDestination);
                            cropIntent.putExtra("input_path", inputImageDestination);
                            context.startService(cropIntent);
                        }
                    }
                    cursor.close();
                }
            }
        };
        registerReceiver(fileListener, filter);
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
