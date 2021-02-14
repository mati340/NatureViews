package com.colman.natureviews.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;


@Dao
public interface CommentDao {

    @Query("select * from Comment")
    LiveData<List<Comment>> getAllComments();

    @Query("select * from Comment where postId = :postId")
    LiveData<List<Comment>> getAllCommentsPerPost(String postId);

    //inserting and updating
    //... is used when we don't know how many arguments will pass..it can be 0 posts, 1 or more...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllComments(Comment...comments);

    @Delete
    void deleteComment(Comment comment);

    @Query("delete from Comment where postId = :postId")
    void deleteAllCommentsByPostId(String postId);

    @Query("select exists(select * from Comment where commentId = :commentId)")
    boolean isCommentExists(String commentId);

    @Query("delete from Comment where commentId = :commentId")
    void deleteByCommentId(String commentId);
}