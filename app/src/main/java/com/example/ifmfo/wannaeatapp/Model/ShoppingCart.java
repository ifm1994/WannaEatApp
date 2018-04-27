package com.example.ifmfo.wannaeatapp.Model;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart extends Application{

    private static ShoppingCart instance;

    private int numberOfProductsAdded;
    private Map<Product, Integer> productsAdded; //Producto - Cantidad
    private double totalPrice;

    public ShoppingCart() {
        numberOfProductsAdded = 0;
        productsAdded = new HashMap<>();
        totalPrice = 0;
    }

    public static synchronized ShoppingCart getInstance(){
        if(instance == null){
            instance = new ShoppingCart();
        }
        return instance;
    }

    public void addProduct(Product product){
        if(productsAdded.containsKey(product)){
            productsAdded.put(product, productsAdded.get(product) + 1);
        }else{
            productsAdded.put(product, 1);
        }

        totalPrice += product.getPrice();
        numberOfProductsAdded++;
        Log.i("info.valor","" + product.getPrice());
        Log.i("info.total","" + getTotalPrice());
    }

    public void emptyShoppingCart(){
        productsAdded.clear();
        totalPrice = 0;
        numberOfProductsAdded = 0;
    }

    public void removeProduct(Product product){
        if(productsAdded.containsKey(product)){
            if(productsAdded.get(product) > 1){
                productsAdded.put(product, productsAdded.get(product) - 1);
            }else{
                productsAdded.remove(product);
            }
            totalPrice -= product.getPrice();
            numberOfProductsAdded--;
        }
    }

    //Si se trata de añadir un producto de un restaurante diferente a los productos ya añadidos, se
    //mostrará un mensaje, si acepta se vacía la "cesta" y se añade el nuevo producto, si deniega,
    //se cancela el añadir el producto seleccionado.
    public boolean canAddProduct(Product product){
        for (Map.Entry<Product, Integer> entry : productsAdded.entrySet()) {
            if(product.getId_restaurant() != entry.getKey().getId_restaurant()){
                return false;
            }
        }
        return true;
    }

    public int getNumberOfProductsAdded() {
        return numberOfProductsAdded;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getAmountOfAProduct(Product product){
        if(productsAdded.get(product) == null){
            return 0;
        }else{
            return productsAdded.get(product);
        }
    }

    public double getTotalPriceOfAProduct(Product product){
        if(productsAdded.get(product) == null){
            return 0;
        }else{
            return product.getPrice() * productsAdded.get(product);
        }
    }

    @Override
    public String toString() {
        StringBuilder resultado = new StringBuilder();
        for (Map.Entry<Product, Integer> entry : productsAdded.entrySet()){
            resultado.append("{").append(entry.getKey().getName()).append(" - ").append(Integer.toString(entry.getValue())).append("}");
        }
        return resultado.toString();
    }

}
