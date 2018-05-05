package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.BookingProductAdapter;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.Model.User;
import com.example.ifmfo.wannaeatapp.R;

import org.w3c.dom.Text;

import java.util.Objects;

import static com.example.ifmfo.wannaeatapp.R.drawable.*;

public class FullShoppingBasketActivity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    Toolbar toolbar;
    Restaurant lastRestaurantVisited;
    @SuppressLint("StaticFieldLeak")
    static RecyclerView bookingProductsContainer;
    Spinner couponDesplegable;
    String [] couponTitles = {"Elegir tu cupón"};
    @SuppressLint("StaticFieldLeak")
    static TextView totalPrice;
    NestedScrollView scrollView;
    @SuppressLint("StaticFieldLeak")
    static Context context;
    Button applyCouponButton;
    RelativeLayout blackMask;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_shopping_basket);

        lastRestaurantVisited = (Restaurant) getIntent().getSerializableExtra("restaurant");
        couponDesplegable = findViewById(R.id.coupon_spinner);
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        totalPrice = findViewById(R.id.booking_total_price);
        scrollView = findViewById(R.id.scroll_content);
        bookingProductsContainer = findViewById(R.id.booking_products_container);
        context = getApplicationContext();
        CardView continueBooking = findViewById(R.id.continueBookingButon);
        applyCouponButton = findViewById(R.id.apply_coupon_button);
        blackMask =  findViewById(R.id.black_mask);

        bookingProductsContainer.setNestedScrollingEnabled(false);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationIcon(ic_action_orange_back);
            getSupportActionBar().setTitle("Pedido");
        }
        dibujarProductosDeLaReserva();
        actualizarPrecioTotal();
        rellenarDesplegableCupon();


        globalResources.user_logIn(new User(1,"Isaac", "",""));
        if(globalResources.getUser_isLogged()){
            blackMask.setVisibility(View.GONE);
            rellenarDesplegableCupon();
        }else{
            blackMask.setOnTouchListener((v, event) -> true);
        }

        continueBooking.setOnClickListener(arg0 -> {
            startActivityForResult(new Intent(getApplicationContext(), BookingDetailsActivity.class),1);
        });
    }


    public boolean onTouchEvent(MotionEvent me) {
        return true;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public static void actualizarPrecioTotal() {
        totalPrice.setText( String.format("%.2f", globalResources.shopping_basket_getTotalPrice())+"€");
    }


    private void rellenarDesplegableCupon() {
        couponTitles = new String[]{"Elegir tu cupón","Cupon 1","Cupon 2"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, couponTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        couponDesplegable.setAdapter(adapter);
    }

    public static void dibujarProductosDeLaReserva() {
        LinearLayoutManager layout = new LinearLayoutManager(context);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        bookingProductsContainer.setHasFixedSize(true);
        bookingProductsContainer.setAdapter(new BookingProductAdapter(context));
        bookingProductsContainer.setLayoutManager(layout);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("restaurantObject", lastRestaurantVisited);
        RestaurantActivity.updateBasketIndicator();
        setResult(RESULT_OK, intent);
        finish();

    }

    //METODO para darle funcionalidad a los botones de la orange_toolbar
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("restaurantObject", lastRestaurantVisited);
                RestaurantActivity.updateBasketIndicator();
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                super.onOptionsItemSelected(menuItem);
                break;
        }
        return true;
    }
}
