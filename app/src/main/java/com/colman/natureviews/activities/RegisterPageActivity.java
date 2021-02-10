package com.colman.natureviews.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.colman.natureviews.R;
import com.colman.natureviews.Utils;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterPageActivity extends AppCompatActivity {

    //XML views
    ImageView backgroundImageView;
    EditText usernameInput;
    EditText passwordInput;
    EditText emailInput;
    Button registerBtn;
    CircleImageView profileImageView;

    //Firebase
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;

    //for opening gallery
    Uri profileImageUrl;
    static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        usernameInput = findViewById(R.id.register_activity_username_edit_text);
        passwordInput = findViewById(R.id.register_activity_password_edit_text);
        emailInput = findViewById(R.id.register_activity_email_edit_text);

        backgroundImageView = findViewById(R.id.register_activity_background_image_view);
        Utils.animateBackground(backgroundImageView);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImageFromGallery();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("images");

        registerBtn = findViewById(R.id.register_activity_register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUserAccount();
            }
        });
    }

    private void registerUserAccount(){

        if (firebaseAuth.getCurrentUser() != null){
            firebaseAuth.signOut();
        }
        if (firebaseAuth.getCurrentUser() == null && !usernameInput.getText().toString().isEmpty() && !passwordInput.getText().toString().isEmpty() && !emailInput.getText().toString().isEmpty()){
            Toast.makeText(this, "Registering the user...", Toast.LENGTH_SHORT).show();
            firebaseAuth.createUserWithEmailAndPassword(emailInput.getText().toString(), passwordInput.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    uploadUserData();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterPageActivity.this, "Fails ro create user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Toast.makeText(this, "Please fill all input fields and profile image", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadUserData(){
        if (profileImageUrl != null){
            String imageName = usernameInput.getText().toString() + "." + getExtension(profileImageUrl);
            final StorageReference imageRef = storageReference.child(imageName);

            Toast.makeText(this, "Uploading user data...", Toast.LENGTH_SHORT).show();

            UploadTask uploadTask = imageRef.putFile(profileImageUrl);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){

                        Map<String,Object> data = new HashMap<>();
                        data.put("profileImageUrl", task.getResult().toString());
                        data.put("username", usernameInput.getText().toString());
                        data.put("email", emailInput.getText().toString());
                        data.put("info", "NA");
                        firebaseFirestore.collection("userProfileData").document(emailInput.getText().toString()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RegisterPageActivity.this, "User is registered", Toast.LENGTH_SHORT).show();
                                if (firebaseAuth.getCurrentUser() != null){
                                    firebaseAuth.signOut();

                                }
                                RegisterPageActivity.this.finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterPageActivity.this, "Fails to create user and upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if (!task.isSuccessful()){
                        Toast.makeText(RegisterPageActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Please choose a profile image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getExtension(Uri uri){
        try{
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

        } catch (Exception e) {
            Toast.makeText(this, "Register page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    private void chooseImageFromGallery(){

        try{
            Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            openGalleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(openGalleryIntent, REQUEST_CODE);
        }
        catch (Exception e){
            Toast.makeText(this, "Register Page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData() != null && data != null){
            profileImageUrl = data.getData();
            profileImageView.setImageURI(profileImageUrl);
        }
        else {
            Toast.makeText(this, "No image was selected", Toast.LENGTH_SHORT).show();
        }
    }
}