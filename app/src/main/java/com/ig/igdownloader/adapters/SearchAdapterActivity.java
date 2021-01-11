package com.ig.igdownloader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ig.igdownloader.R;
import com.ig.igdownloader.datahold.UserDetails;

import java.util.ArrayList;

public class SearchAdapterActivity extends RecyclerView.Adapter<SearchAdapterActivity.ViewHolder> {
    private static final String TAG = "SearchAdapterActivity";
    private final ViewHolder.OnItemClick onItemClick;
    ArrayList<UserDetails> users;
    Context context;

    public SearchAdapterActivity(ArrayList<UserDetails> users, Context context, ViewHolder.OnItemClick onItemClick) {
        this.users = users;
        this.context = context;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public SearchAdapterActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_holder_activity, parent, false);

        return new ViewHolder(view, onItemClick);
    }


    @Override
    public void onBindViewHolder(@NonNull SearchAdapterActivity.ViewHolder holder, int position) {

        ImageView view = holder.cardSmallPic;
        holder.userFullName.setText(users.get(position).getUserFullName());
        holder.userName.setText(users.get(position).getUserName());
        Glide.with(context).load(users.get(position).getUserPic()).into(view);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView cardSmallPic;
        CardView cardContainer;
        TextView userFullName;
        TextView userName;
        OnItemClick onItemClick;

        public ViewHolder(@NonNull View itemView, OnItemClick onItemClick) {
            super(itemView);
//            this.cardContainer = itemView.findViewById(R.id.card_container);
            this.userFullName = itemView.findViewById(R.id.card_fullname);
            this.userName = itemView.findViewById(R.id.card_username);
            this.cardSmallPic = itemView.findViewById(R.id.card_user_pic);
            this.onItemClick = onItemClick;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClick.onItemClickListener(getAdapterPosition());
        }

        public interface OnItemClick {
            void onItemClickListener(int pos);
        }
    }
}