package com.example.ifmfo.wannaeatapp.Activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.BookingCardAdapter;
import com.example.ifmfo.wannaeatapp.Model.Booking;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyBookingsActivity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    private Toolbar toolbar;
    RecyclerView bookingCardsContainer;
    RelativeLayout bookingsEmptyContainer;
    RelativeLayout zeroBookingsContainer;
    CardView goLoginButton;
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        initUI();
        setupToolbar();
        bindEvents();

        if(globalResources.getUser_isLogged()){
            if(globalResources.getUserLogged().getUserBookings().size() != 0){
                rellenarContainerDeReservas(globalResources.getUserLogged().getUserBookings());
                showItsNecesary();
            }else{
                obtenerTodasLasReservas();
            }
        }

    }

    private void initUI() {
        toolbar = findViewById(R.id.transparent_toolbar);
        bookingsEmptyContainer = findViewById(R.id.empty_bookings_container);
        bookingCardsContainer = findViewById(R.id.booking_cards_container);
        goLoginButton = findViewById(R.id.goLoginActivityButton);
        zeroBookingsContainer = findViewById(R.id.zero_bookings_container);
        mainLayout = findViewById(R.id.main_layout);

        if(!globalResources.getUser_isLogged()){
            bookingsEmptyContainer.setVisibility(View.VISIBLE);
        }
    }

    public void showItsNecesary(){
        if( globalResources.getUserLogged().getUserBookings().size() != 0){
            bookingCardsContainer.setVisibility(View.VISIBLE);
        }else{
            zeroBookingsContainer.setVisibility(View.VISIBLE);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_action_orange_back);
    }

    private void bindEvents() {
        goLoginButton.setOnClickListener(arg0 -> {
            Intent goLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            goLoginActivity.putExtra("NavigationCode","sinceHome");
            startActivityForResult(goLoginActivity, 1);
        });
    }

    public void obtenerTodasLasReservas(){
        List<Booking> allBookingsOfUser = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/bookings/user/" + globalResources.getUserLogged().getId();
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
                            allBookingsOfUser.add(booking);
                        }
                        globalResources.getUserLogged().setUserBookings(allBookingsOfUser);
                        rellenarContainerDeReservas(allBookingsOfUser);
                        showItsNecesary();


                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Do something when error occurred
                    Snackbar.make(mainLayout ,"Error en la petición de reservas",Snackbar.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void rellenarContainerDeReservas(List <Booking> allBookingsOfUser) {
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        bookingCardsContainer.setHasFixedSize(true);
        bookingCardsContainer.setAdapter(new BookingCardAdapter(allBookingsOfUser, MyBookingsActivity.this));
        bookingCardsContainer.setLayoutManager(layout);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    //METODO para darle funcionalidad a los botones de la orange_toolbar
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case android.R.id.home:
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            default:
                super.onOptionsItemSelected(menuItem);
                break;
        }
        return true;
    }
}
