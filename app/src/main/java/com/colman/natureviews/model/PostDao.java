package com.colman.natureviews.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;



@Dao
public interface PostDao {

    @Query("select * from Post")
    LiveData<List<Post>> getAllPosts();

    @Query("select * from Post where userId = :userId")
    LiveData<List<Post>> getAllPostsByUserId(String userId);

    //inserting and updating
    //... is used when we don't know how many arguments will pass..it can be 0 posts, 1 or more...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllPosts(Post...posts);

    @Delete
    void deletePost(Post post);

    @Query("select exists(select * from Post where postId = :postId)")
    boolean isPostExists(String postId);

    @Query("delete from Post where postId = :postId")
    void deleteByPostId(String postId);
}