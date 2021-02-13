package com.colman.natureviews.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.colman.natureviews.model.Model;
import com.colman.natureviews.model.Post;
import java.util.List;



public class FeedListViewModel extends ViewModel {
    LiveData<List<Post>> liveData;

    public LiveData<List<Post>> getData(){
        if (liveData == null)
            liveData = Model.instance.getAllPosts();
        return liveData;
    }

    public void refresh(Model.CompListener listener){
        Model.instance.refreshPostsList(listener);
    }
}
