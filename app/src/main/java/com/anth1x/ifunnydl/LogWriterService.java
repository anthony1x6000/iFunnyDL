package com.anth1x.ifunnydl;

import android.app.IntentService;
import android.content.Intent;
import java.io.File;
import java.io.IOException;

public class LogWriterService extends IntentService {
    public static final String EXTRA_DEST = "DESTINATION";

    public LogWriterService() {
        super("LogWriterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("Called intent for tele");
        if (intent != null) {
            String tmpDest = intent.getStringExtra(EXTRA_DEST);
            File logDirectory = new File(tmpDest);
            if (!logDirectory.exists()) {
                logDirectory.mkdirs();
            }
            writeLog(tmpDest);
        }
    }

    private void writeLog(String tmpDest) {
        System.out.println("in tele log service. Writing to " + tmpDest);
        File logDirectory = new File(tmpDest);
        if (!logDirectory.exists()) {
            logDirectory.mkdirs();
        }
        if (logDirectory.canWrite()) {
            File logFile = new File(logDirectory, "logcat_" + System.currentTimeMillis() + ".txt");
            try {
                Process process = Runtime.getRuntime().exec("logcat -c");
                process = Runtime.getRuntime().exec("logcat -f " + logFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Log DIR does not exist.");
        }
    }
}
