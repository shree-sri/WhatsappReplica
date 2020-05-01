package com.shanmathi.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shanmathi.myapplication.MessageActivity;
import com.shanmathi.myapplication.Models.Chat;
import com.shanmathi.myapplication.Models.Users;
import com.shanmathi.myapplication.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context mContext;
    List<Users> mUsers;
    boolean isChat;
    String theLastMsg;

    public UserAdapter(Context mContext, List<Users> mUsers, boolean isChat, String theLastMsg) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isChat = isChat;
    }

    public UserAdapter() {
    }

    public UserAdapter(Context mContext, List<Users> display_chat, boolean isChat) {
        this.mContext = mContext;
        this.mUsers = display_chat;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users users = mUsers.get(position);
        holder.username.setText(users.getUsername());

        if(users.getImgURL().equals("default")){
            holder.profile_img.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(users.getImgURL()).into(holder.profile_img);
        }
        if(isChat){
            theLastMsg(users.getId(),holder.last_msg);
        }else{
            holder.last_msg.setVisibility(View.GONE);
        }
        if(isChat){
            if(users.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else{
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid",users.getId());
                mContext.startActivity(intent);
            }
        });
    }

    private void theLastMsg(final String userid, final TextView last_msg) {
        theLastMsg = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    assert chat != null;
                    assert firebaseUser != null;
                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) || (chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(userid))){
                        theLastMsg = chat.getMessage();
                    }
                }
                if ("default".equals(theLastMsg)) {
                    last_msg.setText("No message");
                } else {
                    last_msg.setText(theLastMsg);
                }
                theLastMsg="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_img;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.username_item);
            profile_img = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }
}
