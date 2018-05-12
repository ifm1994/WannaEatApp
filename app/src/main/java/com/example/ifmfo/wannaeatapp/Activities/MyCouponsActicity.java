package com.example.ifmfo.wannaeatapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.CouponCardAdapter;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.R;
import java.util.Objects;

public class MyCouponsActicity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    private Toolbar toolbar;
    RecyclerView couponCardsContainer;
    RelativeLayout couponsEmptyContainer;
    RelativeLayout zeroCouponsContainer;
    CardView goLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupons_acticity);

        initUI();
        setupToolbar();
        bindEvents();

        if(globalResources.getUser_isLogged() && globalResources.getUserLogged().getUserCoupons().size() != 0){
            rellenarContainerDeCuponer();
        }
    }

    private void initUI() {
        toolbar = findViewById(R.id.transparent_toolbar);
        couponsEmptyContainer = findViewById(R.id.empty_coupons_container);
        couponCardsContainer = findViewById(R.id.coupon_cards_container);
        goLoginButton = findViewById(R.id.goLoginActivityButton);
        zeroCouponsContainer = findViewById(R.id.zero_coupons_container);


        if(globalResources.getUser_isLogged()){
            if( globalResources.getUserLogged().getUserCoupons().size() != 0){
                couponCardsContainer.setVisibility(View.VISIBLE);
            }else{
                zeroCouponsContainer.setVisibility(View.VISIBLE);
            }
        }else{
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

    private void rellenarContainerDeCuponer() {
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        couponCardsContainer.setHasFixedSize(true);
        couponCardsContainer.setAdapter(new CouponCardAdapter(globalResources.getUserLogged().getUserCoupons(), MyCouponsActicity.this));
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
