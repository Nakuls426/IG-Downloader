package com.ig.igdownloader.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ig.igdownloader.R;

public class AboutActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        toolbar = findViewById(R.id.about_us_toolbar);
        toolbar.setTitle(R.string.about);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //CLEAR TASK
            startActivity(intent);
        });

        findViewById(R.id.open_source_btn).setOnClickListener(view -> openGit());
    }

    public void openGit() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Nakuls426/IG-Downloader")));
    }
}
