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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.Model.ShoppingCart;
import com.example.ifmfo.wannaeatapp.R;
import com.example.ifmfo.wannaeatapp.RestaurantTabAdapter;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Objects;

public class RestaurantActivity extends AppCompatActivity {

    Restaurant thisRestaurant;
    TextView restaurantName;
    ImageView restaurantImage;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    RestaurantTabAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    static TextView basketPrice, basketAmount;
    private static Menu menu;
    static final ShoppingCart shoppingCart = ShoppingCart.getInstance();

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
        if(getSupportActionBar() != null){
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setNavigationIcon(R.drawable.ic_action_orange_back);
        }
    }

    public static Menu getMenu() {
        return menu;
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

        if(requestCode == 1 && resultCode == RESULT_OK && data.getExtras() != null){
            initializeActivity();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    //METODO para darle funcionalidad a los botones de la orange_toolbar
    @SuppressLint("ShowToast")
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                Log.i("info.","default");
                break;
        }
        return true;
    }


    //METODO para crear un toolbar personalizado
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.shopping_basket_menu, menu);
        RestaurantActivity.menu = menu;
        LinearLayout basketItem = (LinearLayout) menu.findItem(R.id.shopping_basket_item).getActionView();
        basketPrice = (TextView) basketItem.findViewById(R.id.basket_price);
        basketAmount= (TextView) basketItem.findViewById(R.id.basket_amount);
        updateBasketIndicator();
        return true;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public static void updateBasketIndicator(){
        getBasketAmount().setText(Integer.toString(shoppingCart.getNumberOfProductsAdded()));
        getBasketPrice().setText(String.format("%.2f", shoppingCart.getTotalPrice()) + "€");
    }

    public static TextView getBasketPrice() {
        return basketPrice;
    }

    public static TextView getBasketAmount() {
        return basketAmount;
    }
}
