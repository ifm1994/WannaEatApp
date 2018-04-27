package com.example.ifmfo.wannaeatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Activities.ProductsPerCategoryActivity;
import com.example.ifmfo.wannaeatapp.Activities.RestaurantActivity;
import com.example.ifmfo.wannaeatapp.Model.Product;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.Model.ShoppingCart;

public class ProductCardViewHolder extends RecyclerView.ViewHolder{

    public View view;
    TextView productName;
    TextView productPrice;
    TextView productDescription;
    TextView productAmountSelected;
    TextView productTotalPrice;
    Product currentProduct;
    private TextView shoppingCartTotalAmount;
    private TextView shoppingCartTotalPrice;
    RelativeLayout productAmountContainer;


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    ProductCardViewHolder(View itemView, final Context context) {
        super(itemView);
        view = itemView;
        productName = itemView.findViewById(R.id.product_name);
        productPrice = itemView.findViewById(R.id.product_price);
        productDescription = itemView.findViewById(R.id.product_description);
        productAmountSelected = itemView.findViewById(R.id.product_amount);
        productTotalPrice = itemView.findViewById(R.id.product_total_price);
        productAmountContainer = itemView.findViewById(R.id.product_amount_container);
        ImageButton addButton = itemView.findViewById(R.id.addProductButton);
        ImageButton removeButton = itemView.findViewById(R.id.removeProductButton);
        final ShoppingCart shoppingCart = ShoppingCart.getInstance();
        addButton.setOnClickListener(v -> {
            if(shoppingCart.canAddProduct(currentProduct)){
                shoppingCart.addProduct(currentProduct);
                productAmountContainer.setVisibility(View.VISIBLE);
                productAmountSelected.setText(Integer.toString(shoppingCart.getAmountOfAProduct(currentProduct)));
                productTotalPrice.setText(String.format("%.2f",shoppingCart.getTotalPriceOfAProduct(currentProduct)) + "€");

                RestaurantActivity.updateBasketIndicator();
                ProductsPerCategoryActivity.updateBasketIndicator();
            }else{
                Toast.makeText(context, "No se puede añadir este producto porque ya existe alguno en la cesta que es de otro restaurante",Toast.LENGTH_LONG).show();
            }
        });

        removeButton.setOnClickListener(v -> {
            shoppingCart.removeProduct(currentProduct);
            productAmountSelected.setText(Integer.toString(shoppingCart.getAmountOfAProduct(currentProduct)));
            productTotalPrice.setText(String.format("%.2f",shoppingCart.getTotalPriceOfAProduct(currentProduct)) + "€");

            if(shoppingCart.getAmountOfAProduct(currentProduct) == 0){
                productAmountContainer.setVisibility(View.GONE);
            }

            RestaurantActivity.updateBasketIndicator();
            ProductsPerCategoryActivity.updateBasketIndicator();
        });
    }



}
