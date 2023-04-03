package com.anth1x.ifunnydl;

import java.io.File;
import java.io.IOException;
public class LogWriter {
    public static void writeLog(String tmpDest) {
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