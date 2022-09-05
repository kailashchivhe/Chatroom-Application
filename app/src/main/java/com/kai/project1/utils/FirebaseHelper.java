package com.kai.project1.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.kai.project1.listener.LoginListener;
import com.kai.project1.listener.ProfileListener;
import com.kai.project1.listener.ProfileRetrieveListener;
import com.kai.project1.listener.RegisterListener;
import com.kai.project1.model.User;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class FirebaseHelper {
    static FirebaseAuth firebaseAuth;
    static FirebaseFirestore firebaseFirestore;
    static FirebaseFirestore db;
    private static StorageReference mStorageRef;
    private static StorageTask mUploadTask;
    public static final String TAG = "vidit";


    public static void initFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public static FirebaseUser getUser(){
        return firebaseAuth.getCurrentUser();
    }

    public static void login(String email, String password, LoginListener loginListener ){
        firebaseAuth.signInWithEmailAndPassword( email, password ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                loginListener.onSuccess();
            } else {
                loginListener.onFailure( task.getException().getMessage() );
            }
        });
    }

    protected static void upload(Bitmap bitmap){
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+".JPEG");
            mUploadTask = fileReference.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                HashMap<String,Object> map = new HashMap<>();
                                map.put("uri",uri.toString());
                                db.collection("project1").document("Users").collection("Users").document(firebaseAuth.getCurrentUser().getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "onSuccess: uploaded");
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: "+ e.getMessage());
                            }
                        });
                        Log.d(TAG, "onSuccess: registration done");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        }
        else {
            //Photo not clicked
        }
    }

    public static void register(String email, String password, String firstName, String lastName, String city, String gender, Bitmap bitmapProfile,RegisterListener registerListener){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(firstName+ " " + lastName).build();
                    user.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                DocumentReference dr = db.collection("project1").document("Users").collection("Users").document(firebaseAuth.getCurrentUser().getUid());
                                HashMap<String,Object> map = new HashMap<>();
                                map.put("firstname",firstName);
                                map.put("lastname",lastName);
                                map.put("email",email);
                                map.put("password",password);
                                map.put("userid", firebaseAuth.getCurrentUser().getUid());
                                map.put("city",city);
                                map.put("gender",gender);
                                dr.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //image profile upload left
                                            upload(bitmapProfile);
                                            registerListener.onSuccess();
                                        }
                                        else{
                                            //Profile details not set properly
                                            registerListener.onFailure(task.getException().getMessage());
                                        }
                                    }
                                });
                            }
                            else{
                                // Profile name not set properly
                                registerListener.onFailure(task.getException().getMessage());
                            }
                        }
                    });
                }
                else{
                    // User not created properly
                    registerListener.onFailure(task.getException().getMessage());
                }
            }
        });
    }

    public static void logout(){
        firebaseAuth.signOut();
    }

    public static void profileUpdate(User user, ProfileListener profileListener){
        DocumentReference dr = db.collection("project1").document("Users").collection("Users").document(user.getUserid());
        HashMap<String,Object> map = new HashMap<>();
        map.put("firstname", user.getFirstname());
        map.put("lastname", user.getLastname());
        map.put("gender",user.getGender());
        map.put("city" , user.getCity());
        dr.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    profileListener.onSuccess();
                }
                else{
                    profileListener.onFailure(task.getException().getMessage());
                }
            }
        });
    }

    public static void userDetails(ProfileRetrieveListener profileRetrieveListener){
        DocumentReference dr = db.collection("project1").document("Users").collection("Users").document(getUser().getUid());
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    String firstname = ds.get("firstname").toString();
                    String lastname = ds.get("lastname").toString();
                    String city = ds.get("city").toString();
                    String gender = ds.get("gender").toString();
                    profileRetrieveListener.onSuccess(new User(firstname,lastname,gender,city,FirebaseHelper.getUser().getUid()));
                }
                else{
                    profileRetrieveListener.onFail(task.getException().getMessage());
                }
            }
        });

    }

    public static void getAllChatRooms(){
        CollectionReference dr = db.collection("chatrooms");
        dr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

            }
        });
    }

    public static void postMessage(String chatRoomId){
        chatRoomId = "THQV0n7mQyAFH97zbARd";
        Map<String, Object> messageMap = new HashMap<String, Object>();
        messageMap.put( "likes", new HashMap<String,Boolean>() );
        messageMap.put("message", "Hi");
        messageMap.put("userId", firebaseAuth.getUid());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        messageMap.put("date", formatter.format(date) );

        firebaseFirestore.collection("chatrooms").document(chatRoomId).collection("messages")
                .add(messageMap)
                .addOnSuccessListener(documentReference -> {

                })
                .addOnFailureListener(e -> {

                });
    }

    public static void createChatRoom(String name){
        Map<String, Object> chat = new HashMap<String, Object>();
        chat.put("name", name);
        Map<String, Boolean> onlineUsers = new HashMap<String, Boolean>();
        onlineUsers.put(firebaseAuth.getUid(), true );
        chat.put("online", onlineUsers);
        firebaseFirestore.collection("chatrooms").add(chat);
    }

    public static void likeMessage(String messageId, String chatId, boolean isLiked){
        if( isLiked ){
            firebaseFirestore.collection("project1/chatrooms/"+chatId+"/chats/messages"+messageId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Map<String, Object> deleteData = new HashMap<>();
                        deleteData.put("likes."+getUser().getUid(), FieldValue.delete());
                        firebaseFirestore.collection("project1/chatrooms/"+chatId+"/chats/messages"+messageId).document(document.getId()).update(deleteData);
                    }
                }
            });
        }
        else{
            firebaseFirestore.collection("project1/chatrooms/"+chatId+"/chats/messages"+messageId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if( task.isSuccessful() ){
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Map<String, Boolean> likes = new HashMap<>();
                            Map<String, Object> map = new HashMap<>();
                            likes.put(getUser().getUid(), true);
                            map.put("likes", likes );
                            firebaseFirestore.collection("project1/chatrooms/"+chatId+"/chats/messages"+messageId).document(document.getId()).set( map, SetOptions.merge() );
                        }
                    }
                }
            });
        }
    }

    public static void getOnlineUsers(String chatRoomId){
        firebaseFirestore.collection("project1/chatrooms/"+chatRoomId+"/online/users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if( task.isSuccessful() ){
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    }
                }
            }
        });
    }

    public static void addOnlineUser(String chatRoomId){
        firebaseFirestore.collection("project1/chatrooms/"+chatRoomId+"/online/users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if( task.isSuccessful() ){
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    }
                }
            }
        });
    }

    public static void removeOnlineUser(String chatRoomId){
        firebaseFirestore.collection("project1/chatrooms/"+chatRoomId+"/online/users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if( task.isSuccessful() ){
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    }
                }
            }
        });
    }

}