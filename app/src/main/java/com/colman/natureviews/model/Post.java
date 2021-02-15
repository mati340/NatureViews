package com.colman.natureviews.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;



@Entity
public class Post implements Serializable {
    @PrimaryKey
    @NonNull
    public String postId;
    public String postTitle;
    public String postContent;
    public String postImgUrl;
    public String userId;
    public String userProfileImageUrl;
    public String name;
    public long lastUpdated;

    public Post(){
        postId = "";
        postTitle = "";
        postContent = "";
        postImgUrl = "";
        userId = "";
        userProfileImageUrl = "";
        name = "";
        lastUpdated = 0;
    }

    public Post(String postId, String postTitle, String postContent, String postImgUrl, String userId, String userProfilePicUrl, String name){
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postImgUrl = postImgUrl;
        this.userId = userId;
        this.userProfileImageUrl = userProfilePicUrl;
        this.name = name;
    }

    @NonNull
    public String getPostId() {
        return postId;
    }

    public void setPostId(@NonNull String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostImgUrl() {
        return postImgUrl;
    }

    public void setPostImgUrl(String postImgUrl) {
        this.postImgUrl = postImgUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
