package com.colman.natureviews.fragments;

import androidx.lifecycle.LiveData;
import com.colman.natureviews.model.Model;
import com.colman.natureviews.model.Post;
import java.util.List;


public class FeedListViewModel extends PostsListViewModel {
    LiveData<List<Post>> liveData;

    @Override
    public LiveData<List<Post>> getData() {
        if (liveData == null)
            liveData = Model.instance.getAllPosts();
        return liveData;
    }

    @Override
    public void refresh(Model.CompListener listener) {
        Model.instance.refreshPostsList(listener);
    }
}
