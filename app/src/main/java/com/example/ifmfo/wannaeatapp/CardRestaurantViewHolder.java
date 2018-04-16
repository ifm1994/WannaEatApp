package com.example.ifmfo.wannaeatapp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Model.Restaurant;

import java.io.Serializable;

public class CardRestaurantViewHolder extends RecyclerView.ViewHolder {

    ImageView restaurantImage;
    TextView restaurantName;
    TextView restaurantFoodType;
    ImageView star1;
    ImageView star2;
    ImageView star3;
    ImageView star4;
    ImageView star5;
    public View view;
    public Restaurant currentRestaurant;
    private Context context;

    public CardRestaurantViewHolder(View itemView, final Context context) {
        super(itemView);
        view = itemView;
        this.context = context;

        restaurantImage = itemView.findViewById(R.id.restaurant_image);
        restaurantName= itemView.findViewById(R.id.restaurant_name);
        restaurantFoodType= itemView.findViewById(R.id.restaurant_kind_of_food);
        star1 = itemView.findViewById(R.id.star_1);
        star2 = itemView.findViewById(R.id.star_2);
        star3 = itemView.findViewById(R.id.star_3);
        star4 = itemView.findViewById(R.id.star_4);
        star5 = itemView.findViewById(R.id.star_5);

        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // item clicked
                Intent intent = new Intent(context, RestaurantActivity.class);
                intent.putExtra("restaurantObject", currentRestaurant);
                context.startActivity(intent);
            }
        });
    }

}
