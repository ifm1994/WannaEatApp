package com.example.ifmfo.wannaeatapp;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class RestaurantActivity extends AppCompatActivity {

    Restaurant thisRestaurant;
    TextView restaurantName;
    ImageView restaurantImage;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        thisRestaurant = (Restaurant) getIntent().getSerializableExtra("restaurantObject");
        restaurantName = findViewById(R.id.single_restaurant_name);
        restaurantImage = findViewById(R.id.single_restaurant_image);

        Toolbar toolbar = findViewById(R.id.transparent_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            toolbar.setNavigationIcon(R.drawable.ic_action_back_icon);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        restaurantName.setText(thisRestaurant.getName());
        Picasso.get()
                .load(thisRestaurant.getImage_path())
                .into(restaurantImage);

    }
}
