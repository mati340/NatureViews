package com.colman.natureviews.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.colman.natureviews.NatureViewsApplication;
import java.util.List;



public class Model {

    public static final com.colman.natureviews.model.Model instance = new com.colman.natureviews.model.Model();

    public interface Listener<T>{
        void onComplete(T data);
    }
    public interface CompListener{
        void onComplete();
    }

    private Model(){
    }

    @SuppressLint("StaticFieldLeak")
    public void addComment(final Comment comment, Listener<Boolean> listener) {
        ModelFirebase.addComment(comment,listener);
        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.commentDao().insertAllComments(comment);
                return "";
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void editComment(final Comment comment, Listener<Boolean> listener) {
        ModelFirebase.editComment(comment,listener);
        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.commentDao().insertAllComments(comment);
                return "";
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteComment(final Comment comment, Listener<Boolean> listener){
        ModelFirebase.deleteComment(comment,listener);
        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.commentDao().deleteComment(comment);
                return "";
            }
        }.execute();
    }

    public void refreshCommentsList(String postId, final CompListener listener){
        long lastUpdated = NatureViewsApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("CommentsLastUpdateDate",0);
        ModelFirebase.getAllCommentsSince(lastUpdated, postId,new Listener<List<Comment>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Comment> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        for(Comment c: data){
                            AppLocalDb.db.commentDao().insertAllComments(c);
                            if (c.lastUpdated > lastUpdated)
                                lastUpdated = c.lastUpdated;
                        }
                        SharedPreferences.Editor edit = NatureViewsApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
                        edit.putLong("CommentsLastUpdateDate",lastUpdated);
                        edit.commit();
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        cleanLocalCommentDb();
                        if (listener!=null)
                            listener.onComplete();
                    }
                }.execute("");
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void cleanLocalCommentDb(){
        ModelFirebase.getDeletedCommentsId(new Listener<List<String>>() {
            @Override
            public void onComplete(final List<String> data) {
                new AsyncTask<String,String,String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        for (String id: data){
                            Log.d("TAG", "deleted id: " + id);
                            AppLocalDb.db.commentDao().deleteByCommentId(id);
                        }
                        return "";
                    }
                }.execute("");
            }
        });
    }

    public LiveData<List<Comment>> getAllComments(){
        LiveData<List<Comment>> liveData = AppLocalDb.db.commentDao().getAllComments();
        refreshPostsList(null);
        return liveData;
    }

    public LiveData<List<Comment>> getAllCommentsPerPost(String postId){
        LiveData<List<Comment>> liveData = AppLocalDb.db.commentDao().getAllCommentsPerPost(postId);
        refreshPostsList(null);
        return liveData;
    }

    @SuppressLint("StaticFieldLeak")
    public void addPost(final Post post, Listener<Boolean> listener) {
        ModelFirebase.addPost(post,listener);
        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.postDao().insertAllPosts(post);
                return "";
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deletePost(final Post post, Listener<Boolean> listener){
        ModelFirebase.deletePost(post,listener);
        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... strings) {
                AppLocalDb.db.postDao().deletePost(post);
                AppLocalDb.db.commentDao().deleteAllCommentsByPostId(post.postId);
                return "";
            }
        }.execute();
    }

    public void refreshPostsList(final CompListener listener){
        long lastUpdated = NatureViewsApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("PostsLastUpdateDate",0);
        ModelFirebase.getAllPostsSince(lastUpdated,new Listener<List<Post>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onComplete(final List<Post> data) {
                new AsyncTask<String,String,String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        long lastUpdated = 0;
                        for(Post p: data){
                            AppLocalDb.db.postDao().insertAllPosts(p);
                            if (p.lastUpdated > lastUpdated)
                                lastUpdated = p.lastUpdated;
                        }
                        SharedPreferences.Editor edit = NatureViewsApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
                        edit.putLong("PostsLastUpdateDate",lastUpdated);
                        edit.commit();
                        return "";
                    }
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        cleanLocalDb();
                        if (listener!=null)
                            listener.onComplete();
                    }
                }.execute("");
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void cleanLocalDb(){
        ModelFirebase.getDeletedPostsId(new Listener<List<String>>() {
            @Override
            public void onComplete(final List<String> data) {
                new AsyncTask<String,String,String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        for (String id: data){
                            Log.d("TAG", "deleted id: " + id);
                            AppLocalDb.db.postDao().deleteByPostId(id);
                        }
                        return "";
                    }
                }.execute("");
            }
        });
    }

    public LiveData<List<Post>> getAllPosts(){
        LiveData<List<Post>> liveData = AppLocalDb.db.postDao().getAllPosts();
        refreshPostsList(null);
        return liveData;
    }

    public LiveData<List<Post>> getAllPostsByUserId(String userId){
        LiveData<List<Post>> liveData = AppLocalDb.db.postDao().getAllPostsByUserId(userId);
        refreshPostsList(null);
        return liveData;
    }

    public void updateUserProfile(String name, String info, String profileImgUrl, Listener<Boolean> listener) {
        ModelFirebase.updateUserProfile(name, info, profileImgUrl, listener);
    }

    public void setUserAppData(String email){
        ModelFirebase.setUserAppData(email);
    }


}
