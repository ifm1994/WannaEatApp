package com.example.ifmfo.wannaeatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Model.Product;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;

import java.util.List;

public class ProductCardAdapter extends RecyclerView.Adapter<ProductCardViewHolder>{

    private Context context;
    List<Product> products;
    final GlobalResources globalResources;

    public ProductCardAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        this.globalResources = GlobalResources.getInstance();
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductCardViewHolder(vista, context);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ProductCardViewHolder holder, int position) {
        holder.productName.setText(products.get(position).getName());
        holder.productDescription.setText(products.get(position).getDescription());
        holder.productPrice.setText( String.format("%.2f", products.get(position).getPrice() ) + "€");
        holder.currentProduct = products.get(position);
        int amountOfProduct = globalResources.shopping_basket_getAmountOfAProduct(holder.currentProduct);

        if(amountOfProduct != 0){
            holder.productAmountContainer.setVisibility(View.VISIBLE);
            holder.productAmountSelected.setText(Integer.toString(amountOfProduct));
            holder.productTotalPrice.setText( String.format("%.2f", holder.currentProduct.getPrice() * amountOfProduct) + "€");
        }else{
            holder.productAmountContainer.setVisibility(View.GONE);
            holder.productAmountSelected.setText("0");
            holder.productTotalPrice.setText("0.00€");
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
