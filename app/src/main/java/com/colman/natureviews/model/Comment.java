package com.colman.natureviews.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;


@Entity
public class Comment implements Serializable {
    @PrimaryKey
    @NonNull
    public String commentId;
    public String postId;
    public String commentContent;
    public String userId;
    public String userProfileImageUrl;
    public String name;
    public long lastUpdated;

    public Comment() {
        commentId = "";
        postId = "";
        commentContent = "";
        userId = "";
        userProfileImageUrl = "";
        name = "";
        lastUpdated = 0;
    }

/*    public Comment(String commentId, String postId, String commentContent, String userId, String userProfileImageUrl, String username) {
        this.commentId = commentId;
        this.postId = postId;
        this.commentContent = commentContent;
        this.userId = userId;
        this.userProfileImageUrl = userProfileImageUrl;
        this.username = username;
    }*/

    @NonNull
    public String getPostId() {
        return postId;
    }

    public void setPostId(@NonNull String postId) {
        this.postId = postId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
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
