package com.ig.igdownloader.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ig.igdownloader.R;
import com.ig.igdownloader.activities.AboutActivity;
import com.ig.igdownloader.activities.SlideVideoActivity;
import com.ig.igdownloader.datahold.DataUser;
import com.ig.igdownloader.nakul.Endpoints;
import com.ig.igdownloader.nakul.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Objects;

import static android.content.Context.DOWNLOAD_SERVICE;

public class MainActivityFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MainActivityFragment";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 128;
    AlertDialog alertDialog;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private EditText userInput;
    private long videoId = 0;
    private MainActivityFragment context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = this;
        toolbar = view.findViewById(R.id.custom_tool_main_activity);
        toolbar.setTitle(R.string.app_name);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        progressBar = view.findViewById(R.id.main_progress_bar);
        userInput = view.findViewById(R.id.user_link);

        progressBar.setVisibility(View.GONE);
        view.findViewById(R.id.search_button_main).setOnClickListener(this);

        if (getClipboard().contains("https://www.instagram.com")) {
            setLayout(view, view1 -> alertDialog.dismiss(), view12 -> {
                if (getClipboard().contains("https://www.instagram.com")) {
                    checkPermissions(getClipboard());
                    alertDialog.dismiss();
                }
            });
            Utils.displaySize(Objects.requireNonNull(getActivity()).getWindowManager(), alertDialog);
        } else {
            Log.d(TAG, "onViewCreated: +ex");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.about_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.about) {
            Intent intent = new Intent(getContext(), AboutActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private String getClipboard() {
        String URL;
        ClipboardManager clipboardManager = (ClipboardManager) Objects.requireNonNull(getContext()).getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = clipboardManager.getPrimaryClip();
        if (data == null) {
            Log.d(TAG, "clipboard is null");

        } else if (data.toString().contains("https://www.instagram.com/")) {
            URL = data.toString();
            URL = URL.split("\\?")[0];
            URL = URL.substring(46).trim();
            Log.d(TAG, "" + URL);
            return URL;
        }
        return "URL";
    }

    private void checkPermissions(String url) {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 128);
            Log.d(TAG, "checkingPermissionsFun");
        }
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            alertDialogVideo(url);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Utils.checkFolder();
            } else {
                for (String permission : permissions) {
                    boolean showRationale = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission);
                        Toast.makeText(getContext(), "Permission required", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onRequestPermissionResult");
                    }
                    if (!showRationale) {
                        Utils.alertDialog(getContext());
                    }
                }
            }
        }
    }

    @SuppressWarnings("ParameterCanBeLocal")
    private void setLayout(View view, View.OnClickListener cancelBtn, View.OnClickListener btnOk) {
        if (getClipboard().contains("https://www.instagram.com")) {
            view = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(view);
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
            view.findViewById(R.id.cancel).setOnClickListener(cancelBtn);
            view.findViewById(R.id.button_ok).setOnClickListener(btnOk);
        } else if (!getClipboard().contains("https://www.instagram.com")) {
            Toast.makeText(getContext(), "Clipboard doesn't contain", Toast.LENGTH_SHORT).show();
        }
    }

    private void alertDialogVideo(String url) {
        JsonObjectRequest objectRequest = new JsonObjectRequest(0, Endpoints.INSTAGRAM_VIDEOS_ENDPOINT + Endpoints.INSTAGRAM_VIDEO_PATH + url
                , null, response -> {
            DataUser dataUser = new DataUser(null, null);
            try {
                JSONObject ob = new JSONObject(response.toString());
                int j = ob.getInt("no_of_videos");
                if (j == 1) {
                    JSONArray js = ob.getJSONArray("info");
                    for (int i = 0; i < js.length(); i++) {
                        JSONObject ob1 = js.getJSONObject(i);
                        String video_url = ob1.getString("video_url");
                        dataUser.data(video_url);
                        Log.d(TAG, video_url);
                    }
                    downloadManager(dataUser.data());
                } else if (j >= 2) {
                    Intent intent = new Intent(getContext(), SlideVideoActivity.class);
                    intent.putExtra("video_url", url);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::getMessage);
        Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(objectRequest);
    }

    private void parsingURL(String URL) {
        String parsedURL = URL.split("\\?")[0];
        progressBar.setVisibility(View.VISIBLE);
        JsonObjectRequest objectRequest = new JsonObjectRequest(0, Endpoints.INSTAGRAM_VIDEOS_ENDPOINT +
                Endpoints.INSTAGRAM_VIDEO_PATH + parsedURL, null, response -> {
            DataUser dataUser = new DataUser(null, null);
            JSONObject object;
            try {
                object = new JSONObject(response.toString());
                int j = response.getInt("no_of_videos");

                if (j == 1) {
                    JSONArray info = object.getJSONArray("info");
                    for (int i = 0; i < info.length(); i++) {
                        JSONObject ob = info.getJSONObject(i);
                        String url = ob.getString("video_url");
                        dataUser.data(url);
                    }
                    Intent intent = new Intent(getContext(), SlideVideoActivity.class);
                    intent.putExtra("video_url", URL);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);

                }
                if (j >= 2) {
                    Intent intent = new Intent(getContext(), SlideVideoActivity.class);
                    intent.putExtra("video_url", URL);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
        }, Throwable::getMessage);
        Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(objectRequest);
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
        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
        videoId = downloadManager.enqueue(request);
        Toast.makeText(getContext(), "Downloading", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        int f = v.getId();
        if (f == R.id.search_button_main) {
            if (userInput.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Paste link", Toast.LENGTH_SHORT).show();
            }
            parsingURL(userInput.getText().toString());
        }
    }
}