package com.anth1x.ifunnydl;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CropService extends IntentService {

    private static final int THRESHOLD = 8;
    private static final String OUTPUT_DIRECTORY = Environment.DIRECTORY_PICTURES + "/croppedOutput/";

    public CropService() {
        super("ImageCropService");
    }
    private BroadcastReceiver onComplete;

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("started crop service");

        onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                System.out.println("Download complete!");
                unregisterReceiver(onComplete);
                onComplete = null;
            }
        };

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        String inputPath = intent.getStringExtra("input_path");
        if (inputPath == null) {
            Log.d("ImageCropService", "Input path is null");
            return;
        }

        File inputFile = new File(inputPath);
        String filename = inputFile.getName();
        if (filename.endsWith(".jpg")) {
            Bitmap img = BitmapFactory.decodeFile(inputFile.getAbsolutePath());
            int width = img.getWidth();
            int length = img.getHeight();
            int curr_y = length - 1;

            // While color difference is less than threshold, move up and decrease Y height
            while (colorDifference(img.getPixel(0, curr_y), img.getPixel(0, length - 1)) < THRESHOLD) {
                curr_y = curr_y - 1;
            }
            int watermark_size = length - curr_y;

            // For possible bad crops since the average watermark size is 21 pixels or less
            Bitmap cropped;
            if (watermark_size > 21) {
                Log.d("ImageCropService", "[Skipped]: " + filename + ", " + watermark_size);
                return;
            } else {
                cropped = Bitmap.createBitmap(img, 0, 0, width, length - watermark_size);
            }

            // Save image
            File outputDir = new File(OUTPUT_DIRECTORY);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            File outputFile = new File(outputDir, filename);
            try {
                FileOutputStream out = new FileOutputStream(outputFile);
                cropped.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Get color difference
    private static double colorDifference(int pixel1, int pixel2) {
        int red1 = Color.red(pixel1);
        int green1 = Color.green(pixel1);
        int blue1 = Color.blue(pixel1);
        int red2 = Color.red(pixel2);
        int green2 = Color.green(pixel2);
        int blue2 = Color.blue(pixel2);
        double difference = Math.sqrt(
                Math.pow(Math.abs(red2 - red1), 2) +
                        Math.pow(Math.abs(green2 - green1), 2) +
                        Math.pow(Math.abs(blue2 - blue1), 2));
        return difference;
    }
}
