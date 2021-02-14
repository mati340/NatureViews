package com.colman.natureviews.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.colman.natureviews.R;
import com.colman.natureviews.model.Model;
import com.colman.natureviews.model.Post;
import com.squareup.picasso.Picasso;
import java.util.LinkedList;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;



public class UserPostsFragment extends Fragment {

    RecyclerView list;
    List<Post> data = new LinkedList<>();
    UserPostsAdapter adapter;
    UserPostsViewModel viewModel;
    LiveData<List<Post>> liveData;

    public interface Delegate{
        void onItemSelect(Post post);
    }

    Delegate parent;

    public UserPostsFragment() {
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

        viewModel = new ViewModelProvider(this).get(UserPostsViewModel.class);
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

        adapter = new UserPostsAdapter();
        list.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Post post = data.get(position);
                parent.onItemSelect(post);
            }
        });

        liveData = viewModel.getData();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                List<Post> reversedData = reverseData(posts);
                data = reversedData;
                adapter.notifyDataSetChanged();
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.feed_list_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refresh(new Model.CompListener() {
                    @Override
                    public void onComplete() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });

        return view;
    }

    private List<Post> reverseData(List<Post> posts) {
        List<Post> reversedData = new LinkedList<>();
        for (Post post: posts) {
            reversedData.add(0, post);
        }
        return reversedData;
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
        CircleImageView userProfilePic;
        ProgressBar progressBar;
        Post post;

        public PostRowViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.row_post_title_text_view);
            postImg = itemView.findViewById(R.id.row_post_image_view);
            username = itemView.findViewById(R.id.row_username_text_view);
            userProfilePic = itemView.findViewById(R.id.row_profile_image_view);
            progressBar = itemView.findViewById(R.id.row_post_progress_bar);

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
            post = postToBind;
            if (postToBind.postImgUrl != null && postToBind.userProfileImageUrl != null){
                Picasso.get().load(postToBind.postImgUrl).noPlaceholder().into(postImg);
                Picasso.get().load(postToBind.userProfileImageUrl).noPlaceholder().into(userProfilePic);
            }
            else {
                postImg.setImageResource(R.drawable.profile_pic_placeholder);
                userProfilePic.setImageResource(R.drawable.profile_pic_placeholder);
            }
        }
    }

    interface OnItemClickListener {
        void onClick(int position);
    }

    class UserPostsAdapter extends RecyclerView.Adapter<PostRowViewHolder>{

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
