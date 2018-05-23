package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.BookingProductAdapter;
import com.example.ifmfo.wannaeatapp.Model.Coupon;
import com.example.ifmfo.wannaeatapp.Model.Product;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.ifmfo.wannaeatapp.R.drawable.*;

public class FullShoppingBasketActivity extends AppCompatActivity {

    static final GlobalResources globalResources = GlobalResources.getInstance();
    Toolbar toolbar;
    Restaurant lastRestaurantVisited;
    @SuppressLint("StaticFieldLeak")
    static RecyclerView bookingProductsContainer;
    static Spinner couponDesplegable;
    String [] couponTitles = {"Elige tu cupón"};
    @SuppressLint("StaticFieldLeak")
    static TextView totalPrice;
    NestedScrollView scrollView;
    @SuppressLint("StaticFieldLeak")
    static Context context;
    Button applyCouponButton;
    RelativeLayout blackMask;
    CardView continueBooking;
    static TextView discontedProductLabel;
    static TextView discontedPriceLabel;
    static ImageButton removeCouponButton;
    TextView restaurantName;
    static RelativeLayout mainLayout;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n", "ResourceAsColor", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_shopping_basket);

        initUI();
        setUpToolbar();
        dibujarProductosDeLaReserva();
        actualizarPrecioTotal();
        rellenarDesplegableCupon();

