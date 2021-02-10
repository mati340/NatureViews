package com.colman.natureviews.fragments;

import android.os.Bundle;

import androidx.core.app.Person;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.colman.natureviews.R;
import com.colman.natureviews.model.Post;

public class PostDetailsFragment extends Fragment {

    Post post;
    TextView postTitle;
    TextView username;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        postTitle = view.findViewById(R.id.post_details_fragment_title_text_view);
        username = view.findViewById(R.id.post_details_fragment_username_text_view);

        post = PostDetailsFragmentArgs.fromBundle(getArguments()).getPost();
        if (post != null) {
            postTitle.setText(post.postTitle);
            username.setText(post.username);
            postContent.setText(post.postContent);
        }

        ImageButton closeBtn = view.findViewById(R.id.post_details_fragment_close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navCtrl = Navigation.findNavController(view);
                navCtrl.popBackStack();
            }
        });
        return view;
    }

}