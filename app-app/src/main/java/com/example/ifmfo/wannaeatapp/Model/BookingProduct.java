package com.example.ifmfo.wannaeatapp.Model;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Activities.SingleBookingMade;

import org.json.JSONException;

public class BookingProduct {

    private String name;
    private int id;
    private int amount;

    public BookingProduct(int id, int amount) {
        this.id = id;
        this.amount = amount;
        calculateProductName(id);
    }

    private void calculateProductName(int id){
        RequestQueue requestQueue = Volley.newRequestQueue(SingleBookingMade.getContext());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/products/" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlPeticion,
                null,
                response -> {
                    try{
                        setName(response.getString("name"));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Do something when error occurred
                    SingleBookingMade.showMessage("Error en la petición del restaurantes");
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
