package com.anth1x.ifunnydl;

import static com.anth1x.ifunnydl.globalDefaults.tmpDest;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CropService extends IntentService {

    private static final int THRESHOLD = 8;
    private static final String outputDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/iFunny/";
    private final File asFileTypeOutputDirectory = new File(outputDirectory);

    public CropService() {
        super("CropService");
    }

    public static void deleteDirectory() {
        if (tmpDest.isDirectory()) {
            String[] children = tmpDest.list();
            assert children != null;
            for (String child : children) {
                new File(tmpDest, child).delete();
            }
        }
        tmpDest.delete();
        System.out.println("Deleted tmpDest");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        System.out.println("started crop service");
        if (!asFileTypeOutputDirectory.exists()) {
            asFileTypeOutputDirectory.mkdirs();
        }

        String inputPath = intent.getStringExtra("input_path");
        File inputFile = new File(inputPath);
        String filename = inputFile.getName();

        if (filename.endsWith(".jpg")) {
            Bitmap img = BitmapFactory.decodeFile(inputFile.getAbsolutePath());
            int width = img.getWidth();
            int length = img.getHeight();
            int curr_y = length - 1;

            while (colorDifference(img.getPixel(0, curr_y), img.getPixel(0, length - 1)) < THRESHOLD) {
                curr_y = curr_y - 1;
            }
            int watermark_size = length - curr_y;

            Bitmap cropped;
            if (watermark_size > 21) {
                System.out.println("Skipped a file.");
                return;
            } else {
                cropped = Bitmap.createBitmap(img, 0, 0, width, length - watermark_size);
            }

            // Save image
            if (!asFileTypeOutputDirectory.exists()) {
                asFileTypeOutputDirectory.mkdirs();
            }
            File outputFile = new File(asFileTypeOutputDirectory, filename);
            try {
                FileOutputStream out = new FileOutputStream(outputFile);
                cropped.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                System.out.println("Cropped image");

                System.out.println("Deleting temp directory");
                deleteDirectory();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private static double colorDifference(int pixel1, int pixel2) {
        int red1 = Color.red(pixel1);
        int green1 = Color.green(pixel1);
        int blue1 = Color.blue(pixel1);
        int red2 = Color.red(pixel2);
        int green2 = Color.green(pixel2);
        int blue2 = Color.blue(pixel2);
        return Math.sqrt(
                Math.pow(Math.abs(red2 - red1), 2) +
                        Math.pow(Math.abs(green2 - green1), 2) +
                        Math.pow(Math.abs(blue2 - blue1), 2));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Killed Crop Service.");
    }
}
