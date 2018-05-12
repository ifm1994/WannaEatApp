package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.R;
import com.example.ifmfo.wannaeatapp.RestaurantTabAdapter;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class RestaurantActivity extends AppCompatActivity {

    static Restaurant thisRestaurant;
    TextView restaurantName;
    ImageView restaurantImage;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    RestaurantTabAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    static TextView basketPrice, basketAmount;
    private static Menu menu;
    static final GlobalResources globalResources = GlobalResources.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        restaurantName = findViewById(R.id.single_restaurant_name);
        restaurantImage = findViewById(R.id.single_restaurant_image);
        toolbar = findViewById(R.id.transparent_toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.restaurantsTabContainer);

        initializeActivity();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_action_orange_back);

    }

    public void initializeActivity(){
        if(getIntent().getExtras() != null){
            thisRestaurant = (Restaurant) getIntent().getSerializableExtra("restaurantObject");
            adapter = new RestaurantTabAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), thisRestaurant);
            viewPager.setAdapter(adapter);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }
                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            viewPager.addOnPageChangeListener (new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            //Fill restaurant data
            restaurantName.setText(thisRestaurant.getName());
            Picasso.get()
                    .load(thisRestaurant.getImage_path())
                    .into(restaurantImage);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && data.getExtras() != null){
            initializeActivity();
            updateBasketIndicator();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), RestaurantsActivity.class);
        intent.putExtra("direccion", globalResources.getSession_addressEntered());
        startActivityForResult(intent, 1);
    }

    //METODO para darle funcionalidad a los botones de la orange_toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), RestaurantsActivity.class);
                intent.putExtra("direccion", globalResources.getSession_addressEntered());
                startActivityForResult(intent, 1);
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
        getMenuInflater().inflate(R.menu.shopping_basket_menu, menu);
        RestaurantActivity.menu = menu;
        LinearLayout basketItem = (LinearLayout) menu.findItem(R.id.shopping_basket_item).getActionView();
        basketPrice = basketItem.findViewById(R.id.basket_price);
        basketAmount= basketItem.findViewById(R.id.basket_amount);
        updateBasketIndicator();

        MenuItem basketIcon = menu.findItem(R.id.shopping_basket_item);
        if(basketIcon != null){
            basketIcon.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(globalResources.shopping_basket_getNumberOfProductsAdded() > 0){
                        Intent goFullBookingActivity = new Intent(RestaurantActivity.this, FullShoppingBasketActivity.class);
                        goFullBookingActivity.putExtra("restaurant", thisRestaurant);
                        startActivityForResult(goFullBookingActivity,1);
                    }else{
                        Intent goEmptyBookingActivity = new Intent(RestaurantActivity.this, EmptyShoppingBasketActivity.class);
                        goEmptyBookingActivity.putExtra("restaurant", thisRestaurant);
                        startActivityForResult(goEmptyBookingActivity,1);
                    }
                }
            });
        }

        return true;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public static void updateBasketIndicator(){
        getBasketAmount().setText(Integer.toString(globalResources.shopping_basket_getNumberOfProductsAdded()));
        getBasketPrice().setText(String.format("%.2f", globalResources.shopping_basket_getTotalPrice()) + "€");
    }

    public static TextView getBasketPrice() {
        return basketPrice;
    }

    public static TextView getBasketAmount() {
        return basketAmount;
    }

    public static Restaurant getCurrentRestaurant() {
        return thisRestaurant;
    }
}
