package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;


public class HomeActivity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navView;
    private EditText inputDireccion;
    CardView searchRestaurantsButton;
    RelativeLayout mainHomeLayout;

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
        setupToolbar();
        bindEvents();

        if(globalResources.getUser_isLogged()){
            if(!globalResources.wellcomeMessageIsShowed()){
                globalResources.setWellcomeMessageTo(true);
                Snackbar.make(mainHomeLayout ,"Bienvenida/o " + globalResources.getUserLogged().getName() + "!",Snackbar.LENGTH_LONG).show();
            }

            globalResources.getNavigationView().getMenu().findItem(R.id.log_in_out_button).setTitle("Cerrar sesión");
            TextView userNameLoggedLabel = globalResources.getNavigationView().getHeaderView(0).findViewById(R.id.user_name_logged);
            TextView userEmailLoggedLabel= globalResources.getNavigationView().getHeaderView(0).findViewById(R.id.user_email_logged);
            userNameLoggedLabel.setText(globalResources.getUserLogged().getName());
            userEmailLoggedLabel.setText(globalResources.getUserLogged().getEmail());
        }

    }

    private void initUI() {
        inputDireccion = findViewById(R.id.restaurantAddressInput);
        searchRestaurantsButton = findViewById(R.id.searchRestaurantButton);
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navview);
        toolbar = findViewById(R.id.transparent_toolbar);
        mainHomeLayout = findViewById(R.id.home_main_layout);

        inputDireccion.setText(globalResources.getSession_currentAddress());
        inputDireccion.setSelection(inputDireccion.getText().length());

        navView.setItemIconTintList(null);
        globalResources.setNavigationView(findViewById(R.id.navview));
        globalResources.getNavigationView().getMenu().findItem(R.id.restaurants_section_button).setChecked(true);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindEvents() {
        searchRestaurantsButton.setOnClickListener(arg0 -> {
            if(inputDireccion.getText().length() != 0){
                Intent intent = new Intent(HomeActivity.this, RestaurantsActivity.class);
                intent.putExtra("direccion", inputDireccion.getText().toString());
                startActivityForResult(intent, 1);
            }else{
                Snackbar.make(mainHomeLayout ,"Debe rellenar una dirección",Snackbar.LENGTH_LONG).show();
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.bookings_section_button:
                        startActivityForResult(new Intent(HomeActivity.this, MyBookingsActivity.class), 1);
                        break;
                    case R.id.coupons_section_button:
                        startActivityForResult(new Intent(HomeActivity.this, MyCouponsActicity.class), 1);
                        break;
                    case R.id.configuration_button:
                        startActivityForResult(new Intent(HomeActivity.this, ConfigurationActivity.class), 1);
                        break;
                    case R.id.log_in_out_button:
                        if(globalResources.getUser_isLogged()){
                            globalResources.getUserLogged().getUserCoupons().clear();
                            globalResources.getUserLogged().getUserBookings().clear();
                            globalResources.user_logOut();
                            globalResources.getNavigationView().getMenu().findItem(R.id.log_in_out_button).setTitle("Iniciar sesión");
                            TextView userNameLoggedLabel = globalResources.getNavigationView().getHeaderView(0).findViewById(R.id.user_name_logged);
                            TextView userEmailLoggedLabel= globalResources.getNavigationView().getHeaderView(0).findViewById(R.id.user_email_logged);
                            Snackbar.make(mainHomeLayout ,"Hasta pronto " + userNameLoggedLabel.getText().toString() + "!",Snackbar.LENGTH_LONG).show();
                            userNameLoggedLabel.setText("");
                            userEmailLoggedLabel.setText("");
                        }else{
                            Intent goLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                            goLoginActivity.putExtra("NavigationCode","sinceHome");
                            startActivityForResult(goLoginActivity, 1);
                        }
                        break;
                }
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        inputDireccion.setText(globalResources.getSession_currentAddress());
        globalResources.getNavigationView().getMenu().findItem(R.id.restaurants_section_button).setChecked(true);
    }
}
