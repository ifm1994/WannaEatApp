package com.example.ifmfo.wannaeatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ifmfo.wannaeatapp.Activities.RestaurantActivity;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;

public class RestaurantCardViewHolder extends RecyclerView.ViewHolder {

    ImageView restaurantImage;
    TextView restaurantName;
    TextView restaurantFoodType;
    TextView restaurantRating;
    public View view;
    public Restaurant currentRestaurant;
    private Context context;

    RestaurantCardViewHolder(View itemView, final Context context) {
        super(itemView);
        this.context = context;
        view = itemView;

        restaurantImage = itemView.findViewById(R.id.restaurant_image);
        restaurantName= itemView.findViewById(R.id.restaurant_name);
        restaurantFoodType= itemView.findViewById(R.id.restaurant_kind_of_food);
        restaurantRating = itemView.findViewById(R.id.restaurant_rating);

        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // item clicked
                Intent intent = new Intent(context, RestaurantActivity.class);
                intent.putExtra("restaurantObject", currentRestaurant);
                ( (Activity) context).startActivityForResult(intent, 1);
            }
        });
    }

}
