package com.colman.natureviews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.colman.natureviews.activities.LoginPageActivity;

public class MainActivity extends AppCompatActivity {

    ImageView backgroundImageView;
    Button exploreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting background, animation and button to login
        backgroundImageView = findViewById(R.id.main_background_image_view);

        Utils.animateBackground(backgroundImageView,3500);

        exploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLoginPage();
            }
        });
    }

    private void toLoginPage(){
        Intent intent = new Intent(this, LoginPageActivity.class);
        startActivity(intent);
    }

}