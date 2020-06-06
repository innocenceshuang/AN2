package com.example.shakingparrot.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shakingparrot.R;
import com.example.shakingparrot.data.VideoSource;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.CardViewHolder> {

    private List<VideoSource> videoSourceList;
    private Context context;

    public ViewPagerAdapter(List<VideoSource> videoSourceList, Context context) {
        this.videoSourceList = videoSourceList;
        this.context = context;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder
    {

        private VideoView videoPlayer;
        private TextView description;
        private SimpleDraweeView avatar;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            videoPlayer = itemView.findViewById(R.id.videoPlayer);
            description = itemView.findViewById(R.id.description);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }
    @NonNull
    @Override
    public ViewPagerAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_playing_item, parent,false);
        CardViewHolder holder = new CardViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter.CardViewHolder holder, int position) {
        VideoSource videoSource = videoSourceList.get(position);
//        Glide.with(context).load(videoSource.getAvatar()).into(holder.avatar);
        Uri uri = Uri.parse(videoSource.getAvatar());
        holder.avatar.setImageURI(uri);

        holder.description.setText(videoSource.getDescription());

//        holder.videoPlayer.setVideoURI(Uri.parse(videoSource.getUrl().replace("http","https")));
        holder.videoPlayer.setVideoURI(Uri.parse(videoSource.getUrl()));
        holder.videoPlayer.start();

        Log.d("bugReport", videoSource.getUrl());
        Log.d("bugReport", (Uri.parse(videoSource.getUrl().replace("http","https"))).toString());
    }

    @Override
    public int getItemCount() {
        return videoSourceList.size();
    }
}