        if(globalResources.getUser_isLogged()){
            blackMask.setVisibility(View.GONE);
            obtenerCuponesDelUsuario();
        }
        if(globalResources.getCouponApplied() != null){
            int index = globalResources.getIndexOfCouponApplied(globalResources.getCouponApplied(), couponTitles);
            if(index != -1){
                couponDesplegable.setSelection(index);
                if(globalResources.getCouponApplied().getCategory().equals("")){
                    //El descuento ya aplicado es para toda la cuenta
                    discontedProductLabel.setText("Descuento del pedido");
                }else{
                    //El descuento ya aplicado es para un tipo de producto
                    discontedProductLabel.setText(globalResources.getProductCouponApplied().getName());
                }
                discontedPriceLabel.setText("- " + String.format("%.2f", globalResources.getDiscountApplied()) + "€");
            }
        }
        bindEvents();

    }

    private void initUI() {
        lastRestaurantVisited = (Restaurant) getIntent().getSerializableExtra("restaurant");
        couponDesplegable = findViewById(R.id.coupon_spinner);
        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        totalPrice = findViewById(R.id.booking_total_price);
        scrollView = findViewById(R.id.scroll_content);
        bookingProductsContainer = findViewById(R.id.booking_products_container);
        context = getApplicationContext();
        continueBooking = findViewById(R.id.continueBookingButon);
        applyCouponButton = findViewById(R.id.apply_coupon_button);
        blackMask =  findViewById(R.id.black_mask);
        discontedPriceLabel = findViewById(R.id.discounted_price);
        discontedProductLabel = findViewById(R.id.discounted_product);
        removeCouponButton = findViewById(R.id.removeAppliedCouponButton);
        restaurantName = findViewById(R.id.booking_restaurant_name);
        mainLayout = findViewById(R.id.main_layout);

        bookingProductsContainer.setNestedScrollingEnabled(false);
        restaurantName.setText(globalResources.getCurrentRestaurantNameOfBooking());
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationIcon(ic_action_orange_back);
            getSupportActionBar().setTitle("Pedido");
        }
    }

    private void bindEvents() {
        continueBooking.setOnClickListener(arg0 -> {
            if(globalResources.getUser_isLogged()){
                startActivityForResult(new Intent(getApplicationContext(), BookingDetailsActivity.class),1);
            }else{
                Intent goLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                goLoginActivity.putExtra("NavigationCode","sinceBasket");
                startActivityForResult(goLoginActivity,1);
            }
        });

        blackMask.setOnClickListener(arg0 -> {
            Intent goLoginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            goLoginActivity.putExtra("NavigationCode","sinceBasket");
            startActivityForResult(goLoginActivity,1);
        });

        applyCouponButton.setOnClickListener(arg0 -> {
            if(!couponDesplegable.getSelectedItem().toString().equals("Elige tu cupón")){
                applyCoupon(globalResources.getCouponWithThisDescription(couponDesplegable.getSelectedItem().toString()));
            }else{
                Snackbar.make(mainLayout ,"Debe seleccionar un cupón",Snackbar.LENGTH_SHORT).show();
            }
        });

        removeCouponButton.setOnClickListener(arg0 -> {
            if(globalResources.getCouponApplied() != null){
                removeCouponApplied();
            }
        });
    }

    public static Spinner getCouponDesplegable() {
        return couponDesplegable;
    }

    private void rellenarDesplegableCupon() {

        if(globalResources.getUser_isLogged()){
            List<String> couponsTitlesList = new ArrayList<>();
            List<Coupon> coupons =  globalResources.getUserLogged().getUserCoupons();

            couponsTitlesList.add("Elige tu cupón");
            int currentIdRestaurant = globalResources.getCurrentRestaurantIdOfBooking();
            for(Coupon coupon : coupons){
                if(coupon.getId_restaurant() == currentIdRestaurant)
                    couponsTitlesList.add(coupon.getDescription());
            }
            if(couponsTitlesList.size() == 1){
                couponsTitlesList.clear();
                couponsTitlesList.add("No tiene cupones para este restaurante");
                applyCouponButton.setVisibility(View.GONE);
            }else{
                applyCouponButton.setVisibility(View.VISIBLE);
            }
            couponTitles = new String[couponsTitlesList.size()];
            couponTitles = couponsTitlesList.toArray(couponTitles);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, couponTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        couponDesplegable.setAdapter(adapter);
    }

    private void obtenerCuponesDelUsuario() {
        List <Coupon> allCouponsOfUser = new ArrayList<>();
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
                        rellenarDesplegableCupon();

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

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void applyCoupon(Coupon coupon) {
        if(globalResources.getCouponApplied() == null){
            if(coupon.getCategory().equals("")){
                //Descuento al pedido
                Double discount = globalResources.shopping_basket_getTotalPrice() - (globalResources.shopping_basket_getTotalPrice() * (100 - coupon.getDiscount()) * 0.01);
                discontedProductLabel.setText("Descuento del pedido");
                discontedPriceLabel.setText("- " + String.format("%.2f", discount) + "€");
                removeCouponButton.setVisibility(View.VISIBLE);
                globalResources.setShopping_basket_totalPrice_with_discount(globalResources.shopping_basket_getTotalPrice() - discount);
                globalResources.setCouponApplied(coupon);
                globalResources.setDiscountApplied(discount);
                FullShoppingBasketActivity.actualizarPrecioTotal();
                Snackbar.make(mainLayout ,"Cupón aplicado",Snackbar.LENGTH_SHORT).show();
            }else{
                //Descuento a un artículo
                Product cheapestProduct = globalResources.CheapestProductOfCategory(coupon.getCategory());
                if(cheapestProduct == null){
                    Snackbar.make(mainLayout ,"Este cupon no se puede aplicar porque no tiene ningún producto de esa categoria",Snackbar.LENGTH_SHORT).show();
                }else{
                    Double discount = cheapestProduct.getPrice() - (cheapestProduct.getPrice() * (100 - coupon.getDiscount()) * 0.01);
                    discontedPriceLabel.setText("- " + String.format("%.2f", discount) + "€");
                    discontedProductLabel.setText(cheapestProduct.getName());
                    removeCouponButton.setVisibility(View.VISIBLE);
                    globalResources.setShopping_basket_totalPrice_with_discount(globalResources.shopping_basket_getTotalPrice() - discount);
                    globalResources.setCouponApplied(coupon);
                    globalResources.setProductCouponApplied(cheapestProduct);
                    globalResources.setDiscountApplied(discount);
                    FullShoppingBasketActivity.actualizarPrecioTotal();
                    Snackbar.make(mainLayout ,"Cupón aplicado",Snackbar.LENGTH_SHORT).show();
                }
            }
        }else{
            int index = globalResources.getIndexOfCouponApplied(globalResources.getCouponApplied(), couponTitles);
            if(index != -1){
                couponDesplegable.setSelection(index);
            }
            Snackbar.make(mainLayout ,"Solo es posible aplicar un cupon por reserva",Snackbar.LENGTH_SHORT).show();
        }

    }

    public static void removeCouponApplied(){
        globalResources.setShopping_basket_totalPrice_with_discount(0.0);
        discontedProductLabel.setText("");
        discontedPriceLabel.setText("");
        globalResources.setCouponApplied(null);
        globalResources.setProductCouponApplied(null);
        globalResources.setDiscountApplied(null);
        removeCouponButton.setVisibility(View.GONE);
        FullShoppingBasketActivity.actualizarPrecioTotal();
        Snackbar.make(mainLayout ,"El cupón ha dejado de estar aplicado",Snackbar.LENGTH_SHORT).show();

    }

    public boolean onTouchEvent(MotionEvent me) {
        return true;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public static void actualizarPrecioTotal() {
        if(globalResources.getCouponApplied() == null){
            totalPrice.setText( String.format("%.2f", globalResources.shopping_basket_getTotalPrice())+"€");
        }else{
            totalPrice.setText( String.format("%.2f", globalResources.getShopping_basket_totalPrice_with_discount())+"€");
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.booking_details_menu, menu);
        return true;
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
            case R.id.fill_basket_option:
                globalResources.shopping_basket_emptyShoppingCart();
                Intent goEmpty = new Intent( context, EmptyShoppingBasketActivity.class );
                goEmpty.putExtra("restaurant", RestaurantActivity.getCurrentRestaurant());
                startActivity(goEmpty);

                break;
            default:
                super.onOptionsItemSelected(menuItem);
                break;
        }
        return true;
    }
}
