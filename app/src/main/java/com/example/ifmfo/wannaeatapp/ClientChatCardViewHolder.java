package com.example.ifmfo.wannaeatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Activities.ChatActivity;
import com.example.ifmfo.wannaeatapp.Model.User;

import java.io.Serializable;

public class ClientChatCardViewHolder extends RecyclerView.ViewHolder{

    TextView clientName, bookingTime;
    User currentClient;
    Context context;
    String restaurantName;


    public ClientChatCardViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        clientName = itemView.findViewById(R.id.client_name);
        bookingTime = itemView.findViewById(R.id.booking_time);

        itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("comingFrom", "admin");
            intent.putExtra("client", currentClient);
            intent.putExtra("sender_name", restaurantName);
            ((Activity) context).startActivityForResult(intent, 1);
        });

    }
}
