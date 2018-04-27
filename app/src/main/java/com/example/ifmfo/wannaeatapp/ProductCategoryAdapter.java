package com.example.ifmfo.wannaeatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ifmfo.wannaeatapp.Model.Product;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;

import java.util.ArrayList;
import java.util.List;


public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryViewHolder>{

    private List<String> categories;
    private Context context;
    private Restaurant thisRestaurant;

    public ProductCategoryAdapter(Restaurant restaurant, List<String> categories, Context context){
        this.context = context;
        this.categories = categories;
        thisRestaurant = restaurant;
    }

    @NonNull
    @Override
    public ProductCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_category_item, parent, false);
        return new ProductCategoryViewHolder(vista, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCategoryViewHolder holder, int position) {
        holder.itemList.setText(categories.get(position));
        holder.currentRestaurant = thisRestaurant;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

}
