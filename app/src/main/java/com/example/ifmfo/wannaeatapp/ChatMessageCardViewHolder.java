package com.example.ifmfo.wannaeatapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ChatMessageCardViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView message;
    private TextView time;
    CardView cardContainer;

    public ChatMessageCardViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        message = itemView.findViewById(R.id.message);
        time = itemView.findViewById(R.id.time);
        cardContainer = itemView.findViewById(R.id.message_card_container);

    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getMessage() {
        return message;
    }

    public void setMessage(TextView message) {
        this.message = message;
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }
}
