package com.colman.natureviews.activities;
import com.colman.natureviews.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.colman.natureviews.Utils;


public class RegisterPageActivity extends AppCompatActivity {

    ImageView backgroundImageView;
    EditText usernameInput;
    EditText passwordInput;
    EditText emailInput;
    Button registerBtn;
    ProgressBar progressBar;
    ImageButton closeBtn;
    Uri profileImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        this.setTitle("Sign Up");

        usernameInput = findViewById(R.id.register_activity_username_edit_text);
        passwordInput = findViewById(R.id.register_activity_password_edit_text);
        emailInput = findViewById(R.id.register_activity_email_edit_text);


        backgroundImageView = findViewById(R.id.register_activity_background_image_view);
        Utils.animateBackground(backgroundImageView, 30000);


        progressBar = findViewById(R.id.register_activity_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        registerBtn = findViewById(R.id.register_activity_register_btn);



        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterPageActivity.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData() != null && data != null){
            profileImageUri = data.getData();
        }
        else {
            Toast.makeText(this, "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }
}