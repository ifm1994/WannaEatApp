package com.example.ifmfo.wannaeatapp.Model;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalResources extends Application{

    private static GlobalResources instance;

    private int shopping_basket_numberOfProductsAdded;
    private Map<Product, Integer> shopping_basket_productsAdded; //Producto - Cantidad
    private double shopping_basket_totalPrice;
    private boolean user_isLogged;
    private User user;
    private String session_currentAddress;
    private String session_addressEntered;

    public GlobalResources() {
        shopping_basket_numberOfProductsAdded = 0;
        shopping_basket_productsAdded = new HashMap<>();
        shopping_basket_totalPrice = 0;
        user_isLogged = false;
        user = null;
    }

    public static synchronized GlobalResources getInstance(){
        if(instance == null){
            instance = new GlobalResources();
        }
        return instance;
    }

    public void user_logIn(User user){
        this.user = user;
        setUser_isLogged(true);
    }

    public void user_logOut(){
        this.user = null;
        setUser_isLogged(false);
    }

    public List <Product> shopping_basket_getAllDiferentsProducts(){
        List <Product> allDiferentsProducts = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : shopping_basket_productsAdded.entrySet()){
            allDiferentsProducts.add(entry.getKey());
        }
        return allDiferentsProducts;
    }

    public int shopping_basket_getAmountOfDiferentsProduct(){
        return shopping_basket_productsAdded.size();
    }

    public void shopping_basket_addProduct(Product product){
        if(shopping_basket_productsAdded.containsKey(product)){
            shopping_basket_productsAdded.put(product, shopping_basket_productsAdded.get(product) + 1);
        }else{
            shopping_basket_productsAdded.put(product, 1);
        }

        shopping_basket_totalPrice += product.getPrice();
        shopping_basket_numberOfProductsAdded++;
    }

    public void shopping_basket_emptyShoppingCart(){
        shopping_basket_productsAdded.clear();
        shopping_basket_totalPrice = 0;
        shopping_basket_numberOfProductsAdded = 0;
    }

    public void shopping_basket_removeProduct(Product product){
        if(shopping_basket_productsAdded.containsKey(product)){
            if(shopping_basket_productsAdded.get(product) > 1){
                shopping_basket_productsAdded.put(product, shopping_basket_productsAdded.get(product) - 1);
            }else{
                shopping_basket_productsAdded.remove(product);
            }
            shopping_basket_totalPrice -= product.getPrice();
            shopping_basket_numberOfProductsAdded--;
        }
    }

    //Si se trata de añadir un producto de un restaurante diferente a los productos ya añadidos, se
    //mostrará un mensaje, si acepta se vacía la "cesta" y se añade el nuevo producto, si deniega,
    //se cancela el añadir el producto seleccionado.
    public boolean shopping_basket_canAddProduct(Product product){
        for (Map.Entry<Product, Integer> entry : shopping_basket_productsAdded.entrySet()) {
            if(product.getId_restaurant() != entry.getKey().getId_restaurant()){
                return false;
            }
        }
        return true;
    }

    public int shopping_basket_getNumberOfProductsAdded() {
        return shopping_basket_numberOfProductsAdded;
    }

    public double shopping_basket_getTotalPrice() {
        return shopping_basket_totalPrice;
    }

    public int shopping_basket_getAmountOfAProduct(Product product){
        for (Map.Entry<Product, Integer> entry : shopping_basket_productsAdded.entrySet()) {
            if(product.getId() == entry.getKey().getId()){
                return entry.getValue();
            }
        }
        return 0;
    }

    public double shopping_basket_getTotalPriceOfAProduct(Product product){
        if(shopping_basket_productsAdded.get(product) == null){
            return 0;
        }else{
            return product.getPrice() * shopping_basket_productsAdded.get(product);
        }
    }

    public String shopping_basket_toString() {
        StringBuilder resultado = new StringBuilder();
        for (Map.Entry<Product, Integer> entry : shopping_basket_productsAdded.entrySet()){
            resultado.append("{").append(entry.getKey().getName()).append(" - ").append(Integer.toString(entry.getValue())).append("}");
        }
        return resultado.toString();
    }

    public String getSession_currentAddress() {
        return session_currentAddress;
    }

    public void setSession_currentAddress(String session_currentAddress) {
        this.session_currentAddress = session_currentAddress;
    }

    public String getSession_addressEntered() {
        return session_addressEntered;
    }

    public void setSession_addressEntered(String session_addressEntered) {
        this.session_addressEntered = session_addressEntered;
    }

    public boolean getUser_isLogged() {
        return user_isLogged;
    }

    public void setUser_isLogged(boolean user_isLogged) {
        this.user_isLogged = user_isLogged;
    }
}
