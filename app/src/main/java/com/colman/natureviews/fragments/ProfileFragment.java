package com.colman.natureviews.fragments;

import android.content.Intent;
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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.colman.natureviews.R;
import com.colman.natureviews.Utils;
import com.colman.natureviews.activities.LoginPageActivity;
import com.colman.natureviews.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;



public class ProfileFragment extends Fragment {

    TextView userName;
    TextView userEmail;
    TextView userInfo;
    CircleImageView userProfileImage;
    ImageButton editProfileBtn;
    ImageView backgroundImageView;
    Button showPostsBtn;
    Button logoutBtn;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        backgroundImageView = view.findViewById(R.id.profile_fragment_background_image_view);
        userName = view.findViewById(R.id.profile_fragment_name_text_view);
        userEmail = view.findViewById(R.id.profile_fragment_email_text_view);
        userInfo = view.findViewById(R.id.profile_fragment_info_text_view);
        userProfileImage = view.findViewById(R.id.profile_fragment_profile_image_view);

        editProfileBtn = view.findViewById(R.id.profile_fragment_edit_profile_btn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEditProfilePage();
            }
        });

        showPostsBtn = view.findViewById(R.id.profile_fragment_show_posts_btn);
        showPostsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPostsListPage();
            }
        });

        logoutBtn = view.findViewById(R.id.profile_fragment_logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLoginPage();
            }
        });

        Utils.animateBackground(backgroundImageView, 30000);
        setUserProfile();
        return view;

    }


    private void toEditProfilePage() {
        NavController navCtrl = Navigation.findNavController(getActivity(), R.id.home_nav_host);
        NavDirections directions = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment();
        navCtrl.navigate(directions);
    }

    private void toPostsListPage() {
        NavController navCtrl = Navigation.findNavController(getActivity(), R.id.home_nav_host);
        ProfileFragmentDirections.ActionProfileFragmentToFeedListFragment directions = ProfileFragmentDirections.actionProfileFragmentToFeedListFragment().setListFor("User");
        navCtrl.navigate(directions);
    }

    private void toLoginPage() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this.getActivity(), LoginPageActivity.class));
    }

    public void setUserProfile(){
        userName.setText(User.getInstance().name);
        userEmail.setText(User.getInstance().userEmail);
        userInfo.setText(User.getInstance().userInfo);

        if (User.getInstance().profileImageUrl != null){
            Picasso.get().load(User.getInstance().profileImageUrl).noPlaceholder().into(userProfileImage);
        }
    }

}