package com.example.ifmfo.wannaeatapp.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.Model.User;
import com.example.ifmfo.wannaeatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LoadingAppActivity extends AppCompatActivity {

    private Boolean direccionAutoCompletada = false;
    static final GlobalResources globalResources = GlobalResources.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_app);

        mAuth = FirebaseAuth.getInstance();

        obtenerTodosLosRestaurantes();

    }

    private void obtenerTodosLosRestaurantes() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/restaurants";
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

                            Restaurant restaurant = new Restaurant(id, name, address, kindOfFood, rating, imagePath, openningHours, description, phone, latitude, longitude);
                            globalResources.addFoundRestaurant(restaurant);
                        }
                        checkIfLoggedIn();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Do something when error occurred
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void checkIfLoggedIn(){

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            String urlPeticion = "https://wannaeatservice.herokuapp.com/api/user/email/" + currentUser.getEmail();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    urlPeticion,
                    null,
                    response -> {
                        try {
                            int id = response.getInt("id");
                            String name = response.getString("name");
                            String email = response.getString("email");
                            String phone = response.getString("phone");
                            User user = new User(name, email, phone);
                            user.setId(id);
                            globalResources.user_logIn(user);
                            detectLocation();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {

                    }
            );
            requestQueue.add(jsonObjectRequest);
        }else{
            detectLocation();
        }
    }

    private void detectLocation(){
        /* Uso de la clase LocationManager para obtener la localización del GPS */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Si no tengo los permisos necesarios los solicito
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            //Si tengo los permisos, pues consigo la ubicacion actual
            locationStart();
        }
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        assert mlocManager != null;
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            new AlertDialog.Builder(this)
                .setTitle("GPS Desactivado")
                .setMessage("¿Quieres activarlo para un mejor funcionamiento?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(settingsIntent);
                    }
                }).setNegativeButton("No", null).show();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
            }
        }
    }

    public class Localizacion implements LocationListener {
        LoadingAppActivity mainActivity;

        void setMainActivity(LoadingAppActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();

            setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            AlertDialog.Builder alert = new AlertDialog.Builder(LoadingAppActivity.this);
            alert.setTitle("GPS Desactivado");
            alert.setMessage("El GPS está desactivado, se recomienda activarlo para un mejor funcionamiento de la aplicación");
            alert.setPositiveButton("OK",null);
            alert.show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            AlertDialog.Builder alert = new AlertDialog.Builder(LoadingAppActivity.this);
            alert.setTitle("GPS Activado");
            alert.setMessage("El GPS ha sido activado");
            alert.setPositiveButton("OK",null);
            alert.show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    if(!direccionAutoCompletada){
                        direccionAutoCompletada = true;
                        Intent homeIntent = new Intent(LoadingAppActivity.this, HomeActivity.class);
                        globalResources.setSession_currentAddress(DirCalle.getAddressLine(0));

                        startActivity(homeIntent);
                        finish();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/*

final float scale = getContext().getResources().getDisplayMetrics().density;
int pixels = (int) (200 * scale + 0.5f);

 */