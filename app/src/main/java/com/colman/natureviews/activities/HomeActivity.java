package com.colman.natureviews.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.colman.natureviews.fragments.UserPostsFragment;
import com.colman.natureviews.fragments.UserPostsFragmentDirections;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.colman.natureviews.R;
import com.colman.natureviews.fragments.FeedListFragment;
import com.colman.natureviews.fragments.FeedListFragmentDirections;
import com.colman.natureviews.model.Post;

public class HomeActivity extends AppCompatActivity implements FeedListFragment.Delegate, UserPostsFragment.Delegate {

    NavController navCtrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navCtrl = Navigation.findNavController(this, R.id.home_nav_host);
        BottomNavigationView bottomNav = findViewById(R.id.home_bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navCtrl);
    }

    @Override
    public void onItemSelected(Post post) {
        navCtrl = Navigation.findNavController(this, R.id.home_nav_host);
        FeedListFragmentDirections.ActionFeedListFragmentToPostDetailsFragment directions = FeedListFragmentDirections.actionFeedListFragmentToPostDetailsFragment(post);
        navCtrl.navigate(directions);
    }

    @Override
    public void onItemSelect(Post post) {
        navCtrl = Navigation.findNavController(this, R.id.home_nav_host);
        UserPostsFragmentDirections.ActionUserPostsFragmentToPostDetailsFragment directions = UserPostsFragmentDirections.actionUserPostsFragmentToPostDetailsFragment(post);
        navCtrl.navigate(directions);
    }
}
