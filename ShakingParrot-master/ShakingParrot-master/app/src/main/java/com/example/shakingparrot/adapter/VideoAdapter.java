package com.example.shakingparrot.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shakingparrot.R;
import com.example.shakingparrot.data.VideoSource;
import com.facebook.drawee.view.SimpleDraweeView;


import java.util.List;


public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private static final String TAG = "VideoAdapter";

    private List<VideoSource> videoSourceList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClicked(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public VideoAdapter(List<VideoSource> videoSourceList, Context context) {
        this.videoSourceList = videoSourceList;
        this.context = context;
    }

    public void setData(List<VideoSource> videoSourceList)
    {
        this.videoSourceList = videoSourceList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView videoPreface;
        TextView videoDescription;
        SimpleDraweeView avatarIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoPreface = itemView.findViewById(R.id.videoPreface);
            videoDescription = itemView.findViewById(R.id.videoDescription);
            avatarIcon = (SimpleDraweeView)itemView.findViewById(R.id.avatar_icon);
        }
    }


    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.video_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        VideoSource videoSource = videoSourceList.get(position);
        holder.videoDescription.setText(videoSource.getDescription());

        Log.d(TAG, videoSource.getDescription() + ": " + videoSource.getUrl());

        holder.videoPreface.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(0)
                                .centerCrop()
                                .error(R.drawable.ic_launcher_foreground)
                                .placeholder(R.drawable.ic_launcher_background)
                )
                .load(videoSource.getUrl())
                .into(holder.videoPreface);

//        Glide.with(context).load(videoSource.getAvatar()).into(holder.avatarIcon);
        Uri uri = Uri.parse(videoSource.getAvatar());
        holder.avatarIcon.setImageURI(uri);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(holder.itemView, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoSourceList.size();
    }
}
