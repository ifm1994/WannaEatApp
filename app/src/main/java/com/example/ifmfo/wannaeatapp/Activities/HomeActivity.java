package com.example.ifmfo.wannaeatapp.Activities;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.R;
import com.google.firebase.auth.FirebaseAuth;


public class HomeActivity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navView;
    private EditText inputDireccion;
    CardView searchRestaurantsButton;
    RelativeLayout mainHomeLayout;
    TextView userNameLoggedLabel;
    TextView userEmailLoggedLabel;
    TextView restaurantAddressInput;
    TextView loginLabel;
    TextView textSearchButton;

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

                if(globalResources.getSetClientViewOfApp()){
                    Snackbar.make(mainHomeLayout ,"Bienvenida/o " + globalResources.getUserLogged().getName() + "!",Snackbar.LENGTH_LONG).show();
                }else{
                    Snackbar.make(mainHomeLayout ,"Bienvenida/o " + globalResources.getUserLogged().getEmail() + "!",Snackbar.LENGTH_LONG).show();
                }
            }

            globalResources.getNavigationView().getMenu().findItem(R.id.log_in_out_button).setTitle("Cerrar sesión");
            userNameLoggedLabel.setText(globalResources.getUserLogged().getName());
            userEmailLoggedLabel.setText(globalResources.getUserLogged().getEmail());
        }


    }

    private void initUI() {
        globalResources.setNavigationView(findViewById(R.id.navview));
        inputDireccion = findViewById(R.id.restaurantAddressInput);
        searchRestaurantsButton = findViewById(R.id.searchRestaurantButton);
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.navview);
        toolbar = findViewById(R.id.transparent_toolbar);
        mainHomeLayout = findViewById(R.id.home_main_layout);
        restaurantAddressInput = findViewById(R.id.RestaurantAddressLabel);
        loginLabel = findViewById(R.id.login_label);
        textSearchButton = findViewById(R.id.text_search_button);

        inputDireccion.setText(globalResources.getSession_currentAddress());
        inputDireccion.setSelection(inputDireccion.getText().length());

        navView.setItemIconTintList(null);
        globalResources.getNavigationView().getMenu().findItem(R.id.restaurants_section_button).setChecked(true);

        userNameLoggedLabel = globalResources.getNavigationView().getHeaderView(0).findViewById(R.id.user_name_logged);
        userEmailLoggedLabel= globalResources.getNavigationView().getHeaderView(0).findViewById(R.id.user_email_logged);

        if(!globalResources.getSetClientViewOfApp() && globalResources.getUserLogged() != null){
            inputDireccion.setVisibility(View.GONE);
            restaurantAddressInput.setVisibility(View.GONE);
            loginLabel.setVisibility(View.GONE);
            textSearchButton.setText("Ver chat con los clientes");
            //Oculto tambien las opciones del menú que son para el cliente, o sea, todas menos configuración
            globalResources.getNavigationView().getMenu().findItem(R.id.restaurants_section_button).setTitle("Home");
            globalResources.getNavigationView().getMenu().findItem(R.id.coupons_section_button).setVisible(false);
            globalResources.getNavigationView().getMenu().findItem(R.id.bookings_section_button).setVisible(false);
        }else if(globalResources.getSetClientViewOfApp() && globalResources.getUserLogged() != null){
            inputDireccion.setVisibility(View.VISIBLE);
            restaurantAddressInput.setVisibility(View.VISIBLE);
            loginLabel.setVisibility(View.VISIBLE);
            textSearchButton.setText("Buscar restaurantes");
            globalResources.getNavigationView().getMenu().findItem(R.id.restaurants_section_button).setTitle("Restaurantes");
            globalResources.getNavigationView().getMenu().findItem(R.id.coupons_section_button).setVisible(true);
            globalResources.getNavigationView().getMenu().findItem(R.id.bookings_section_button).setVisible(true);
        }

    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindEvents() {

        if(!globalResources.getSetClientViewOfApp()){
            searchRestaurantsButton.setOnClickListener(arg0 -> {
                startActivityForResult(new Intent(HomeActivity.this, ClientChatsActivity.class), 1);
            });
        }else{
            searchRestaurantsButton.setOnClickListener(arg0 -> {
                if(inputDireccion.getText().length() != 0){
                    Intent intent = new Intent(HomeActivity.this, RestaurantsActivity.class);
                    intent.putExtra("direccion", inputDireccion.getText().toString());
                    startActivityForResult(intent, 1);
                }else{
                    Snackbar.make(mainHomeLayout ,"Debe rellenar una dirección",Snackbar.LENGTH_LONG).show();
                }
            });
        }

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
                            if(globalResources.getUserLogged().getUserCoupons() != null){
                                globalResources.getUserLogged().getUserCoupons().clear();
                                globalResources.getUserLogged().getUserBookings().clear();
                            }
                            globalResources.user_logOut();
                            FirebaseAuth.getInstance().signOut();
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
