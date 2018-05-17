package com.example.ifmfo.wannaeatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ifmfo.wannaeatapp.Model.Product;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;

import java.util.List;

public class BookingProductAdapter extends RecyclerView.Adapter<BookingProductViewHolder>{

    public Context context;
    private static final GlobalResources GLOBAL_RESOURCES = GlobalResources.getInstance();
    private List<Product> allProducts;

    public BookingProductAdapter(Context context){
        this.context = context;
        this.allProducts = GLOBAL_RESOURCES.shopping_basket_getAllDiferentsProducts();
    }

    @NonNull
    @Override
    public BookingProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_product_item, parent, false);
        return new BookingProductViewHolder(vista, context);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull BookingProductViewHolder holder, int position) {
        holder.bProductName.setText(allProducts.get(position).getName());
        holder.bProductAmount.setText(Integer.toString(GLOBAL_RESOURCES.shopping_basket_getAmountOfAProduct(allProducts.get(position))));
        holder.bProductPrice.setText(String.format("%.2f",allProducts.get(position).getPrice()) + "€");
        holder.currentProduct = allProducts.get(position);
    }

    @Override
    public int getItemCount() {
        return GLOBAL_RESOURCES.shopping_basket_getAmountOfDiferentsProduct();
    }
}
