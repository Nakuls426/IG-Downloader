package com.ig.igdownloader.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ig.igdownloader.R;
import com.ig.igdownloader.adapters.VideoAdapterActivity;
import com.ig.igdownloader.datahold.DataUser;
import com.ig.igdownloader.nakul.Endpoints;
import com.ig.igdownloader.nakul.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SlideVideoActivity extends AppCompatActivity {
    private static final String TAG = "SlideVideoActivity";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 127;
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    ArrayList<DataUser> userData;
    long video = 0;
    String holdVideos;
    Toolbar toolbar;
    ProgressBar progressBar;
    EditText pasteLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_videos_activity);
        toolbar = findViewById(R.id.custom_tool_slide_videos);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setTitle("Videos");

        toolbar.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        pasteLink = findViewById(R.id.paste_link);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        userData = new ArrayList<>();
        holdVideos = getIntent().getStringExtra("video_url");
        progressBar = findViewById(R.id.slide_videos_progress_bar);
        progressBar.setVisibility(View.GONE);

        if (holdVideos != null) {
            pasteLink.setText(holdVideos);
            String parsedURL = holdVideos.split("\\?")[0];
            jsonResponse(parsedURL);
            userData.clear();
        }
        findViewById(R.id.search_video_slide).setOnClickListener(v -> {
            jsonResponse(pasteLink.getText().toString().split("\\?")[0]);
            userData.clear();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission Granted");
            } else {
                for (String permission : permissions) {
                    boolean showRationale = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission);
                        Toast.makeText(this, "Permission require", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onRequestPermissionResult");
                    }
                    if (!showRationale) {
                        Utils.alertDialog(this);
                    }
                }
            }
        }
    }

    private void jsonResponse(String URL) {
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest objectRequest = new JsonObjectRequest(0, Endpoints.INSTAGRAM_VIDEOS_ENDPOINT + Endpoints.INSTAGRAM_VIDEO_PATH + URL, null, response -> {
            try {
                JSONObject ob = new JSONObject(response.toString());
                JSONArray ar = ob.getJSONArray("info");
                userData.clear();
                for (int i = 0; i < ar.length(); i++) {
                    JSONObject object = ar.getJSONObject(i);
                    DataUser thumbnailList = new DataUser(object.getString("thumbnail"), object.getString("video_url"));
                    userData.add(thumbnailList);
                    Log.d(TAG, "Thumbnails --> " + thumbnailList.getThumbnailURL());
                    Log.d(TAG, "Videos --> " + thumbnailList.getVideoUrl());
                }
                Log.d(TAG, "onResponse: " + userData.toString());

                initData(userData);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
        }, Throwable::printStackTrace);
        Volley.newRequestQueue(this).add(objectRequest);
    }

    private void initData(ArrayList<DataUser> userData) {
        adapter = new VideoAdapterActivity(userData, this);
        recyclerView.scheduleLayoutAnimation();
        recyclerView.setAdapter(adapter);
    }
}