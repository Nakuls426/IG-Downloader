package com.ig.igdownloader.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.ig.igdownloader.ImagePreviewActivity;
import com.ig.igdownloader.R;
import com.ig.igdownloader.nakul.Utils;

public class ImageDownloader extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ImageDownloader";
    private String userId;
    String picURL;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_user_activity);
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView fullName = findViewById(R.id.full_name);
        TextView userBio = findViewById(R.id.full_userbio);
        ImageView setUserPic = findViewById(R.id.user_pic);
        picURL = getIntent().getStringExtra("USER_PIC_URL");
        userId = getIntent().getStringExtra("USER_ID");
        intents(fullName, userBio, setUserPic);
        findViewById(R.id.downl_pic).setOnClickListener(this);
    }

    private void intents(TextView fullName, TextView userBio, ImageView setUserPic) {
        fullName.setText(getIntent().getStringExtra("USER_FULL_NAME"));
        userBio.setText(getIntent().getStringExtra("USER_BIO"));
        picURL = getIntent().getStringExtra("USER_PIC_URL");
        new ImagePreviewActivity(picURL, setUserPic).execute();
    }

    private void gettingPicFromMainServer(final String userId) {
        String serviceURL = Utils.getServiceURL(userId);
        AndroidNetworking.get(serviceURL).setTag("test").setPriority(Priority.LOW).build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        String parseBigPP = Utils.parseProfilePic(response);
                        Log.d(TAG, "serviceURL ---- > " + parseBigPP);
                        Log.d(TAG, "User user ID -----> " + userId);
                        downloadProfilePic(parseBigPP);
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.getErrorDetail();
                    }
                });
    }

    private void downloadProfilePic(String picURL) {
        AndroidNetworking.download(picURL, Utils.imageDestionation(), Utils.fileName() + ".JPG")
                .setTag("downloading").build().setDownloadProgressListener(new DownloadProgressListener() {
            @Override
            public void onProgress(long bytesDownloaded, long totalBytes) {
                /*long megaBytes = 1024L * 1024L;
                long b = bytesDownloaded/ megaBytes;
                long c = totalBytes/megaBytes;
                Log.d(TAG,"VIDEO ----- > " +b + "/" +c);
                downloadedOf.setText(b + "/" + c);*/
            }
        }).startDownload(new DownloadListener() {
            @Override
            public void onDownloadComplete() {
                Log.d(TAG, "Image downloaded");
                Toast.makeText(ImageDownloader.this, "Image downloaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(ANError anError) {
                anError.getErrorDetail();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.downl_pic) {
            finalDownloadImage();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(ImageDownloader.this, MainActivity2.class);
            overridePendingTransition(0, 0);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void finalDownloadImage() {
        if (ContextCompat.checkSelfPermission(ImageDownloader.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ImageDownloader.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 127);
        }
        if (ContextCompat.checkSelfPermission(ImageDownloader.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Utils.checkFolder();
            gettingPicFromMainServer(userId);
        }
    }
}