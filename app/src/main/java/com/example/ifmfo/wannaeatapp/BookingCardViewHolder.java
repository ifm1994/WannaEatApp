package com.example.ifmfo.wannaeatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ifmfo.wannaeatapp.Activities.SingleBookingMade;
import com.example.ifmfo.wannaeatapp.Model.Booking;

public class BookingCardViewHolder extends RecyclerView.ViewHolder {

    TextView bookingRestaurantName;
    TextView bookingTime;
    public View view;
    public Booking currentBooking;
    private Button goToSingleBooking;

    public BookingCardViewHolder(View itemView, Context context) {
        super(itemView);
        view = itemView;

        bookingRestaurantName = itemView.findViewById(R.id.booking_restaurant_name);
        bookingTime = itemView.findViewById(R.id.booking_time);
        goToSingleBooking = itemView.findViewById(R.id.see_single_booking);

        goToSingleBooking.setOnClickListener(v -> {
            Intent intent = new Intent(context, SingleBookingMade.class);
            intent.putExtra("booking", currentBooking);
            ( (Activity) context).startActivityForResult(intent, 1);
        });

    }


}