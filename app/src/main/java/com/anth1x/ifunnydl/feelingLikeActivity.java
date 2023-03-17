package com.anth1x.ifunnydl;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import java.util.Objects;

public class feelingLikeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeling_like);
        Objects.requireNonNull(getSupportActionBar()).hide();

        VideoView videoView = findViewById(R.id.videoView);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.vidback;
        videoView.setVideoURI(Uri.parse(videoPath));

        videoView.start();
    }
}