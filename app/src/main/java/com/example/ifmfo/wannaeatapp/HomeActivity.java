package com.example.ifmfo.wannaeatapp;

import android.Manifest;
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
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private EditText inputDireccion;
//    private CardView botonUbicacionActual;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        inputDireccion = (EditText) findViewById(R.id.restaurantAddressInput);

        if(intent.getExtras() != null){
            inputDireccion.setText(Objects.requireNonNull(intent.getExtras()).getString("direccionAutomatica", ""));
        }

        inputDireccion.setSelection(inputDireccion.getText().length());
        CardView searchRestaurantsButton = findViewById(R.id.searchRestaurantButton);
//        botonUbicacionActual = (CardView) findViewById(R.id.locationAdvice);

        searchRestaurantsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                if(inputDireccion.getText().length() != 0){
                    Intent intent = new Intent(HomeActivity.this, RestaurantsActivity.class);
                    intent.putExtra("direccion", inputDireccion.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(HomeActivity.this, "Debe rellenar una dirección",Toast.LENGTH_SHORT).show();
                }
            }
        });

        inputDireccion.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
            if(inputDireccion.getText().length() == 0){
//              botonUbicacionActual.setVisibility(View.VISIBLE);
            }
            }
        });

    }
}
