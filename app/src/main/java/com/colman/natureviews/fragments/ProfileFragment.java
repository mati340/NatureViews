package com.colman.natureviews.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import com.colman.natureviews.R;
import com.colman.natureviews.Utils;
import com.colman.natureviews.model.User;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;



public class ProfileFragment extends Fragment {

    TextView userUsername;
    TextView userEmail;
    TextView userInfo;
    CircleImageView userProfileImage;
    ImageButton editProfileBtn;
    ImageView backgroundImageView;
    Button showPostsBtn;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        backgroundImageView = view.findViewById(R.id.profile_fragment_background_image_view);
        userUsername = view.findViewById(R.id.profile_fragment_username_text_view);
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
                toUserPostsPage();
            }
        });

        Utils.animateBackground(backgroundImageView, 30000);
        setUserProfile();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    private void toEditProfilePage() {
        NavController navCtrl = Navigation.findNavController(getActivity(), R.id.home_nav_host);
        NavDirections directions = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment();
        navCtrl.navigate(directions);
    }

    private void toUserPostsPage() {
        NavController navCtrl = Navigation.findNavController(getActivity(), R.id.home_nav_host);
        NavDirections directions = ProfileFragmentDirections.actionProfileFragmentToUserPostsFragment();
        navCtrl.navigate(directions);
    }

    public void setUserProfile(){
        userUsername.setText(User.getInstance().userUsername);
        userEmail.setText(User.getInstance().userEmail);
        userInfo.setText(User.getInstance().userInfo);

        if (User.getInstance().profileImageUrl != null){
            Picasso.get().load(User.getInstance().profileImageUrl).noPlaceholder().into(userProfileImage);
        }
    }

}