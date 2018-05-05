package com.example.ifmfo.wannaeatapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ifmfo.wannaeatapp.Activities.EmptyShoppingBasketActivity;
import com.example.ifmfo.wannaeatapp.Activities.FullShoppingBasketActivity;
import com.example.ifmfo.wannaeatapp.Activities.RestaurantActivity;
import com.example.ifmfo.wannaeatapp.Model.Product;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;

public class BookingProductViewHolder extends  RecyclerView.ViewHolder{

    TextView bProductName;
    TextView bProductPrice;
    TextView bProductAmount;
    View view;
    Product currentProduct;
    RelativeLayout bookingProductContainer;
    Context context;


    @SuppressLint("SetTextI18n")
    BookingProductViewHolder(View itemView, Context context) {
        super(itemView);
        view = itemView;
        this.context = context;
        bProductName = itemView.findViewById(R.id.booking_product_name);
        bProductPrice = itemView.findViewById(R.id.booking_product_price);
        bProductAmount = itemView.findViewById(R.id.booking_product_amount);
        bookingProductContainer = itemView.findViewById(R.id.booking_product_container);

        ImageButton addButton = itemView.findViewById(R.id.addProductButton);
        ImageButton removeButton = itemView.findViewById(R.id.removeProductButton);

        final GlobalResources globalResources = GlobalResources.getInstance();

        addButton.setOnClickListener(v -> {
            globalResources.shopping_basket_addProduct(currentProduct);

            FullShoppingBasketActivity.dibujarProductosDeLaReserva();

            FullShoppingBasketActivity.actualizarPrecioTotal();
        });

        removeButton.setOnClickListener(v -> {
            globalResources.shopping_basket_removeProduct(currentProduct);

            if(globalResources.shopping_basket_getNumberOfProductsAdded() == 0){
                Intent goEmpty = new Intent( context, EmptyShoppingBasketActivity.class );
                goEmpty.putExtra("restaurant", RestaurantActivity.getCurrentRestaurant());
                view.getContext().startActivity(goEmpty);
            }else{
                FullShoppingBasketActivity.dibujarProductosDeLaReserva();
            }
        });
    }

}
