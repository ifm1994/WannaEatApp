package com.example.ifmfo.wannaeatapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Model.Booking;
import com.example.ifmfo.wannaeatapp.Model.User;

import java.util.List;

public class ClientChatCardAdapter  extends RecyclerView.Adapter<ClientChatCardViewHolder> {

    private Context context;
    private List<Booking> bookings;
    private List<User> users;


    public ClientChatCardAdapter(Context context, List<Booking> bookings, List<User> users) {
        this.context = context;
        this.bookings = bookings;
        this.users = users;
    }

    @Override
    public ClientChatCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_chat_card, parent, false);
        return new ClientChatCardViewHolder(vista, context);
    }

    @Override
    public void onBindViewHolder(ClientChatCardViewHolder holder, int position) {
        holder.clientName.setText( users.get(position).getName() );
        holder.bookingTime.setText( dateFormat(bookings.get(position).getTime()) );
        holder.currentClient = users.get(position);
        holder.restaurantName = bookings.get(position).getBooking_restaurant_name();
    }

    private String dateFormat(String time) {
        time = time.replace("-"," ");
        time = time.replace("_","-");
        return time;
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }
}
