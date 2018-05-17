package com.example.ifmfo.wannaeatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ifmfo.wannaeatapp.Model.BookingProduct;

import java.util.List;

public class SingleBookingProductItemAdapter extends RecyclerView.Adapter<SingleBookingProductItemViewHolder>{

    private List<BookingProduct> products;
    private Context context;

    public SingleBookingProductItemAdapter(List<BookingProduct> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public SingleBookingProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_booking_product_item, parent, false);
        return new SingleBookingProductItemViewHolder(vista);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SingleBookingProductItemViewHolder holder, int position) {
        holder.productName.setText(products.get(position).getName());
        holder.productAmount.setText(Integer.toString(products.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
