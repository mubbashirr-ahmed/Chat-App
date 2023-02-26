package com.example.socialmediamobileqev.SeacrchUsers;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediamobileqev.Messages.Messaging;
import com.example.socialmediamobileqev.R;
import com.example.socialmediamobileqev.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class SearchUserFirebaseRecycler extends FirebaseRecyclerAdapter<Users,SearchUserFirebaseRecycler.ViewHolder> {

    static View v;
    public SearchUserFirebaseRecycler(@NonNull FirebaseRecyclerOptions<Users> options) {
        super(options);
    }




    public SearchUserFirebaseRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search_recyclerview,parent,false);

        return new SearchUserFirebaseRecycler.ViewHolder(view);
    }



    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Users model) {

        holder.nameUser.setText(model.getName());

        if (model.getUrl_profile() == null) {
            holder.profile_url.setImageResource(R.mipmap.ic_launcher);
        } else {
            Picasso.get().load(model.getUrl_profile()).into(holder.profile_url);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (v.getContext(), Messaging.class);
                v.getContext().startActivity(intent);



            }
        });

    }



    public static class ViewHolder  extends RecyclerView.ViewHolder{

        public TextView nameUser;
        public CircleImageView profile_url;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameUser = itemView.findViewById(R.id.name_item_search);
            profile_url = itemView.findViewById(R.id.image_profile_search);
            v =itemView;
        }



    }

}
