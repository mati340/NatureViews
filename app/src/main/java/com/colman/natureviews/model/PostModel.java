package com.colman.natureviews.model;

import androidx.lifecycle.LiveData;
import java.util.LinkedList;
import java.util.List;

public class PostModel {

    public static final PostModel instance = new PostModel();
    List<Post> data = new LinkedList<>();

    private PostModel(){
        for (int i = 0; i < 5; i ++){
            Post newPost = new Post("" + i, "title " + i, "content " + i, null, "id "+i, null, "username " + i);
            addPost(newPost);
        }
    }

    public void addPost(Post post){
        data.add(post);
    }

    public List<Post> getAllPosts(){
        return data;
    }

}