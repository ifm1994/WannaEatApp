package com.example.ifmfo.wannaeatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ifmfo.wannaeatapp.Model.Booking;

import java.util.List;

public class BookingCardAdapter extends RecyclerView.Adapter<BookingCardViewHolder>{

    private List<Booking> bookings;
    private Context context;

    public BookingCardAdapter(List<Booking> bookings, Context context) {
        this.bookings = bookings;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_card, parent, false);
        return new BookingCardViewHolder(vista, context);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingCardViewHolder holder, int position) {
        holder.bookingRestaurantName.setText(bookings.get(position).getBooking_restaurant_name());
        holder.bookingTime.setText(dateFormat(bookings.get(position).getTime()));

        holder.currentBooking = bookings.get(position);
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
