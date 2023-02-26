package com.example.socialmediamobileqev.Messages;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediamobileqev.R;
import com.example.socialmediamobileqev.Users;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Messaging extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    TextView userName;
    CircleImageView imageViewProfile;
    EditText textSendEditText;
    ImageButton sendMessage;
    Chat chat;

    MessagesAdaper messagesAdaper;
    List<Chat> chatList;
    RecyclerView recyclerView;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);


        // Initializing Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        recyclerView = findViewById(R.id.showMessageRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setAdapter(messagesAdaper);
        userName = findViewById(R.id.nameUserMessagingToolbar);
        imageViewProfile = findViewById(R.id.imageViewMessaginToolbar);

        Intent intent = getIntent();
        userId = intent.getStringExtra("IDUser");

        setUserNameForToolbar();







        sendMessage = findViewById(R.id.sendMessageIcon);
        textSendEditText = findViewById(R.id.textMessageEditText);

        // For send message to another user
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgText = textSendEditText.getText().toString().trim();
                if(!msgText.equals("")){
                    sendMessage(firebaseUser.getUid(),userId,msgText);
                    textSendEditText.getText().clear();

                }else {
                    textSendEditText.setError("You can't send empty message");

                }

            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);

                if (user.getUrl_profile() == null) {
                    imageViewProfile.setImageResource(R.drawable.user);
                } else {
                    Picasso.get().load(user.getUrl_profile()).into(imageViewProfile);


                }
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                readMesage(firebaseUser.getUid(),userId,user.getUrl_profile());


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }



    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        reference.child("Chats").push().setValue(hashMap);

    }

    private void readMesage(String myID, String userid, String imageUrl){

        chatList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chat chat1 = dataSnapshot.getValue(Chat.class);
                    if(chat1.getReceiver().equals(myID) && chat1.getSender().equals(userid) || chat1.getReceiver().equals(userid)  && chat1.getSender().equals(myID)){

                        chatList.add(chat1);
                        //chatList.add(new Chat(myID, userid, chat1.getMessage()));
                        //
                    }
                    messagesAdaper = new MessagesAdaper(Messaging.this,chatList,imageUrl);
                    recyclerView.setAdapter(messagesAdaper);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    public void setUserNameForToolbar(){
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                if (user != null) {
                    String name = user.name;
                    userName.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(messagesAdaper);


    }
}