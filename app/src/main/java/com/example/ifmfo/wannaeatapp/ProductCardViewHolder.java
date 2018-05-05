package com.example.ifmfo.wannaeatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Activities.ProductsPerCategoryActivity;
import com.example.ifmfo.wannaeatapp.Activities.RestaurantActivity;
import com.example.ifmfo.wannaeatapp.Model.Product;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;

public class ProductCardViewHolder extends RecyclerView.ViewHolder{

    public View view;
    TextView productName;
    TextView productPrice;
    TextView productDescription;
    TextView productAmountSelected;
    TextView productTotalPrice;
    Product currentProduct;
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

        final GlobalResources globalResources = GlobalResources.getInstance();
        addButton.setOnClickListener(v -> {
            if(globalResources.shopping_basket_canAddProduct(currentProduct)){
                globalResources.shopping_basket_addProduct(currentProduct);
                ProductsPerCategoryActivity.filtrarProductosPorCategoria(ProductsPerCategoryActivity.getThisCategory());
                RestaurantActivity.updateBasketIndicator();
                ProductsPerCategoryActivity.updateBasketIndicator();
            }else{
                Toast.makeText(context, "No se puede añadir este producto porque ya existe alguno en la cesta que es de otro restaurante",Toast.LENGTH_LONG).show();
            }
        });

        removeButton.setOnClickListener(v -> {
            globalResources.shopping_basket_removeProduct(currentProduct);
            ProductsPerCategoryActivity.filtrarProductosPorCategoria(ProductsPerCategoryActivity.getThisCategory());
            RestaurantActivity.updateBasketIndicator();
            ProductsPerCategoryActivity.updateBasketIndicator();
        });
    }



}
