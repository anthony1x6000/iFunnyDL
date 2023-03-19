package com.anth1x.ifunnydl;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class DownloadCompleteListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId != -1) {
                Log.d("DownloadCompleteListener", "Download complete: " + downloadId);
                // Send a broadcast to the activity indicating that the download is complete
                Intent broadcastIntent = new Intent("com.example.DOWNLOAD_COMPLETE");
                broadcastIntent.putExtra("downloadId", downloadId);
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            } else {
                Log.e("DownloadCompleteListener", "Error downloading file");
            }
        }
    }
}
