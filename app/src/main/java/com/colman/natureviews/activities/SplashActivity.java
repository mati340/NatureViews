package com.colman.natureviews.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import com.colman.natureviews.R;
import com.colman.natureviews.Utils;

public class SplashActivity extends AppCompatActivity {

    ImageView backgroundImageView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.main_progress_bar);
        backgroundImageView = findViewById(R.id.main_background_image_view);
        Utils.animateBackground(backgroundImageView, 3500);

        new Thread() {
            public void run()
            {
                try {
                    //Display for 3 seconds
                    sleep(3000);
                } catch (InterruptedException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    //Goes to login
                    toLoginPage();
                }
            }
        }.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void toLoginPage(){
        Intent intent = new Intent(this, LoginPageActivity.class);
        startActivity(intent);
    }

}