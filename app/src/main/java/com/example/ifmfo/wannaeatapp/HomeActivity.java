package com.example.ifmfo.wannaeatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    private CardView searchRestaurantsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        searchRestaurantsButton = (CardView) findViewById (R.id.searchRestaurantButton);
        searchRestaurantsButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                Intent intent = new Intent(HomeActivity.this, findRestaurantActivity.class);
                startActivity(intent);
            }
        });
    }
}
