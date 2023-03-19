package com.anth1x.ifunnydl;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CropService extends IntentService {

    private static final int THRESHOLD = 8;
    private static final String outputDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/iFunny/";
    private File asFileTypeOutputDirectory = new File(outputDirectory);
    private String tmpDest = "/iFunnyTMP/";


    public CropService() {
        super("CropService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("started crop service");
        if (!asFileTypeOutputDirectory.exists()) {
            asFileTypeOutputDirectory.mkdirs();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
            return;
        }

        String inputPath = intent.getStringExtra("input_path");
        File inputFile = new File(inputPath);
//        File inputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), tmpDest + "rraahh");
        FileInputStream inputStream = null;
        String filename = inputFile.getName();
        try {
            inputStream = new FileInputStream(inputFile);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (filename.endsWith(".jpg")) {
            Bitmap img = BitmapFactory.decodeStream(inputStream);
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
                System.out.println("Skipped a file, watermark something something");
                return;
            } else {
                cropped = Bitmap.createBitmap(img, 0, 0, width, length - watermark_size);
            }

            // Save image
            File outputDir = new File(outputDirectory);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            File outputFile = new File(outputDir, filename);
            try {
                FileOutputStream out = new FileOutputStream(outputFile);
                cropped.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                System.out.println("Cropped image");
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
