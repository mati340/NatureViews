package com.colman.natureviews.activities;

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
import com.colman.natureviews.R;
import com.colman.natureviews.Utils;
import com.colman.natureviews.model.ModelFirebase;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterPageActivity extends AppCompatActivity {

    ImageView backgroundImageView;
    EditText nameInput;
    EditText passwordInput;
    EditText emailInput;
    Button registerBtn;
    CircleImageView profileImageView;
    ProgressBar progressBar;
    ImageButton closeBtn;
    Uri profileImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        this.setTitle("Sign Up");

        nameInput = findViewById(R.id.register_activity_name_edit_text);
        passwordInput = findViewById(R.id.register_activity_password_edit_text);
        emailInput = findViewById(R.id.register_activity_email_edit_text);
        profileImageView = findViewById(R.id.register_activity_profile_image_view);

        backgroundImageView = findViewById(R.id.register_activity_background_image_view);
        Utils.animateBackground(backgroundImageView, 30000);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.chooseImageFromGallery(com.colman.natureviews.activities.RegisterPageActivity.this);
            }
        });

        progressBar = findViewById(R.id.register_activity_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        registerBtn = findViewById(R.id.register_activity_register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                ModelFirebase.registerUserAccount(nameInput.getText().toString(), passwordInput.getText().toString(), emailInput.getText().toString(), profileImageUri, new ModelFirebase.Listener<Boolean>() {
                    @Override
                    public void onComplete() {
                        progressBar.setVisibility(View.INVISIBLE);
                        com.colman.natureviews.activities.RegisterPageActivity.this.finish();
                    }

                    @Override
                    public void onFail() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        closeBtn = findViewById(R.id.register_activity_close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.colman.natureviews.activities.RegisterPageActivity.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData() != null && data != null){
            profileImageUri = data.getData();
            profileImageView.setImageURI(profileImageUri);
        }
        else {
            Toast.makeText(this, "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }
}