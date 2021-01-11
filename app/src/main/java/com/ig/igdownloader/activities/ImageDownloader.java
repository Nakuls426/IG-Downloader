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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ig.igdownloader.R;
import com.ig.igdownloader.datahold.UserDetails;
import com.ig.igdownloader.nakul.Endpoints;
import com.ig.igdownloader.nakul.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ImageDownloader extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ImageDownloader";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 128;
    private String userId;
    Toolbar toolbar;
    long imageId = 0;
    private TextView userFullName;
    private ImageView userFullImage;
    private TextView userBiography;
    private TextView userFollower;
    private TextView userFollowing;
    private TextView userName;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_user_activity);
        toolbar = findViewById(R.id.custom_tool_image_act);
        toolbar.setTitle(R.string.photo_preview);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        userName = findViewById(R.id.preview_user_name);
        userFullName = findViewById(R.id.preview_user_full_name);
        userBiography = findViewById(R.id.preview_user_bio);
        userFullImage = findViewById(R.id.preview_user_pic);
        userFollowing = findViewById(R.id.preview_following_count);
        userFollower = findViewById(R.id.preview_follower_count);
        progressBar = findViewById(R.id.custom_progress_bar_image_act);
        progressBar.setVisibility(View.GONE);
        userName.setText(String.format("Username : %s", getIntent().getStringExtra("userName")));
        userFullName.setText(String.format("Full Name : %s", getIntent().getStringExtra("userFullName")));
        userProfileDetails(getIntent().getStringExtra("pk"));
        findViewById(R.id.download_button).setOnClickListener(this);
    }

    private void userProfileDetails(String pk) {
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest request = new JsonObjectRequest(0, Endpoints.INSTAGRAM_HD_PIC_ENDPOINT + Endpoints.INSTAGRAM_USER_ID
                + pk, null, response -> {
            UserDetails userDetails;
            try {
                JSONObject ob = new JSONObject(response.toString());
                JSONObject ob1 = ob.getJSONObject("user");
                String biography = ob1.getString("biography");
                String follower_count = ob1.getString("follower_count");
                String following_count = ob1.getString("following_count");

                userBiography.setText(String.format("Biography : %s", biography));
                userFollower.setText(String.format("Follower : %s", follower_count));
                userFollowing.setText(String.format("Following : %s", following_count));
                JSONObject ob2 = ob1.getJSONObject("hd_profile_pic_url_info");

                Glide.with(ImageDownloader.this).load(ob2.getString("url")).into(userFullImage);
                userDetails = new UserDetails(null, null, null, null, ob2.getString("url"));

                UserDetails finalUserDetails = userDetails;
                        /*findViewById(R.id.download_button).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadManager(finalUserDetails.getUserPicURL());
                            }
                        });*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
        }, Throwable::printStackTrace);
        Volley.newRequestQueue(this).add(request);
    }

    private void getDownloadImage() {
        String serviceURL = Utils.getServiceURL(getIntent().getStringExtra("pk"));
        JsonObjectRequest request = new JsonObjectRequest(0, serviceURL, null, response -> {
            try {
                JSONObject ob = response.getJSONObject("user");
                JSONObject ob1 = ob.getJSONObject("hd_profile_pic_url_info");
                downloadManager(ob1.getString("url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.download_button) {
            checkingPermissions();
        }
    }

    private void downloadManager(final String link) {
        Utils.checkFolder();
        String videoTitle;
        if (link.contains("s150x150") || link.contains("s320x320") || link.contains("_a.jpg")) {
            videoTitle = link.substring(55).split("_a.jpg")[0] + "_a.jpg";
        } else
            videoTitle = link.substring(55).split("_n.jpg")[0] + "_n.jpg";

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
        imageId = downloadManager.enqueue(request);
        Toast.makeText(this, "Downloading", Toast.LENGTH_SHORT).show();
    }

    private void checkingPermissions() {
        if (ContextCompat.checkSelfPermission(ImageDownloader.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ImageDownloader.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 128);
        }
        if (ContextCompat.checkSelfPermission(ImageDownloader.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            getDownloadImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission granted");
                Utils.checkFolder();
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