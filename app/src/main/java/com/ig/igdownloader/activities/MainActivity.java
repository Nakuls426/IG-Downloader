package com.ig.igdownloader.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ig.igdownloader.R;
import com.ig.igdownloader.VideoDownloaderActivity;
import com.ig.igdownloader.nakul.BuildConfig;
import com.ig.igdownloader.nakul.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 127;
    EditText userLink;
    VideoView videoView;
    MediaController mediaController;
    TextView videoTP;
    Button downloadButton;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    BottomNavigationView navigationView;
    ProgressBar bar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoTP = findViewById(R.id.video_p);
        videoView = findViewById(R.id.video_preview);
        downloadButton = findViewById(R.id.download);
        bar = findViewById(R.id.main_progress_bar);
        bar.setVisibility(View.GONE);
        videoTP.setVisibility(View.GONE);

        findViewById(R.id.download).setEnabled(false);
        videoView.setVisibility(View.GONE);
        preferences = getApplicationContext().getSharedPreferences("data",0);

        mediaController = new MediaController(this);
        userLink = findViewById(R.id.user_link);
        navigationView = findViewById(R.id.bottom_nav_view2);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.video_downloader);
        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources()
                .getColor(R.color.primaryTextWhite)));

        findViewById(R.id.search_button_main).setOnClickListener(this);
        findViewById(R.id.download).setOnClickListener(this);
    }

    private void parsingURL(String URL) {
        bar.setVisibility(View.VISIBLE);
        AndroidNetworking.get(URL + BuildConfig.INSTAGRAM_ENDPOINT)
                .setTag("video-link")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,"response "+ response);
                        try {

                            JSONObject object = response.getJSONObject("graphql");
                            JSONObject object1 = object.getJSONObject("shortcode_media");

                            Uri uri = Uri.parse(object1.getString("video_url"));
                            String videoUrl = object1.getString("video_url");
                            videoView.setVideoURI(uri);
                            editor = preferences.edit();
                            editor.putString("video_url",videoUrl);
                            editor.apply();
                            videoTP.setVisibility(View.VISIBLE);
                            videoView.setVisibility(View.VISIBLE);
                            mediaController.setAnchorView(videoView);
                            videoView.setMediaController(mediaController);
                            downloadButton.setEnabled(true);
                            videoView.start();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
    private void downloadVideo() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE},127);
        } if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED) {
            Utils.checkFolder();
            final VideoDownloaderActivity activity = new VideoDownloaderActivity(this);
            activity.execute(preferences.getString("video_url",""));
            downloadButton.setEnabled(false);
            Log.d(TAG,"video url - " +preferences.getString("video_url",""));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.checkFolder();
            }
            else {
                for (String permission : permissions) {
                    boolean showRationale = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        Utils.settings(this);
                    }
                }
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                Utils.alertDialog(this);
                return true;
            case R.id.use:
                Utils.alertDialog1(this);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.video_downloader) {
            return true;
        } else if (item.getItemId() == R.id.photo_downloader) {
            startActivity(new Intent(getApplicationContext(), MainActivity2.class));
            overridePendingTransition(0,0);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int x = v.getId();
        if ( x == R.id.search_button_main) {
            String pasteBar = userLink.getText().toString();
            if (!TextUtils.isEmpty(pasteBar) && pasteBar.startsWith("https://www.instagram.com/")) {
                String URL = userLink.getText().toString();
                parsingURL(URL);
            } if (TextUtils.isEmpty(pasteBar))
                Toast.makeText(MainActivity.this, "Paste url", Toast.LENGTH_SHORT).show();
             if (!pasteBar.startsWith("https://www.instagram.com/"))
                Toast.makeText(MainActivity.this, "Invalid url", Toast.LENGTH_SHORT).show();
        } else if ( x == R.id.download) {
            downloadVideo();
        }
    }
}