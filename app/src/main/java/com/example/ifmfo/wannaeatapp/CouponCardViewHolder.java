package com.example.ifmfo.wannaeatapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ifmfo.wannaeatapp.Model.Coupon;

public class CouponCardViewHolder extends RecyclerView.ViewHolder {

    TextView couponDescription;
    TextView couponDiscount;
    TextView couponRestaurant;
    public View view;
    public Coupon currentCoupon;
    private Context context;

    public CouponCardViewHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        view = itemView;

        couponDescription = itemView.findViewById(R.id.coupon_description);
        couponDiscount = itemView.findViewById(R.id.coupon_discount);
        couponRestaurant = itemView.findViewById(R.id.coupon_restaurant);
    }


}
