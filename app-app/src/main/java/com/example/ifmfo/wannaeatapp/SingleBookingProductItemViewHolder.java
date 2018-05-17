package com.example.ifmfo.wannaeatapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class SingleBookingProductItemViewHolder extends RecyclerView.ViewHolder  {

    TextView productName;
    TextView productAmount;

    public SingleBookingProductItemViewHolder(View itemView) {
        super(itemView);

        productAmount = itemView.findViewById(R.id.booking_product_amount);
        productName = itemView.findViewById(R.id.booking_product_name);

    }
}
