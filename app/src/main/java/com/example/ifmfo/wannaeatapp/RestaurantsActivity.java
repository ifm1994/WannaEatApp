package com.example.ifmfo.wannaeatapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RestaurantsActivity extends AppCompatActivity{

    private String codigoPostalIntroducido;
    private int RangoDeCoincidencia = 1000;
    private String localityAddress;
    private Double latitudIntroducida;
    private Double longitudIntroducida;
    RecyclerView restaurantCardContainer;

    List <Restaurant> allRestaurants = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //Añado la toolbar
        setSupportActionBar(toolbar);
        //Añado la flecha para atras en la toolbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_back_icon);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            String direccionIntroducida = intent.getExtras().getString("direccion", "");
            try {
                calcularDatosDeDireccionIntroducida(direccionIntroducida);
                //Cambio el titulo a la toolbar
                getSupportActionBar().setTitle(localityAddress);

            } catch (IOException e) {
                Toast.makeText(this, "La dirección no ha sido encontrada", Toast.LENGTH_SHORT).show();
            }
        }
        obtenerTodosLosRestaurantes();
    }

    private void calcularDatosDeDireccionIntroducida(String direccion) throws IOException {
        Geocoder geo = new Geocoder(this);
        List<Address> address = geo.getFromLocationName(direccion, 1);
        codigoPostalIntroducido =  address.get(0).getPostalCode();
        localityAddress = address.get(0).getLocality();
        latitudIntroducida = address.get(0).getLatitude();
        longitudIntroducida = address.get(0).getLongitude();
    }

    private void obtenerTodosLosRestaurantes() {
        RequestQueue requestQueue = Volley.newRequestQueue(RestaurantsActivity.this);
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/restaurants";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
            Request.Method.GET,
            urlPeticion,
            null,
            new Response.Listener<JSONArray>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(JSONArray response) {
                    try{
                        // Loop through the array elements
                        for(int i=0;i<response.length();i++){
                            // Get current json object
                            JSONObject restaurantObject = response.getJSONObject(i);
                            // Get the current restaurant (json object) data
                            String name = restaurantObject.getString("name");
                            String address = restaurantObject.getString("address");
                            String kindOfFood = restaurantObject.getString("kind_of_food");
                            String rating = restaurantObject.getString("rating");
                            String imagePath = restaurantObject.getString("image_path");
                            String openningHours = restaurantObject.getString("opening_hours");
                            String description = restaurantObject.getString("description");
                            String phone = restaurantObject.getString("phone");
                            double latitude = Double.parseDouble(restaurantObject.getString("latitude"));
                            double longitude = Double.parseDouble(restaurantObject.getString("longitude"));

                            Restaurant restaurant = new Restaurant(name, address, kindOfFood, rating, imagePath, openningHours, description, phone, latitude, longitude);
                            allRestaurants.add(restaurant);
                        }
                        filtrarRestaurantesCercanos(allRestaurants);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    // Do something when error occurred
                    Toast.makeText(RestaurantsActivity.this, "Error en la petición de restaurantes",Toast.LENGTH_LONG).show();
                }
            }
        );
        requestQueue.add(jsonObjectRequest);

    }


    private void filtrarRestaurantesCercanos(List <Restaurant> restaurants) {

        dibujarTarjetasDeRestaurantes(restaurants);
    }


    @SuppressLint("SetTextI18n")
    private void dibujarTarjetasDeRestaurantes(List <Restaurant> restaurants){
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        restaurantCardContainer = (RecyclerView) findViewById(R.id.restaurant_card_container);
        restaurantCardContainer.setHasFixedSize(true);
        restaurantCardContainer.setAdapter(new CardRestaurantAdapter(restaurants, RestaurantsActivity.this));
        restaurantCardContainer.setLayoutManager(layout);
        //rellenar numero de resultados
        TextView number = findViewById(R.id.number_of_results);
        number.setText(Integer.toString(restaurants.size()));
    }

    //METODO para hacer aparecer los botones de la toolbar
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


//    METODO para darle funcionalidad a los botones de la toolbar
//    @SuppressLint("ShowToast")
//    public boolean onOptionsItemSelected(MenuItem menuItem){
//        switch (menuItem.getItemId()){
//            case R.id.borrar:
//                Toast.makeText(this, "Has pulsado borrar", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.editar:
//                Toast.makeText(this, "Has pulsado editar", Toast.LENGTH_SHORT).show();
//                break;
//        }
//        return true;
//    }

}
