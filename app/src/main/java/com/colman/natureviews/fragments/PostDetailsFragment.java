package com.colman.natureviews.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import com.colman.natureviews.R;
import com.colman.natureviews.model.Model;
import com.colman.natureviews.model.Post;
import com.colman.natureviews.model.StoreModel;
import com.colman.natureviews.model.User;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;



public class PostDetailsFragment extends Fragment {

    Post post;
    View view;
    TextView postDescription;
    TextView name;
    Button comments;
    ImageView postImg;
    ImageButton closeBtn;
    ImageButton editPostBtn;
    TextView editPostText;
    ImageButton deletePostBtn;
    TextView deletePostText;
    CircleImageView profilePic;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_details, container, false);
        postDescription = view.findViewById(R.id.post_details_fragment_post_description_text_view);
        name = view.findViewById(R.id.post_details_fragment_name_text_view);
        comments = view.findViewById(R.id.post_details_fragment_comments_btn);
        postImg = view.findViewById(R.id.post_details_fragment_post_image_view);
        profilePic = view.findViewById(R.id.post_details_fragment_profile_image_view);

        post = PostDetailsFragmentArgs.fromBundle(getArguments()).getPost();
        if (post != null) {
            postDescription.setText(post.postDescription);
            name.setText(post.name);
            if (post.postImgUrl != null && post.userProfileImageUrl != null){
                Picasso.get().load(post.postImgUrl).noPlaceholder().into(postImg);
                Picasso.get().load(post.userProfileImageUrl).noPlaceholder().into(profilePic);
            }
            else {
                postImg.setImageResource(R.drawable.profile_placeholder);
                profilePic.setImageResource(R.drawable.profile_placeholder);
            }


            comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toCommentsPage(post);
                }
            });

            closeBtn = view.findViewById(R.id.post_details_fragment_close_btn);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavController navCtrl = Navigation.findNavController(view);
                    navCtrl.popBackStack();
                }
            });

            editPostBtn = view.findViewById(R.id.post_details_fragment_edit_btn);
            editPostBtn.setVisibility(View.INVISIBLE);
            deletePostBtn = view.findViewById(R.id.post_details_fragment_delete_btn);
            deletePostBtn.setVisibility(View.INVISIBLE);
            editPostText = view.findViewById(R.id.post_details_fragment_edit_text_view);
            editPostText.setVisibility(View.INVISIBLE);
            deletePostText = view.findViewById(R.id.post_details_fragment_delete_text_view);
            deletePostText.setVisibility(View.INVISIBLE);

            if (post.userId.equals(User.getInstance().userId)) {

                editPostBtn.setVisibility(View.VISIBLE);
                editPostText.setVisibility(View.VISIBLE);
                editPostBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toEditPostPage(post);
                    }
                });

                deletePostBtn.setVisibility(View.VISIBLE);
                deletePostText.setVisibility(View.VISIBLE);
                deletePostBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deletePost(post);
                    }
                });
            }
        }

        return view;
    }

    private void toEditPostPage(Post post) {
        NavController navCtrl = Navigation.findNavController(getActivity(), R.id.home_nav_host);
        PostDetailsFragmentDirections.ActionPostDetailsFragmentToEditPostFragment directions = PostDetailsFragmentDirections.actionPostDetailsFragmentToEditPostFragment(post);
        navCtrl.navigate(directions);
    }

    private void toCommentsPage(Post post){
        NavController navCtrl = Navigation.findNavController(getActivity(), R.id.home_nav_host);
        PostDetailsFragmentDirections.ActionPostDetailsFragmentToCommentListFragment directions = PostDetailsFragmentDirections.actionPostDetailsFragmentToCommentListFragment(post);
        navCtrl.navigate(directions);
    }


    void deletePost(Post postToDelete){

        Model.instance.deletePost(postToDelete, new Model.Listener<Boolean>() {
            @Override
            public void onComplete(Boolean data) {
                StoreModel.deleteImage(post.postImgUrl, new StoreModel.Listener() {
                    @Override
                    public void onSuccess(String url) {
                        NavController navCtrl = Navigation.findNavController(view);
                        navCtrl.navigateUp();
                    }
                    @Override
                    public void onFail() {
                        Snackbar.make(view, "Failed to create post and save it in databases", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}