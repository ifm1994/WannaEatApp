package com.example.ifmfo.wannaeatapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.R;

import java.util.Objects;

public class EmptyShoppingBasketActivity extends AppCompatActivity {

    Toolbar toolbar;
    Restaurant lastRestaurantVisited;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_shopping_basket);

        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        lastRestaurantVisited = (Restaurant) getIntent().getSerializableExtra("restaurant");
        CardView startSearch = findViewById(R.id.startSearchButton);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_action_orange_back);
            getSupportActionBar().setTitle("Pedido");
        }


        startSearch.setOnClickListener(arg0 -> {
            Intent intent = new Intent(EmptyShoppingBasketActivity.this, HomeActivity.class);
            startActivityForResult(intent, 1);
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
        intent.putExtra("restaurantObject", lastRestaurantVisited);
        startActivityForResult(intent, 1);
    }

    //METODO para darle funcionalidad a los botones de la orange_toolbar
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
                intent.putExtra("restaurantObject", lastRestaurantVisited);
                startActivityForResult(intent, 1);
                break;
            default:
                super.onOptionsItemSelected(menuItem);
                break;
        }
        return true;
    }
}
