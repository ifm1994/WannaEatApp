package com.example.ifmfo.wannaeatapp.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Model.Product;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.Model.ShoppingCart;
import com.example.ifmfo.wannaeatapp.ProductCardAdapter;
import com.example.ifmfo.wannaeatapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductsPerCategoryActivity extends AppCompatActivity {

    Toolbar toolbar;
    Restaurant thisRestaurant;
    List <Product> allProducts;
    List <Product> allProductsOfThisCategory;
    TextView categoryName;
    String thisCategory;
    RecyclerView productListContainer;
    static final ShoppingCart shoppingCart = ShoppingCart.getInstance();
    @SuppressLint("StaticFieldLeak")
    static TextView basketPrice, basketAmount;
    private static Menu menu;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_per_category);

        toolbar = findViewById(R.id.white_toolbar_with_orange_border);
        categoryName = findViewById(R.id.category_name);
        allProductsOfThisCategory = new ArrayList<>();


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

    private void filtrarProductosPorCategoria(String category){
        allProductsOfThisCategory.clear();
        for (int i = 0; i < allProducts.size(); i++){
            if(allProducts.get(i).getCategory().equals(category)){
                allProductsOfThisCategory.add(allProducts.get(i));
            }
        }
        dibujarProductosDeCategoria(allProductsOfThisCategory);
    }

    public void dibujarProductosDeCategoria(List <Product> products){
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        productListContainer = findViewById(R.id.product_list_container);
        productListContainer.setHasFixedSize(true);
        productListContainer.setAdapter(new ProductCardAdapter(getApplicationContext(), products));
        productListContainer.setLayoutManager(layout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data.getExtras() != null){

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("restaurantObject", thisRestaurant);
        setResult(RESULT_OK, intent);
        finish();

    }

    //METODO para darle funcionalidad a los botones de la orange_toolbar
    @SuppressLint("ShowToast")
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("restaurantObject", thisRestaurant);
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
        ProductsPerCategoryActivity.menu = menu;
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
