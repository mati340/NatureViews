package com.colman.natureviews.model;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.colman.natureviews.NatureViewsApplication;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class ModelFirebase {
    final static String POST_COLLECTION = "posts";
    final static String COMMENT_COLLECTION = "comments";

    public interface Listener<T>{
        void onComplete();
        void onFail();
    }

    public static void loginUser(final String email, String password, final Listener<Boolean> listener){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (email != null && !email.equals("") && password != null && !password.equals("")){
            if (firebaseAuth.getCurrentUser() != null) {
                firebaseAuth.signOut();
            }
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(NatureViewsApplication.context, "Welcome!", Toast.LENGTH_SHORT).show();
                    setUserAppData(email);
                    listener.onComplete();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NatureViewsApplication.context, "Failed to login: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onFail();
                }
            });
        }
        else {
            Toast.makeText(NatureViewsApplication.context, "Please fill both data fields", Toast.LENGTH_SHORT).show();
        }
    }

    public static void registerUserAccount(final String username, String password, final String email, final Uri imageUri, final Listener<Boolean> listener){

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            firebaseAuth.signOut();
        }
        if (firebaseAuth.getCurrentUser() == null &&
                username != null && !username.equals("") &&
                password != null && !password.equals("") &&
                email != null && !email.equals("") &&
                imageUri != null){
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(NatureViewsApplication.context, "User registered", Toast.LENGTH_SHORT).show();
                    uploadUserData(username, email, imageUri);
                    listener.onComplete();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NatureViewsApplication.context, "Failed registering user", Toast.LENGTH_SHORT).show();
                    listener.onFail();
                }
            });
        }
        else {
            Toast.makeText(NatureViewsApplication.context, "Please fill all input fields and profile image", Toast.LENGTH_SHORT).show();
            listener.onFail();
        }
    }

    private static void uploadUserData(final String username, final String email, Uri imageUri){

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("images");

        if (imageUri != null){
            String imageName = username + "." + getExtension(imageUri);
            final StorageReference imageRef = storageReference.child(imageName);

            UploadTask uploadTask = imageRef.putFile(imageUri);
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

                        Map<String, Object> data = new HashMap<>();
                        data.put("profileImageUrl", task.getResult().toString());
                        data.put("username", username);
                        data.put("email", email);
                        data.put("info", "I'm New Here !");
                        firebaseFirestore.collection("userProfileData").document(email).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (firebaseAuth.getCurrentUser() != null){
                                    firebaseAuth.signOut();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NatureViewsApplication.context, "Fails to create user and upload data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else if (!task.isSuccessful()){
                        Toast.makeText(NatureViewsApplication.context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(NatureViewsApplication.context, "Please choose a profile image", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getExtension(Uri uri){
        try{
            ContentResolver contentResolver = NatureViewsApplication.context.getContentResolver();
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

        } catch (Exception e) {
            Toast.makeText(NatureViewsApplication.context, "Register page: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static void getAllCommentsSince(long since, String postId, final Model.Listener<List<Comment>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(since,0);
        db.collection(COMMENT_COLLECTION).whereGreaterThanOrEqualTo("lastUpdated", ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Comment> commentsData = null;
                if (task.isSuccessful()){
                    commentsData = new LinkedList<Comment>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Map<String, Object> json = doc.getData();
                        Comment comment = commentFactory(json);
                        commentsData.add(comment);
                    }
                }
                listener.onComplete(commentsData);
            }
        });
    }

    public static void addComment(Comment comment, final Model.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COMMENT_COLLECTION).document(comment.getCommentId()).set(toJson(comment)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener!=null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    public static void editComment(Comment comment, final Model.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COMMENT_COLLECTION).document(comment.getCommentId()).update("commentContent", comment.commentContent).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener!=null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    public static void deleteComment(final Comment comment, final Model.Listener<Boolean> listener) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COMMENT_COLLECTION).document(comment.getCommentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Map<String, Object> deleted = new HashMap<>();
                deleted.put("commentId", comment.commentId);
                db.collection("deletedComments").document(comment.commentId).set(deleted).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (listener!=null){
                            listener.onComplete(task.isSuccessful());
                        }
                    }
                });
            }
        });
    }

    public static void getDeletedCommentsId(final Model.Listener<List<String>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("deletedComments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> deletedCommentsIds = null;
                if (task.isSuccessful()){
                    deletedCommentsIds = new LinkedList<String>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        String deleted = (String) doc.getData().get("commentId");
                        deletedCommentsIds.add(deleted);
                    }
                }
                listener.onComplete(deletedCommentsIds);
            }
        });
    }

    private static Comment commentFactory(Map<String, Object> json){
        Comment newComment = new Comment();
        newComment.commentId = (String) json.get("commentId");
        newComment.postId = (String) json.get("postId");
        newComment.commentContent = (String) json.get("commentContent");
        newComment.userId = (String) json.get("userId");
        newComment.userProfileImageUrl = (String) json.get("userProfileImageUrl");
        newComment.username = (String) json.get("username");

        Timestamp ts = (Timestamp)json.get("lastUpdated");
        if (ts != null)
            newComment.lastUpdated = ts.getSeconds();
        return newComment;
    }

    private static Map<String, Object> toJson(Comment comment){
        HashMap<String, Object> json = new HashMap<>();
        json.put("commentId", comment.commentId);
        json.put("postId", comment.postId);
        json.put("commentContent", comment.commentContent);
        json.put("userId", comment.userId);
        json.put("userProfileImageUrl", comment.userProfileImageUrl);
        json.put("username", comment.username);
        json.put("lastUpdated", FieldValue.serverTimestamp());
        return json;
    }

    //----------------------------------------------------------------------------------------

    public static void getAllPostsSince(long since, final Model.Listener<List<Post>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(since,0);
        db.collection(POST_COLLECTION).whereGreaterThanOrEqualTo("lastUpdated", ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> postsData = null;
                if (task.isSuccessful()){
                    postsData = new LinkedList<Post>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Map<String, Object> json = doc.getData();
                        Post posts = factory(json);
                        postsData.add(posts);
                    }
                }
                listener.onComplete(postsData);
                if(postsData != null) {
                    Log.d("TAG", "refresh " + postsData.size());
                }
            }
        });
    }

    public static void addPost(Post post, final Model.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(POST_COLLECTION).document(post.getPostId()).set(toJson(post)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener!=null){
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }

    public static void deletePost(final Post post, final Model.Listener<Boolean> listener) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COMMENT_COLLECTION).whereEqualTo("postId", post.postId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc : task.getResult()){
                    String deletedCommentId = (String) doc.getData().get("commentId");
                    doc.getReference().delete();
                    Map<String, Object> deleted = new HashMap<>();
                    deleted.put("commentId", deletedCommentId);
                    db.collection("deletedComments").document(deletedCommentId).set(deleted).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
                }
            }
        });
        db.collection(POST_COLLECTION).document(post.getPostId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Map<String, Object> deleted = new HashMap<>();
                deleted.put("postId", post.postId);
                db.collection("deleted").document(post.getPostId()).set(deleted).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (listener!=null){
                            listener.onComplete(task.isSuccessful());
                        }
                    }
                });
            }
        });
    }

    public static void getDeletedPostsId(final Model.Listener<List<String>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("deleted").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> deletedPostsIds = null;
                if (task.isSuccessful()){
                    deletedPostsIds = new LinkedList<String>();
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        String deleted = (String) doc.getData().get("postId");
                        deletedPostsIds.add(deleted);
                    }
                }
                listener.onComplete(deletedPostsIds);
            }
        });
    }

    private static Post factory(Map<String, Object> json){
        Post newPost = new Post();
        newPost.postId = (String) json.get("postId");
        newPost.postTitle = (String) json.get("postTitle");
        newPost.postContent = (String) json.get("postContent");
        newPost.postImgUrl = (String) json.get("postImgUrl");
        newPost.userId = (String) json.get("userId");
        newPost.userProfileImageUrl = (String) json.get("userProfilePicUrl");
        newPost.username = (String) json.get("username");
        Timestamp ts = (Timestamp)json.get("lastUpdated");
        if (ts != null)
            newPost.lastUpdated = ts.getSeconds();
        return newPost;
    }

    private static Map<String, Object> toJson(Post post){
        HashMap<String, Object> json = new HashMap<>();
        json.put("postId", post.postId);
        json.put("postTitle", post.postTitle);
        json.put("postContent", post.postContent);
        json.put("postImgUrl", post.postImgUrl);
        json.put("userId", post.userId);
        json.put("userProfilePicUrl", post.userProfileImageUrl);
        json.put("username", post.username);
        json.put("lastUpdated", FieldValue.serverTimestamp());
        return json;
    }

    public static void updateUserProfile(String username, String info, String profileImgUrl, final Model.Listener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> json = new HashMap<>();
        if (username != null)
            json.put("username", username);
        else json.put("username", User.getInstance().userUsername);
        if (info != null)
            json.put("info", info);
        else json.put("info", User.getInstance().userInfo);
        if (profileImgUrl != null)
            json.put("profileImageUrl", profileImgUrl);
        else json.put("profileImageUrl", User.getInstance().profileImageUrl);
        json.put("email", User.getInstance().userEmail);

        db.collection("userProfileData").document(User.getInstance().userEmail).set(json).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener != null)
                    listener.onComplete(task.isSuccessful());
            }
        });
    }

    public static void setUserAppData(final String email){
        FirebaseFirestore db = FirebaseFirestore.getInstance();;
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        db.collection("userProfileData").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    User.getInstance().userUsername = (String) task.getResult().get("username");
                    User.getInstance().profileImageUrl = (String) task.getResult().get("profileImageUrl");
                    User.getInstance().userInfo = (String) task.getResult().get("info");
                    User.getInstance().userEmail = email;
                    User.getInstance().userId = firebaseAuth.getUid();
                }
            }
        });
    }

}
