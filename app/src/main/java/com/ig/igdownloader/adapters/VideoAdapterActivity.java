package com.ig.igdownloader.adapters;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ig.igdownloader.R;
import com.ig.igdownloader.activities.PlayVideoActivity;
import com.ig.igdownloader.datahold.DataUser;

import java.io.File;
import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;

public class VideoAdapterActivity extends RecyclerView.Adapter<VideoAdapterActivity.ViewHolder> {
    ArrayList<DataUser> dataUsers;
    Context context;
    long videoId = 0;

    public VideoAdapterActivity(ArrayList<DataUser> dataUsers, Context context) {
        this.dataUsers = dataUsers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_activity, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView view = holder.imageView;
        Glide.with(context).load(dataUsers.get(position).getThumbnailURL()).into(view);

        Runnable r = () -> holder.downloadBtn.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();

            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 127);
            } else if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                String videoTitle = dataUsers.get(position).getVideoUrl().substring(55).split("_n.mp4")[0] + "_n.mp4";
                File file = new File(Environment.getExternalStorageDirectory() + "/IGDownloader");
                File file1 = new File(file, videoTitle);
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(dataUsers.get(position).getVideoUrl()))
                        .setDescription(videoTitle)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
                        .setDestinationUri(Uri.fromFile(file1))
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setTitle(videoTitle);
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                videoId = downloadManager.enqueue(request);
                Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show();
            }
        });
        Thread t = new Thread(r);
        t.start();

        holder.playBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlayVideoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("play_link", dataUsers.get(position).getVideoUrl());
            context.startActivity(intent);

        });
    }


    @Override
    public int getItemCount() {
        return dataUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        Button downloadBtn;
        AppCompatButton playBtn;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.card_view);
            this.downloadBtn = itemView.findViewById(R.id.btn);
            this.imageView = itemView.findViewById(R.id.image_view);
            this.playBtn = itemView.findViewById(R.id.play_button);
        }
    }
}