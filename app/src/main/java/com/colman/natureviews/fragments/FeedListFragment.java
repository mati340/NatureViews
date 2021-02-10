package com.colman.natureviews.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.colman.natureviews.R;
import com.colman.natureviews.model.Post;
import com.colman.natureviews.model.PostModel;

import java.util.List;

public class FeedListFragment extends Fragment {

    RecyclerView list;
    List<Post> data;

    public interface Delegate{
        void onItemSelected(Post post);
    }

    Delegate parent;

    public FeedListFragment() {
        data = PostModel.instance.getAllPosts();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Delegate){
            parent = (Delegate) getActivity();
        }
        else {
            throw new RuntimeException(context.toString() + " must implement Delegate");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed_list, container, false);

        list = view.findViewById(R.id.feed_list_list);
        list.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);

        FeedListAdapter adapter = new FeedListAdapter();
        list.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG", "Row was clicked" + position);
                Post post = data.get(position);
                parent.onItemSelected(post);
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parent = null;
    }

    static class PostRowViewHolder extends RecyclerView.ViewHolder {

        TextView postTitle;
        ImageView postImg;
        TextView username;
        ImageView userProfilePic;
        Post post;

        public PostRowViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.row_post_title_text_view);
            postImg = itemView.findViewById(R.id.row_post_image_view);
            username = itemView.findViewById(R.id.row_username_text_view);
            userProfilePic = itemView.findViewById(R.id.row_profile_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onClick(position);
                    }
                }
            });
        }

        public void bind(Post postToBind){
            postTitle.setText(postToBind.postTitle);
            username.setText(postToBind.username);
            //implement images
            post = postToBind;
        }
    }

    interface OnItemClickListener {
        void onClick(int position);
    }

    class FeedListAdapter extends RecyclerView.Adapter<PostRowViewHolder>{

        private OnItemClickListener listener;

        void setOnClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public PostRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_row, parent, false);
            PostRowViewHolder postRowViewHolder = new PostRowViewHolder(view, listener);
            return postRowViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull PostRowViewHolder holder, int position) {
            Post post = data.get(position);
            holder.bind(post);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
