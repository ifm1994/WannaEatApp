package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Model.Product;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.Model.GlobalResources;
import com.example.ifmfo.wannaeatapp.ProductCardAdapter;
import com.example.ifmfo.wannaeatapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductsPerCategoryActivity extends AppCompatActivity {

    Toolbar toolbar;
    Restaurant thisRestaurant;
    static List <Product> allProducts;
    static List <Product> allProductsOfThisCategory;
    TextView categoryName;
    static String thisCategory;
    static RecyclerView productListContainer;
    static final GlobalResources GLOBAL_RESOURCES = GlobalResources.getInstance();
    @SuppressLint("StaticFieldLeak")
    static TextView basketPrice, basketAmount;
    private static Menu menu;
    static Context context;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_per_category);

        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        categoryName = findViewById(R.id.category_name);
        productListContainer = findViewById(R.id.product_list_container);
        allProductsOfThisCategory = new ArrayList<>();
        context = getApplicationContext();


        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_action_orange_back);
        }

        Intent intent = getIntent();
        if(getIntent().getExtras() != null){
            thisRestaurant = (Restaurant) getIntent().getSerializableExtra("restaurant");
            allProducts = (List<Product>) getIntent().getSerializableExtra("products");
            thisCategory = getIntent().getStringExtra("categoryName");

            //Cambio el titulo a la orange_toolbar
            getSupportActionBar().setTitle(thisRestaurant.getName());
            categoryName.setText(thisCategory);
        }
        filtrarProductosPorCategoria(thisCategory);
    }

    public static void filtrarProductosPorCategoria(String category){
        allProductsOfThisCategory.clear();
        for (int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getCategory().equals(category)){
                allProductsOfThisCategory.add(allProducts.get(i));
            }
        }
        dibujarProductosDeCategoria(allProductsOfThisCategory);
    }

    public static void dibujarProductosDeCategoria(List<Product> products){
        LinearLayoutManager layout = new LinearLayoutManager(context);
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        productListContainer.setHasFixedSize(true);
        productListContainer.setAdapter(new ProductCardAdapter(context, products));
        productListContainer.setLayoutManager(layout);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data.getExtras() != null){
            updateBasketIndicator();
            dibujarProductosDeCategoria(allProductsOfThisCategory);
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("restaurantObject", thisRestaurant);
        updateBasketIndicator();
        setResult(RESULT_OK, intent);
        finish();

    }

    //METODO para darle funcionalidad a los botones de la orange_toolbar
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("restaurantObject", thisRestaurant);
                updateBasketIndicator();
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                super.onOptionsItemSelected(menuItem);
                break;
        }
        return true;
    }

    //METODO para crear un toolbar personalizado
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.shopping_basket_menu, menu);
        ProductsPerCategoryActivity.menu = menu;
        LinearLayout basketItem = (LinearLayout) menu.findItem(R.id.shopping_basket_item).getActionView();
        basketPrice = (TextView) basketItem.findViewById(R.id.basket_price);
        basketAmount= (TextView) basketItem.findViewById(R.id.basket_amount);
        updateBasketIndicator();

        MenuItem basketIcon = menu.findItem(R.id.shopping_basket_item);
        if(basketIcon != null){
            basketIcon.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(GLOBAL_RESOURCES.shopping_basket_getNumberOfProductsAdded() > 0){
                        Intent goFullBookingActivity = new Intent(ProductsPerCategoryActivity.this, FullShoppingBasketActivity.class);
                        goFullBookingActivity.putExtra("restaurant", thisRestaurant);
                        startActivityForResult(goFullBookingActivity,1);
                    }else{
                        Intent goEmptyBookingActivity = new Intent(ProductsPerCategoryActivity.this, EmptyShoppingBasketActivity.class);
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
        getBasketAmount().setText(Integer.toString(GLOBAL_RESOURCES.shopping_basket_getNumberOfProductsAdded()));
        getBasketPrice().setText(String.format("%.2f", Math.abs(GLOBAL_RESOURCES.shopping_basket_getTotalPrice())) + "€");
    }

    public static TextView getBasketPrice() {
        return basketPrice;
    }

    public static TextView getBasketAmount() {
        return basketAmount;
    }

    public static String getThisCategory() {
        return thisCategory;
    }
}
