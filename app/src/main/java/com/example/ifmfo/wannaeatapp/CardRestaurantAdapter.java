package com.example.ifmfo.wannaeatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardRestaurantAdapter extends RecyclerView.Adapter<CardRestaurantViewHolder>{

    private List<Restaurant> restaurants;
    private Context context;

    public CardRestaurantAdapter(List<Restaurant> restaurants, Context context) {
        this.restaurants = restaurants;
        this.context = context;
    }

    @NonNull
    @Override
    public CardRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_restaurant, parent, false);
        return new CardRestaurantViewHolder(vista, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CardRestaurantViewHolder holder, int position) {
        holder.restaurantName.setText(restaurants.get(position).getName());

        Picasso.get()
                .load(restaurants.get(position).getImage_path())
                .into(holder.restaurantImage);


        holder.restaurantFoodType.setText(restaurants.get(position).getKind_of_food());
        fillRestaurantRating(holder, restaurants.get(position).getRating());
        holder.star1.setBackgroundResource(R.drawable.ic_action_star_filled);
        holder.currentRestaurant = restaurants.get(position);
    }

    private void fillRestaurantRating(@NonNull CardRestaurantViewHolder holder, String restaurantRating) {
        int intNumber, decNumberInt, i;
        ImageView [] starArray = {holder.star1, holder.star2, holder.star3, holder.star4,holder.star5 };
        if(restaurantRating.indexOf('.') != -1){
            intNumber = Integer.parseInt(restaurantRating.substring(0, restaurantRating.indexOf('.')));
            decNumberInt = Integer.parseInt(restaurantRating.substring(restaurantRating.indexOf('.') + 1));
        }else{
            intNumber = Integer.parseInt(restaurantRating);
            decNumberInt = 0;
        }
        boolean hasHalfStar = decNumberInt >= 50;
        //Poner imagen de estrella rellena
        for (i = 0; i < intNumber; i++){
            starArray[i].setBackgroundResource(R.drawable.ic_action_star_filled);
//            Toast.makeText(context, Integer.toString(i) + " estrella cambiada a rellena",Toast.LENGTH_LONG).show();
        }
        //Poner imagen de estrella a media
        if(hasHalfStar){
            if (intNumber != 0){
                i=i+1;
            }
            starArray[i].setBackgroundResource(R.drawable.ic_action_star_half);
//            Toast.makeText(context, Integer.toString(i) + " estrella cambiada a media",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
