package com.anth1x.ifunnydl;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class fileListener extends Service {
    private static final int REQUEST_EXTERNAL_STORAGE = 5;
    private static final int requestCode = 2;

    private String tmpDest = "/iFunnyTMP/";
    private BroadcastReceiver fileListener;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("FILE LISTENER STARTED");
        registerFileListener();
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void registerFileListener() {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        fileListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // Give permissions
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
                List<String> permissionsToRequest = new ArrayList<>();
                for (String permission : permissions) {
                    if (ContextCompat.checkSelfPermission(fileListener.this, permission) != PackageManager.PERMISSION_GRANTED) {
                        permissionsToRequest.add(permission);
                    }
                }
                if (!permissionsToRequest.isEmpty()) {
                    ActivityCompat.requestPermissions((Activity) getApplicationContext(),
                            permissionsToRequest.toArray(new String[0]),
                            requestCode);
                }
                verifyStoragePermissions((Activity) getApplicationContext());


                System.out.println("Onrecieve hit");
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
                            int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                            String downloadedFilePath = cursor.getString(uriIndex);
                            String fileName = downloadedFilePath.substring(downloadedFilePath.lastIndexOf('/') + 1);
                            String message = "Download completed: " + fileName;
                            System.out.println(message);
                            System.out.println("Asking for crop sercice");
                            Intent cropIntent = new Intent(context, CropService.class);
                            cropIntent.putExtra("input_path", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + tmpDest + fileName));
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
