package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.RestaurantCardAdapter;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.R;
import com.example.ifmfo.wannaeatapp.SearchFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RestaurantsActivity extends AppCompatActivity{

    static final GlobalResources globalResources = GlobalResources.getInstance();
    private String localityAddress;
    SearchFilter filterSelected;
    RecyclerView restaurantCardContainer;
    Geocoder geocoder;
    Toolbar toolbar;
    LinearLayout mainLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        initUI();
        setupToolbar();

        if(getIntent().getExtras() != null){
            localityAddress = getIntent().getStringExtra("locality");
            Objects.requireNonNull(getSupportActionBar()).setTitle(localityAddress);
        }
        try {
            dibujarTarjetasDeRestaurantes(filtrarRestaurantesCercanos(globalResources.getFoundRestaurnts(),filterSelected));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        mainLayout = findViewById(R.id.main_layout);
        filterSelected = SearchFilter.LOCALITY;
        geocoder = new Geocoder(this);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        //Añado la orange_toolbar
        setSupportActionBar(toolbar);
        //Añado la flecha para atras en la orange_toolbar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private List <Restaurant> filtrarRestaurantesCercanos(List <Restaurant> restaurants, SearchFilter filterSelected) throws IOException {
        List<Restaurant> listOfRestaurantsFiltered = new ArrayList<>();
        List<Address> currentAddress = geocoder.getFromLocationName(globalResources.getSession_addressEntered(), 1);
        for(Restaurant restaurant: restaurants){
            //Filtro por misma localidad
            if(filterSelected == SearchFilter.LOCALITY){
                List<Address> restaurantAddress = geocoder.getFromLocationName(restaurant.getAddress(), 1);
                if(currentAddress.get(0).getLocality().equals(restaurantAddress.get(0).getLocality())){
                    listOfRestaurantsFiltered.add(restaurant);
                }
            }
        }
        return listOfRestaurantsFiltered;
    }

    @SuppressLint("SetTextI18n")
    private void dibujarTarjetasDeRestaurantes(List <Restaurant> restaurants){
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        restaurantCardContainer = findViewById(R.id.restaurant_card_container);
        restaurantCardContainer.setHasFixedSize(true);
        restaurantCardContainer.setAdapter(new RestaurantCardAdapter(restaurants, RestaurantsActivity.this));
        restaurantCardContainer.setLayoutManager(layout);

        //rellenar numero de resultados
        TextView number = findViewById(R.id.number_of_results);
        number.setText(Integer.toString(restaurants.size()));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("direccionAutomatica", globalResources.getSession_addressEntered());
        startActivityForResult(intent, 2);
    }

    //METODO para darle funcionalidad a los botones de la orange_toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("direccionAutomatica", globalResources.getSession_addressEntered());
                startActivityForResult(intent, 2);
                break;
            default:
                super.onOptionsItemSelected(menuItem);
                break;
        }
        return true;
    }

    //METODO para crear un toolbar personalizado
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.restaurants_searcher_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && data.getExtras() != null){
            getSupportActionBar().setTitle(localityAddress);
        }
    }

}
