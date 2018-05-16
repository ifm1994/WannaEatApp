package com.example.ifmfo.wannaeatapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.CouponCardAdapter;
import com.example.ifmfo.wannaeatapp.Model.Coupon;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyCouponsActicity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    private Toolbar toolbar;
    RecyclerView couponCardsContainer;
    RelativeLayout couponsEmptyContainer;
    RelativeLayout zeroCouponsContainer;
    CardView goLoginButton;
    LinearLayout redeemCouponContainer;
    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupons);

        initUI();
        setupToolbar();
        bindEvents();

        if(globalResources.getUser_isLogged()){
            obtenerCuponesDelUsuario();
        }
    }

    private void initUI() {
        toolbar = findViewById(R.id.transparent_toolbar);
        couponsEmptyContainer = findViewById(R.id.empty_coupons_container);
        couponCardsContainer = findViewById(R.id.coupon_cards_container);
        goLoginButton = findViewById(R.id.goLoginActivityButton);
        zeroCouponsContainer = findViewById(R.id.zero_coupons_container);
        redeemCouponContainer = findViewById(R.id.redeem_coupon_container);
        mainLayout = findViewById(R.id.main_layout);

        if(!globalResources.getUser_isLogged()){
            couponsEmptyContainer.setVisibility(View.VISIBLE);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_action_orange_back);
    }

    private void bindEvents() {
        goLoginButton.setOnClickListener(arg0 -> {
            Intent goLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            goLoginActivity.putExtra("NavigationCode","sinceHome");
            startActivityForResult(goLoginActivity, 1);
        });
    }

    public void showItIfIsNecesary(){
        redeemCouponContainer.setVisibility(View.VISIBLE);
        if( globalResources.getUserLogged().getUserCoupons().size() != 0){
            couponCardsContainer.setVisibility(View.VISIBLE);
        }else{
            zeroCouponsContainer.setVisibility(View.VISIBLE);
        }
    }

    private void obtenerCuponesDelUsuario() {
        List<Coupon> allCouponsOfUser = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/coupons/user/" + globalResources.getUserLogged().getId();
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
                        rellenarContainerDeCuponer(globalResources.getUserLogged().getUserCoupons());
                        showItIfIsNecesary();

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Do something when error occurred
                    Snackbar.make(mainLayout ,"Error en la petición de cupones",Snackbar.LENGTH_SHORT).show();
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void rellenarContainerDeCuponer(List <Coupon> cuponsOfThisUser) {
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        couponCardsContainer.setHasFixedSize(true);
        couponCardsContainer.setAdapter(new CouponCardAdapter(cuponsOfThisUser, MyCouponsActicity.this));
        couponCardsContainer.setLayoutManager(layout);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    //METODO para darle funcionalidad a los botones de la orange_toolbar
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case android.R.id.home:
                setResult(RESULT_OK, new Intent());
                finish();
                break;
            default:
                super.onOptionsItemSelected(menuItem);
                break;
        }
        return true;
    }
}
