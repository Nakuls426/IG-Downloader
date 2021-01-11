package com.ig.igdownloader.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ig.igdownloader.R;
import com.ig.igdownloader.activities.ImageDownloader;
import com.ig.igdownloader.adapters.SearchAdapterActivity;
import com.ig.igdownloader.datahold.UserDetails;
import com.ig.igdownloader.nakul.Endpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class PhotoActivityFragment extends Fragment implements SearchAdapterActivity.ViewHolder.OnItemClick {
    private static final String TAG = "PhotoActivityFragment";
    private EditText userInput;
    private RecyclerView holdUserRecycler;
    private ImageButton searchBtn;
    private RecyclerView.Adapter adapter;
    private ArrayList<UserDetails> addData;
    private Toolbar toolbar;
    private ProgressBar showProgress;
    private ImageView graphicImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_downloader, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar = view.findViewById(R.id.custom_tool_profile_pic);
        toolbar.setTitle(R.string.picture_downloader);
        userInput = view.findViewById(R.id.user_edit);
        searchBtn = view.findViewById(R.id.search_photo);
        showProgress = view.findViewById(R.id.photo_progress_bar);
        graphicImage = view.findViewById(R.id.graphic_image);

        showProgress.setVisibility(View.GONE);
        holdUserRecycler = view.findViewById(R.id.photo_recycler_view);
        holdUserRecycler.setHasFixedSize(true);
        holdUserRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        addData = new ArrayList<>();
        searchBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(userInput.getText().toString())) {
                Toast.makeText(getContext(), "Enter username", Toast.LENGTH_SHORT).show();
            }
            queryUsers(userInput.getText().toString());
        });
    }

    private void queryUsers(String query) {
        showProgress.setVisibility(View.VISIBLE);
        JsonObjectRequest objectRequest = new JsonObjectRequest(0, Endpoints.INSTAGRAM_SEARCH_USER + Endpoints.INSTAGRAM_QUERY_USER + query, null, response -> {
            try {
                JSONObject ob = new JSONObject(response.toString());
                JSONArray ar = ob.getJSONArray("users");
                addData.clear();
                for (int i = 0; i < ar.length(); i++) {
                    JSONObject object = ar.getJSONObject(i);
                    JSONObject object1 = object.getJSONObject("user");
                    UserDetails userDetails = new UserDetails(object1.getString("profile_pic_url"),
                            object1.getString("username"),
                            object1.getString("full_name"), object1.getString("pk"), null);
                    addData.add(userDetails);
                }
                initData(addData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            showProgress.setVisibility(View.GONE);
        }, Throwable::printStackTrace);
        Volley.newRequestQueue(Objects.requireNonNull(getContext())).add(objectRequest);
    }

    private void initData(ArrayList<UserDetails> userDetails) {
        adapter = new SearchAdapterActivity(userDetails, getContext(), this);
        holdUserRecycler.scheduleLayoutAnimation();
        holdUserRecycler.setAdapter(adapter);
        if (holdUserRecycler != null) {
            Objects.requireNonNull(getView()).findViewById(R.id.graphic_image).setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClickListener(int pos) {
        Intent intent = new Intent(getContext(), ImageDownloader.class);
        intent.putExtra("userName", addData.get(pos).getUserName());
        intent.putExtra("userFullName", addData.get(pos).getUserFullName());
        intent.putExtra("pk", addData.get(pos).getUserPk());
        startActivity(intent);
    }
}