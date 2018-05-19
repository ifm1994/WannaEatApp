package com.example.ifmfo.wannaeatapp.Activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.ClientChatCardAdapter;
import com.example.ifmfo.wannaeatapp.Model.Booking;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.Model.User;
import com.example.ifmfo.wannaeatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.ifmfo.wannaeatapp.R.drawable.ic_action_orange_back;

public class ClientChatsActivity extends AppCompatActivity {

    private static final GlobalResources globalResources = GlobalResources.getInstance();
    Toolbar toolbar;
    LinearLayout mainLayout;
    List<Booking> allBookingsOfRestaurant;
    List<User> allUsersOfBookings;
    RecyclerView clientsChatCardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_chats);

        initUI();
        setupToolbar();
        bindEvets();

        obtenerReservas();
    }

    private void initUI() {
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        mainLayout = findViewById(R.id.main_layout);
        allBookingsOfRestaurant = new ArrayList<>();
        allUsersOfBookings = new ArrayList<>();
        clientsChatCardContainer = findViewById(R.id.clients_chat_card_container);
    }

    private void bindEvets() {

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(ic_action_orange_back);
        getSupportActionBar().setTitle("Chats");
    }

    private void obtenerReservas() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/bookings/restaurant/" + globalResources.getUserLogged().getId();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlPeticion,
                null,
                response -> {
                    try{
                        // Loop through the array elements
                        for(int i=0;i<response.length();i++){
                            // Get current json object
                            JSONObject restaurantObject = response.getJSONObject(i);
                            // Get the current restaurant (json object) data
                            int id = restaurantObject.getInt("id");
                            int id_restaurant = restaurantObject.getInt("id_restaurant");
                            int id_user = restaurantObject.getInt("id_user");
                            String time = restaurantObject.getString("time");
                            String price = restaurantObject.getString("price");
                            String id_transaction = restaurantObject.getString("id_transaction");
                            String products_and_amount = restaurantObject.getString("products_and_amount");
                            String payment_method = restaurantObject.getString("payment_method");
                            String client_name = restaurantObject.getString("client_name");
                            String client_phone = restaurantObject.getString("client_phone");
                            String client_email = restaurantObject.getString("client_email");
                            int number_of_commensals = restaurantObject.getInt("number_of_commensals");
                            String client_commentary = restaurantObject.getString("client_commentary");
                            Boolean canrate = restaurantObject.getBoolean("canrate");
                            int status = restaurantObject.getInt("status");

                            Booking booking = new Booking(id_restaurant, id_user, time, price, id_transaction, products_and_amount, payment_method, client_name, client_phone, client_email, number_of_commensals, client_commentary, canrate, status);
                            booking.setId(id);
                            allBookingsOfRestaurant.add(booking);
                        }
                        getUsersOfBookings();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Do something when error occurred
                    Snackbar.make(mainLayout ,"Error en la petición de reservas de este restaurante",Snackbar.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void getUsersOfBookings(){
        Boolean isLast = false;
        for(int i = 0; i < allBookingsOfRestaurant.size(); i++ ){
            if(i == allBookingsOfRestaurant.size() -1){
                isLast = true;
            }
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String urlPeticion = "https://wannaeatservice.herokuapp.com/api/users/" + allBookingsOfRestaurant.get(i).getId_user();
            Boolean finalIsLast = isLast;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    urlPeticion,
                    null,
                    response -> {
                        try{
                            // Get the current restaurant (json object) data
                            int id = response.getInt("id");
                            String name = response.getString("name");
                            String email = response.getString("email");
                            String phone = response.getString("phone");
                            String ftoken = response.getString("ftoken");

                            User user = new User(name, email, phone);
                            user.setId(id);
                            user.setFirebaseToken(ftoken);
                            allUsersOfBookings.add(user);
                            if(finalIsLast){
                                drawListOfChats();
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        // Do something when error occurred
                        Snackbar.make(mainLayout ,"Error a la hora de pedir los usuarios",Snackbar.LENGTH_SHORT).show();
                    }
            );
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void drawListOfChats() {
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        clientsChatCardContainer.setHasFixedSize(true);
        clientsChatCardContainer.setAdapter(new ClientChatCardAdapter(ClientChatsActivity.this, allBookingsOfRestaurant, allUsersOfBookings));
        clientsChatCardContainer.setLayoutManager(layout);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    //METODO para darle funcionalidad a los botones de la orange_toolbar
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                super.onOptionsItemSelected(menuItem);
                break;
        }
        return true;
    }
}
