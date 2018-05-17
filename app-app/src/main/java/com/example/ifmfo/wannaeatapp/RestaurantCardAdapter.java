package com.example.ifmfo.wannaeatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantCardAdapter extends RecyclerView.Adapter<RestaurantCardViewHolder>{

    private List<Restaurant> restaurants;
    private Context context;

    public RestaurantCardAdapter(List<Restaurant> restaurants, Context context) {
        this.restaurants = restaurants;
        this.context = context;
    }

    @NonNull
    @Override
    public RestaurantCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_card, parent, false);
        return new RestaurantCardViewHolder(vista, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantCardViewHolder holder, int position) {
        holder.restaurantName.setText(restaurants.get(position).getName());
        holder.restaurantFoodType.setText(restaurants.get(position).getKind_of_food());
        holder.restaurantRating.setText(restaurants.get(position).getRating());
        Picasso.get()
                .load(restaurants.get(position).getImage_path())
                .into(holder.restaurantImage);
        holder.currentRestaurant = restaurants.get(position);
    }



    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
