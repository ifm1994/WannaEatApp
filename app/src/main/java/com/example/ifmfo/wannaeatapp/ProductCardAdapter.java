package com.example.ifmfo.wannaeatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Activities.RestaurantActivity;
import com.example.ifmfo.wannaeatapp.Model.Product;
import com.example.ifmfo.wannaeatapp.Model.ShoppingCart;

import java.util.List;

public class ProductCardAdapter extends RecyclerView.Adapter<ProductCardViewHolder>{

    private Context context;
    List<Product> products;
    final ShoppingCart shoppingCart;

    public ProductCardAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        this.shoppingCart = ShoppingCart.getInstance();
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductCardViewHolder(vista, context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductCardViewHolder holder, int position) {
        holder.productName.setText(products.get(position).getName());
        holder.productDescription.setText(products.get(position).getDescription());
        holder.productPrice.setText( String.valueOf(products.get(position).getPrice()) + "€");
        holder.currentProduct = products.get(position);
        int amountOfProduct = shoppingCart.getAmountOfAProduct(holder.currentProduct);
        if(amountOfProduct != 0){
            holder.productAmountContainer.setVisibility(View.VISIBLE);
            holder.productAmountSelected.setText(Integer.toString(amountOfProduct));
            holder.productTotalPrice.setText( Double.toString(holder.currentProduct.getPrice() * amountOfProduct) + "€");
        }else{
            holder.productAmountContainer.setVisibility(View.GONE);
            holder.productAmountSelected.setText("0");
            holder.productTotalPrice.setText("0.0€");
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
