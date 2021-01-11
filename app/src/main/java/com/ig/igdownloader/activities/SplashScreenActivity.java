package com.ig.igdownloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ig.igdownloader.R;

public class SplashScreenActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        imageView = findViewById(R.id.i);
        textView = findViewById(R.id.ig_downloader_splash);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1000);

        imageView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        textView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
    }
}
