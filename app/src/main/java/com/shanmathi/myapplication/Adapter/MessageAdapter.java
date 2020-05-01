package com.shanmathi.myapplication.Adapter;

import android.content.Context;
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
import com.shanmathi.myapplication.Models.Chat;
import com.shanmathi.myapplication.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    static final int MSG_TYPE_LEFT = 0;
    static final int MSG_TYPE_RIGHT = 0;

    Context mContext;
    List<Chat> mchat;
    String imgURL;
    FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mchat, String imgURL) {
        this.mContext = mContext;
        this.mchat = mchat;
        this.imgURL = imgURL;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view= LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = mchat.get(position);
        holder.show_msg.setText(chat.getMessage());
        if (imgURL.equals("default")){
            holder.profile_img.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imgURL).into(holder.profile_img);
        }
        if (position==mchat.size()-1){
            if (chat.isSeen()){
                holder.txt_seen.setText("seen");
            }else {
                holder.txt_seen.setText("delivered");
            }
        }else {
            holder.txt_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_msg;
        public ImageView profile_img;
        private TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_msg = itemView.findViewById(R.id.show_message);
            profile_img = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.text_seen);
        }
    }


    public int getItemView(int position){
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if (mchat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
