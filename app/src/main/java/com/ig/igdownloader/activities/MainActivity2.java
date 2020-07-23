package com.ig.igdownloader.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ig.igdownloader.ImagePreviewActivity;
import com.ig.igdownloader.R;
import com.ig.igdownloader.nakul.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity2";
    EditText userEdit;
    BottomNavigationView navigationView;
    ImageView userImage;
    TextView userName;
    public String userFullName;
    public String userBio;
    public String photo;
    public String userId;
    ProgressBar progressBar;
    LinearLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_downloader);

        navigationView = findViewById(R.id.bottom_nav_view);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.photo_downloader);
        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryTextWhite)));
        userEdit = findViewById(R.id.user_edit);
        userName = findViewById(R.id.user_name);
        userImage = findViewById(R.id.user_image);
        progressBar = findViewById(R.id.progress_bar1);
        progressBar.setVisibility(View.GONE);
        findViewById(R.id.search_photo).setOnClickListener(this);
        findViewById(R.id.linear_layout).setOnClickListener(this);
        layout = findViewById(R.id.app_linear);
    }


    private void parsingPhotoURL(String name) {
        showProgressBar();
        AndroidNetworking.get(BuildConfig.INSTAGRAM_URL + name + BuildConfig.INSTAGRAM_ENDPOINT)
                .setTag("json-response")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject object = response.getJSONObject("graphql");
                            JSONObject object1 = object.getJSONObject("user");

                            userId = object1.getString("id");
                            userFullName = object1.getString("full_name");
                            userBio = object1.getString("biography");
                            photo = object1.getString("profile_pic_url_hd");

                            new ImagePreviewActivity(photo, userImage).execute();
                            userName.setText(userFullName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        hideProgressBar();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "AN Error -----> " + anError.getErrorDetail());
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.photo_downloader) {
            return true;
        } else if (item.getItemId() == R.id.video_downloader) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.search_photo) {
            String userName = userEdit.getText().toString();
            parsingPhotoURL(userName);
        } else if (i == R.id.linear_layout) {
            Intent intent = new Intent(MainActivity2.this, ImageDownloader.class);
            intent.putExtra("USER_FULL_NAME", userFullName);
            intent.putExtra("USER_BIO", userBio);
            intent.putExtra("USER_PIC_URL", photo);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            finish();
        }
    }

    public void showProgressBar() {
        layout.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        layout.setBackgroundColor(getResources().getColor(android.R.color.white));
        progressBar.setVisibility(View.GONE);
    }
}
