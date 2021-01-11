package com.ig.igdownloader.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ig.igdownloader.R;
import com.ig.igdownloader.nakul.Utils;

import java.io.File;

public class PlayVideoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 127;
    private static final String TAG = "PlayVideoActivity";
    Toolbar toolbar;
    long videoId = 0;
    private ProgressBar progressBar;
    private MediaController mediaController;
    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video_activity);
        toolbar = findViewById(R.id.custom_tool_play_video);
        toolbar.setTitle("Video Preview");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, SlideVideoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //CLEAR TASK
            startActivity(intent);
        });
        findViewById(R.id.fab_download).setOnClickListener(this);
        if (getIntent().getStringExtra("play_link") == null) {
            initMediaController(getIntent().getStringExtra("video_url"));
        } else {
            initMediaController(getIntent().getStringExtra("play_link"));
        }

    }

    private void initMediaController(String videoLink) {
        videoView = findViewById(R.id.video_view);
        mediaController = new MediaController(this);
        videoView.setVideoURI(Uri.parse(videoLink));
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
    }

    private void downloadManager(final String link) {
        Utils.checkFolder();
        String videoTitle = link.substring(55).split("_n.mp4")[0] + "_n.mp4";
        File file = new File(Environment.getExternalStorageDirectory() + "/IGDownloader");
        File file1 = new File(file, videoTitle);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(link))
                .setDescription(videoTitle)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                .setDestinationUri(Uri.fromFile(file1))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(videoTitle);
        DownloadManager downloadManager = (DownloadManager) this.getSystemService(DOWNLOAD_SERVICE);
        videoId = downloadManager.enqueue(request);
        Toast.makeText(this, "Downloading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fab_download) {
//            downloadManager(getIntent().getStringExtra("play_link"));
            checkingPermissions(getIntent().getStringExtra("play_link"));
        }
    }

    private void checkingPermissions(String videoLink) {
        if (ContextCompat.checkSelfPermission(PlayVideoActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PlayVideoActivity.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 127);
        }
        if (ContextCompat.checkSelfPermission(PlayVideoActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            downloadManager(videoLink);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted");
            } else {
                for (String permission : permissions) {
                    boolean showRationale = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission);
                        Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
                    }
                    if (!showRationale) {
                        Utils.alertDialog(this);
                    }
                }
            }
        }
    }
}