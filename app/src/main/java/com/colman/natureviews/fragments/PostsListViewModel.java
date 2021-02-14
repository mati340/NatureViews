package com.colman.natureviews.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.colman.natureviews.model.Model;
import com.colman.natureviews.model.Post;

import java.util.List;

public abstract class PostsListViewModel extends ViewModel {
    public abstract LiveData<List<Post>> getData();
    public abstract void refresh(Model.CompListener listener);

}
