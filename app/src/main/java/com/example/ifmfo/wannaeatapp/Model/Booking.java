package com.example.ifmfo.wannaeatapp.Model;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Activities.LoginActivity;

import org.json.JSONException;

import java.io.Serializable;

public class Booking implements Serializable {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    private int id;
    private int id_restaurant;
    private int id_user;
    private String time;
    private String price;
    private String id_transaction;
    private String products_and_amount;
    private String payment_method;
    private String client_name;
    private String client_phone;
    private String client_email;
    private int number_of_commensals;
    private String client_commentary;
    private String booking_restaurant_name;
    private Boolean canrate;
    private int status;

    public Booking(int id_restaurant,int id_user, String time, String price, String id_transaction, String products_and_amount, String payment_method, String client_name, String client_phone, String client_email, int number_of_commensals, String client_commentary, Boolean canrate, int status) {
        this.id_restaurant = id_restaurant;
        this.id_user = id_user;
        this.time = time;
        this.price = price;
        this.id_transaction = id_transaction;
        this.products_and_amount = products_and_amount;
        this.payment_method = payment_method;
        this.client_name = client_name;
        this.client_phone = client_phone;
        this.client_email = client_email;
        this.number_of_commensals = number_of_commensals;
        this.client_commentary = client_commentary;
        this.canrate = canrate;
        this.status = status;
        setBooking_restaurant_name(globalResources.getNameOfThisRestaurant(id_restaurant));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId_transaction() {
        return id_transaction;
    }

    public void setId_transaction(String id_transaction) {
        this.id_transaction = id_transaction;
    }

    public String getProducts_and_amount() {
        return products_and_amount;
    }

    public void setProducts_and_amount(String products_and_amount) {
        this.products_and_amount = products_and_amount;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public int getNumber_of_commensals() {
        return number_of_commensals;
    }

    public void setNumber_of_commensals(int number_of_commensals) {
        this.number_of_commensals = number_of_commensals;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }

    public String getClient_commentary() {
        return client_commentary;
    }

    public void setClient_commentary(String client_commentary) {
        this.client_commentary = client_commentary;
    }

    public void setBooking_restaurant_name(String booking_restaurant_name){
        this.booking_restaurant_name = booking_restaurant_name;
    }

    public String getBooking_restaurant_name() {
        return this.booking_restaurant_name;
    }

    public Boolean getCanrate() {
        return canrate;
    }

    public void setCanrate(Boolean canrate) {
        this.canrate = canrate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
