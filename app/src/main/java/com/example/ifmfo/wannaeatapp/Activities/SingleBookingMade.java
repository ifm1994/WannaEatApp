package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Model.Booking;
import com.example.ifmfo.wannaeatapp.Model.BookingProduct;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.R;
import com.example.ifmfo.wannaeatapp.SingleBookingProductItemAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SingleBookingMade extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    List<BookingProduct> bookingProductList;
    Booking thisBooking;
    ImageView restaurantImage;
    Restaurant thisRestaurant;
    Toolbar toolbar;
    RecyclerView productList;
    Boolean productsShowing;
    static Context context;
    TextView bookingPrice, bookingTime, bookingCommensals, bookingStatus, bookingCommentary, productsHeader, restaurantName;
    CardView writeOpinionButton, sendMessageButton;
    static RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_booking_made);

        thisBooking = (Booking) getIntent().getSerializableExtra("booking");
        globalResources.setLast_single_booking_visited(thisBooking);
        context = getApplicationContext();

        initUI();
        setupToolbar();
        bindEvents();

        getRestaurantOfThisBooking();
        decodeProductsString();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void initUI() {
        productsShowing = false;
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        restaurantName = findViewById(R.id.restaurant_name);
        restaurantImage = findViewById(R.id.restaurant_image);
        productList = findViewById(R.id.single_booking_products_container);
        productsHeader = findViewById(R.id.booking_products_label);
        productsHeader.setText("Ver pedido");
        bookingProductList = new ArrayList<>();
        bookingStatus = findViewById(R.id.booking_status);
        bookingCommensals = findViewById(R.id.booking_commensals);
        bookingPrice = findViewById(R.id.booking_price);
        bookingTime = findViewById(R.id.booking_time);
        bookingCommentary = findViewById(R.id.booking_commentary);
        writeOpinionButton = findViewById(R.id.writeOpinionButton);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        mainLayout = findViewById(R.id.main_layout);

        bookingStatus.setText(statusInString(thisBooking.getStatus()));
        bookingTime.setText(dateFormat(thisBooking.getTime()));
        bookingCommensals.setText(Integer.toString(thisBooking.getNumber_of_commensals()));
        bookingPrice.setText(String.format("%.2f",Double.parseDouble(thisBooking.getPrice())) + "€");

        if(!thisBooking.getCanrate()){
            writeOpinionButton.setVisibility(View.GONE);
        }

        if(thisBooking.getClient_commentary().isEmpty()){
            bookingCommentary.setText("Sin rellenar");
        }else{
            bookingCommentary.setText(thisBooking.getClient_commentary());
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void bindEvents() {
        productsHeader.setOnClickListener(v -> {
            if(productsShowing){
                productList.setVisibility(View.GONE);
                productsHeader.setText("Ver pedido");
                productsHeader.setBackgroundColor(getResources().getColor(R.color.orange));
                productsShowing = false;
            }else{
                productList.setVisibility(View.VISIBLE);
                productsHeader.setText("Ocultar pedido");
                productsHeader.setBackgroundColor(getResources().getColor(R.color.blackWithTransparency));
                productsShowing = true;
            }
        });

        writeOpinionButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), WriteOpinionActivity.class);
            intent.putExtra("restaurant", thisRestaurant);
            startActivityForResult(intent, 1);
        });

        sendMessageButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra("restaurant", thisRestaurant);
            intent.putExtra("comingFrom", "client");
            startActivityForResult(intent, 1);
        });
    }

    private String dateFormat(String time) {
        time = time.replace("-"," ");
        time = time.replace("_","-");
        return time;
    }

    private String statusInString(int i) {
        String result = "";
        if(i == 0){
            result = "Esperando a ser servida";
        }else if(i == 1){
            result = "Servida";
        }else if(i == -1){
            result = "Cancelada";
        }
        return result;
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_orange_back);
        getSupportActionBar().setTitle("Reserva realizada");
    }

    public void getRestaurantOfThisBooking() {
        RequestQueue requestQueue = Volley.newRequestQueue(SingleBookingMade.this);
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/restaurants/" + thisBooking.getId_restaurant();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlPeticion,
                null,
                response -> {
                    try{

                        int id = response.getInt("id");
                        String name = response.getString("name");
                        String address = response.getString("address");
                        String kindOfFood = response.getString("kind_of_food");
                        String rating = response.getString("rating");
                        String imagePath = response.getString("image_path");
                        String openningHours = response.getString("opening_hours");
                        String description = response.getString("description");
                        String phone = response.getString("phone");
                        double latitude = Double.parseDouble(response.getString("latitude"));
                        double longitude = Double.parseDouble(response.getString("longitude"));
                        int idAdmin = response.getInt("id_admin");

                        thisRestaurant = new Restaurant(id, name, address, kindOfFood, rating, imagePath, openningHours, description, phone, latitude, longitude, idAdmin);

                        restaurantName.setText(thisRestaurant.getName());
                        Picasso.get()
                                .load(thisRestaurant.getImage_path())
                                .into(restaurantImage);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Do something when error occurred
                    Snackbar.make(mainLayout ,"Error en la petición del restaurantes",Snackbar.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void decodeProductsString() {
        String productsString = thisBooking.getProducts_and_amount();
        String [] products = productsString.split(",");
        for (String product : products) {
            String[] parts = product.split(":");
            bookingProductList.add(new BookingProduct(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
        }
        fillProductList(bookingProductList);
    }

    private void fillProductList(List<BookingProduct> bookingProducts) {
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        productList.setHasFixedSize(true);
        productList.setAdapter(new SingleBookingProductItemAdapter(bookingProducts, getApplicationContext()));
        productList.setLayoutManager(layout);
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

    public static Context getContext() {
        return context;
    }

    public static void showMessage(String message){
        Snackbar.make(mainLayout ,message,Snackbar.LENGTH_SHORT).show();
    }

}
