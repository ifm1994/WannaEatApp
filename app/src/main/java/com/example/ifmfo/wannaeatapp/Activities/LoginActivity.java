package com.example.ifmfo.wannaeatapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Model.Coupon;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.Model.User;
import com.example.ifmfo.wannaeatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText loginEmailInput;
    EditText loginPasswordInput;
    CardView loginButton;
    TextView registerLink;
    TextView loginLayout;
    String comingFrom;
    static final GlobalResources globalResources = GlobalResources.getInstance();
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        setupToolbar();
        bindEvents();

        if(getIntent().getExtras() != null){
            comingFrom = getIntent().getExtras().getString("NavigationCode");
        }
        context = getApplicationContext();
    }

    private void initUI() {
        toolbar = findViewById(R.id.transparent_toolbar);
        loginEmailInput = findViewById(R.id.loginEmailInput);
        loginPasswordInput = findViewById(R.id.loginPasswordInput);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);
        loginLayout = findViewById(R.id.login_label);
        comingFrom = "";
    }


    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_action_back);
            getSupportActionBar().setTitle("Iniciar sesión");
        }
    }

    private void bindEvents() {
        loginButton.setOnClickListener(arg0 -> {
            if(loginEmailInput.getText().toString().equals("") || loginEmailInput.getText().toString().equals(" ") || loginPasswordInput.getText().toString().equals("") || loginPasswordInput.getText().toString().equals(" ")){
                //Hacer peticion al servidor para ver si existe dicho usuario
                Toast.makeText(getApplicationContext(), "Debe rellenar su correo y su contraseña",Toast.LENGTH_SHORT).show();
            }else{
                //Ahora verifico que existe dicho usuario
                verificarUsuario(loginEmailInput.getText().toString(), loginPasswordInput.getText().toString());
            }
        });
    }

    private void verificarUsuario(String emailEntered, String passwordEntered){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/users/" + emailEntered + "/" + passwordEntered;
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
                        User user = new User(id, name, email, phone);

                        globalResources.user_logIn(user);
                        obtenerCuponesDelUsuario(user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Do something when error occurred
                    Snackbar.make( loginLayout,"Email o contraseña incorrectas",Snackbar.LENGTH_LONG).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void obtenerCuponesDelUsuario(User user) {
        List<Coupon> allCouponsOfUser = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/coupons/user/" + user.getId();
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
                            String description = restaurantObject.getString("description");
                            int id_restaurant = restaurantObject.getInt("id_restaurant");
                            int id_user = restaurantObject.getInt("id_user");
                            String category = restaurantObject.getString("category");
                            int discount = restaurantObject.getInt("discount");
                            String code = restaurantObject.getString("code");

                            Coupon coupon = new Coupon(id, description, id_restaurant, id_user,category, discount, code);
                            allCouponsOfUser.add(coupon);
                        }
                        globalResources.getUserLogged().setUserCoupons(allCouponsOfUser);

                        if(comingFrom.equals("sinceHome")){
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        }else if(comingFrom.equals("sinceBasket")){
                            Intent intent = new Intent(getApplicationContext(), FullShoppingBasketActivity.class);
                            startActivity(intent);
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Do something when error occurred
                    Toast.makeText(getApplicationContext(), "Error en la petición de cupones",Toast.LENGTH_LONG).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    //METODO para darle funcionalidad a los botones de la orange_toolbar
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
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
}


























