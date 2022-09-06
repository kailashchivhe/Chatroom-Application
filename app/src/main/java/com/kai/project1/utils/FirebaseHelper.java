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
import com.kai.project1.model.ChatRoom;
import com.kai.project1.model.Message;
import com.kai.project1.model.OnlineUsers;
import com.kai.project1.model.User;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        ArrayList<ChatRoom> chatRoomArrayList = new ArrayList<>();
        CollectionReference dr = db.collection("chatrooms");
        dr.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        ChatRoom chatRoom = new ChatRoom();
                        chatRoom.setName((String) documentSnapshot.get("name"));
                        chatRoom.setChatId(documentSnapshot.getId());
                        HashMap<String, String> onlineMap = (HashMap<String, String>) documentSnapshot.get("online");
                        chatRoom.setOnline(onlineMap);
                        ArrayList<Message> messageArrayList = new ArrayList<>();
                        dr.document(documentSnapshot.getId()).collection("messages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                if(task1.isSuccessful()){
                                    for(DocumentSnapshot documentSnapshot1: task1.getResult().getDocuments()){
                                        Message message = new Message();
                                        message.setMessage((String) documentSnapshot1.get("message"));
                                        message.setLikes((Map<String, Boolean>) documentSnapshot1.get("likes"));
                                        message.setUserId((String) documentSnapshot1.get("userId"));
                                        message.setUserName((String) documentSnapshot1.get("userName"));
                                        message.setDate(documentSnapshot1.get("date").toString());
                                        messageArrayList.add(message);
                                    }
                                    chatRoom.setMessages(messageArrayList);
                                    //TODO add success listener
                                    chatRoomArrayList.add(chatRoom);
                                }
                                else{
                                    //TODO add failure listener
                                }
                            }
                        });
                    }
                }
                else{
                    //TODO add failure listener
                }
            }
        });
    }

    public static void postMessage(String chatRoomId){
        Map<String, Object> messageMap = new HashMap<String, Object>();
        messageMap.put( "likes", new HashMap<String,Boolean>() );
        messageMap.put("message", "Hi");
        messageMap.put("userId", firebaseAuth.getUid());
        messageMap.put("userName", firebaseAuth.getCurrentUser().getDisplayName());
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

    public static void deleteMessage(String chatRoomId, String messageId ){
        firebaseFirestore.collection("chatrooms").document(chatRoomId).collection("messages").document(messageId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //TODO add success listener
                }
                else{
                    //TODO add failure listener
                }
            }
        });
    }

    public static void createChatRoom(String name){
        Map<String, Object> chat = new HashMap<String, Object>();
        chat.put("name", name);
        Map<String, String> onlineUsers = new HashMap<String, String>();
        onlineUsers.put(firebaseAuth.getUid(), Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName() );
        chat.put("online", onlineUsers);
        firebaseFirestore.collection("chatrooms").add(chat).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){

                }
                else{

                }
            }
        });
    }

    public static void likeMessage(String messageId, String chatId, boolean isLiked){
        if( isLiked ){
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("likes."+firebaseAuth.getUid(), FieldValue.delete() );
            firebaseFirestore.collection("chatrooms").document(chatId).collection("messages").document(messageId).update( userMap ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else{

                    }
                }
            });
        }
        else{
            HashMap<String, Boolean> likesMap = new HashMap<>();
            likesMap.put(firebaseAuth.getUid(), true );
            HashMap<String, Object> map = new HashMap<>();
            map.put("likes", likesMap );
            firebaseFirestore.collection("chatrooms").document(chatId).collection("messages").document(messageId).set(map, SetOptions.merge() ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                    }
                    else{

                    }
                }
            });
        }
    }

    public static void getOnlineUsers(String chatRoomId){
        ArrayList<OnlineUsers> onlineUsers = new ArrayList<>();
        firebaseFirestore.collection("chatrooms").document(chatRoomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    HashMap<String, String> onlineMap = (HashMap<String, String>) task.getResult().get("online");
                    for (Map.Entry<String, String> entry : onlineMap.entrySet()) {
                        OnlineUsers onlineUsers1 = new OnlineUsers();
                        onlineUsers1.setId( entry.getKey() );
                        onlineUsers1.setUserName(entry.getValue());
                        onlineUsers.add(onlineUsers1);
                    }
                    //TODO success listener
                }
                else{
                    //TODO failure listener
                }
            }
        });
    }

    public static void addOnlineUser(String chatRoomId){
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put(firebaseAuth.getUid(), firebaseAuth.getCurrentUser().getDisplayName() );
        HashMap<String, Object> map = new HashMap<>();
        map.put("online", userMap );
        firebaseFirestore.collection("chatrooms").document(chatRoomId).set(map, SetOptions.merge() ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
                else{

                }
            }
        });
    }

    public static void removeOnlineUser(String chatRoomId){
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("online."+firebaseAuth.getUid(), FieldValue.delete() );
        firebaseFirestore.collection("chatrooms").document(chatRoomId).update( userMap ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
                else{

                }
            }
        });
    }
}