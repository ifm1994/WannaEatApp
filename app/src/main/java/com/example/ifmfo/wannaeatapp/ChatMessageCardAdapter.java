package com.example.ifmfo.wannaeatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ifmfo.wannaeatapp.Model.ChatMessage;
import com.example.ifmfo.wannaeatapp.Model.ReceiveMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatMessageCardAdapter extends RecyclerView.Adapter<ChatMessageCardViewHolder> {

    private List <ReceiveMessage> messagesList = new ArrayList<>();
    private Context context;

    public ChatMessageCardAdapter(Context context) {
        this.context = context;
    }

    public void addMessage(ReceiveMessage chatMessage){
        messagesList.add(chatMessage);
        notifyItemInserted(messagesList.size());
    }

    @NonNull
    @Override
    public ChatMessageCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_card, parent, false);
        return new ChatMessageCardViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageCardViewHolder holder, int position) {
        holder.getName().setText(messagesList.get(position).getMessageUser());
        holder.getMessage().setText(messagesList.get(position).getMessageText());

        Long timeCode = messagesList.get(position).getTime();
        Date date = new Date(timeCode);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yy h:mm"); //a pm o am

        holder.getTime().setText(simpleDateFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }
}
