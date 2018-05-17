package com.example.ifmfo.wannaeatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ifmfo.wannaeatapp.Model.Coupon;

import java.util.List;

public class CouponCardAdapter extends RecyclerView.Adapter<CouponCardViewHolder>{

    private List<Coupon> coupons;
    private Context context;

    public CouponCardAdapter(List <Coupon> coupons, Context context){
        this.coupons = coupons;
        this.context = context;
    }

    @NonNull
    @Override
    public CouponCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_card, parent, false);
        return new CouponCardViewHolder(vista, context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CouponCardViewHolder holder, int position) {
        holder.couponRestaurant.setText(coupons.get(position).getRestaurantName());
        holder.couponDiscount.setText(Integer.toString(coupons.get(position).getDiscount()) + "%");
        holder.couponDescription.setText(coupons.get(position).getDescription());
        holder.currentCoupon = coupons.get(position);
    }

    @Override
    public int getItemCount() {
        return coupons.size();
    }
}
