package com.example.ifmfo.wannaeatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ifmfo.wannaeatapp.Activities.ProductsPerCategoryActivity;
import com.example.ifmfo.wannaeatapp.Fragments.RestaurantMenuTab;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;

import java.io.Serializable;

public class ProductCategoryViewHolder extends RecyclerView.ViewHolder{

    private Context context;
    public View view;
    TextView itemList;
    Restaurant currentRestaurant;

    ProductCategoryViewHolder(View itemView, final Context context) {
        super(itemView);
        this.context = context;
        view = itemView;
        itemList = view.findViewById(R.id.category_item_list);


        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // item clicked
                String categorySelected = (String) itemList.getText();
                Intent intent = new Intent(context, ProductsPerCategoryActivity.class);
                intent.putExtra("restaurant", currentRestaurant);
                intent.putExtra("categoryName", categorySelected);
                intent.putExtra("products", (Serializable) RestaurantMenuTab.getProducts());
                ((Activity) context).startActivityForResult(intent,1);
            }
        });
    }





}
