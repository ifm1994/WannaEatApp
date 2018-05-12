package com.example.ifmfo.wannaeatapp.Model;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Activities.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Coupon {
    private int id;
    private String description;
    private int id_restaurant;
    private int id_user;
    private String category;
    private int discount;
    private String code;
    private String restaurantName;

    public Coupon(int id, String description, int id_restaurant, int id_user, String category, int discount, String code) {
        this.id = id;
        this.description = description;
        this.id_restaurant = id_restaurant;
        this.id_user = id_user;
        this.category = category;
        this.discount = discount;
        this.code = code;
        calculateRestaurantName(this.id_restaurant);
    }

    private void calculateRestaurantName(int id_restaurant) {
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.getContext());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/restaurants/" + id_restaurant;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlPeticion,
                null,
                response -> {
                    try{
                        setRestaurantName(response.getString("name"));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Do something when error occurred
                    Toast.makeText(LoginActivity.getContext(), "Error en la petición del restaurantes" + error,Toast.LENGTH_LONG).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId_restaurant() {
        return id_restaurant;
    }

    public void setId_restaurant(int id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setRestaurantName(String restaurantName){
        this.restaurantName = restaurantName;
    }

    public String getRestaurantName() {
        return this.restaurantName;
    }
}
