package com.example.shakingparrot.data;

import com.google.gson.annotations.SerializedName;

public class VideoSource {

    @SerializedName("_id")
    public String id;
    @SerializedName("feedurl")
    public String url;
    @SerializedName("nickname")
    public String nickName;
    @SerializedName("description")
    public String description;
    @SerializedName("likecount")
    public int likeCount;
    @SerializedName("avatar")
    public String avatar;


    public VideoSource(String id, String url, String description, String nickName, int likeCount, String avatar) {
        this.id = id;
        this.url = url;
        this.nickName = nickName;
        this.description = description;
        this.likeCount = likeCount;
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getNickName() {
        return nickName;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getAvatar() {
        return avatar;
    }


}
