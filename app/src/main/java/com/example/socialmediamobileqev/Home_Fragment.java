package com.example.socialmediamobileqev;

import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediamobileqev.Messages.Messaging;
import com.example.socialmediamobileqev.SeacrchUsers.SearchUserFirebaseRecycler;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Home_Fragment extends Fragment {

    StorageReference storageReference;
    private RecyclerView messagesRecyclerView;
    CircleImageView circleImageView;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    FirebaseUser user;
    DatabaseReference reference;
    String userID;
    ImageView btnSearchImageView;
    EditText searchEditTextUser;
    FirebaseRecyclerAdapter adapters;

    private List<Users> userList;
    String searchEditText;

    FirebaseRecyclerOptions<Users> options;


    public Home_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_, container, false);
        searchEditTextUser = view.findViewById(R.id.search_barEditTextHome);



        messagesRecyclerView = view.findViewById(R.id.recyclerViewHome);
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        searchEditText = searchEditTextUser.getText().toString().trim();



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Star Search inside Firebase about Users

        // get Image's profile From Storage
        circleImageView = view.findViewById(R.id.profile_image);
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        storageReference = storageReference.child("Users/" + mAuth.getCurrentUser().getUid() + "images/ ");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Picasso.get().load(uri).into(circleImageView);
            }
        });


//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                user = FirebaseAuth.getInstance().getCurrentUser();
//                reference = FirebaseDatabase.getInstance().getReference("Users");
//                userID = user.getUid();
//
//                final String profilePicURL = snapshot.child("Users").child(userID).child("profilepic").getValue(String.class);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.child("Users").getChildren()) {
//                    final String getEveryUser = dataSnapshot.getKey();
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        searchEditTextUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                readUsers(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString() != null) {
                    readUsers(editable.toString());

                } else {
                    messagesRecyclerView.setAdapter(null);
                }

            }

        });

        super.onViewCreated(view, savedInstanceState);
    }
    void readUsers(String Data) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByChild("userID").startAt(Data).endAt(Data + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(query, Users.class)
                .build();
        adapters = new FirebaseRecyclerAdapter<Users, SearchUserFirebaseRecycler.ViewHolder>(options) {


            @NonNull
            @Override
            public SearchUserFirebaseRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search_recyclerview, parent, false);

                return new SearchUserFirebaseRecycler.ViewHolder(view);

            }

            @Override
            protected void onBindViewHolder(@NonNull SearchUserFirebaseRecycler.ViewHolder holder, int position, @NonNull Users model) {

                holder.nameUser.setText(model.getName());

                if (model.getUrl_profile() == null) {
                    holder.profile_url.setImageResource(R.drawable.user);
                } else {
                    Picasso.get().load(model.getUrl_profile()).into(holder.profile_url);

                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(holder.itemView.getContext(), Messaging.class);
                        intent.putExtra("IDUser", model.userID);
                        Log.i("FadyP", model.userID);
                        view.getContext().startActivity(intent);
                    }
                });

            }



        };
        adapters.startListening();

        messagesRecyclerView.setAdapter(adapters);
        if (searchEditTextUser.getText().toString() == null) {
            messagesRecyclerView.setAdapter(null);
        }
    }
}

