package com.example.ifmfo.wannaeatapp.Fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Activities.RestaurantActivity;
import com.example.ifmfo.wannaeatapp.ProductCategoryAdapter;
import com.example.ifmfo.wannaeatapp.Model.Product;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.R;
import com.example.ifmfo.wannaeatapp.RestaurantTabAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class RestaurantMenuTab extends Fragment {

    Restaurant thisRestaurant;
    public static List<Product> allProducts = new ArrayList<>();
    LinearLayout categoriesContainer;
    View fragmentView;
    RecyclerView categoryListContainer;
    NestedScrollView mainLayout;

    @SuppressLint("ValidFragment")
    public RestaurantMenuTab(Restaurant restaurant){
        this.thisRestaurant = restaurant;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
        fragmentView = inflater.inflate(R.layout.fragment_restaurant_menu_tab,container, false);

        mainLayout = fragmentView.findViewById(R.id.main_layout);
        categoriesContainer = fragmentView.findViewById(R.id.categoriesContainer);
        obtenerProductosDelRestaurante();

        return fragmentView;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void obtenerProductosDelRestaurante() {
        allProducts.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/products/restaurant/" + thisRestaurant.getId();
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlPeticion,
                null,
                response -> {
                    try{
                        // Loop through the array elements
                        for(int i=0;i<response.length();i++){
                            // Get current json object
                            JSONObject productObject = response.getJSONObject(i);
                            // Get products of current restaurant (json object) data
                            int id = productObject.getInt("id");
                            String name = productObject.getString("name");
                            int id_restaurante = productObject.getInt("id_restaurant");
                            double price = productObject.getDouble("price");
                            String description = productObject.getString("description");
                            int amount = productObject.getInt("amount");
                            String category = productObject.getString("category");
                            Product product = new Product(id,name,id_restaurante,price,description,amount,category);
                            allProducts.add(product);
                        }
                        dibujarListaDeCategorias(obtenerCategoriasExistentesDelRestaurante());

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Do something when error occurred
                    RestaurantActivity.showMessage("Error en la petición de productos del restaurante");
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint({"LongLogTag", "ResourceAsColor"})
    public void dibujarListaDeCategorias(List <String> categorias){
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        categoryListContainer = fragmentView.findViewById(R.id.category_item_container);
        categoryListContainer.setHasFixedSize(true);
        categoryListContainer.setAdapter(new ProductCategoryAdapter(thisRestaurant, categorias, getContext()));
        categoryListContainer.setLayoutManager(layout);
    }

    public List <String> obtenerCategoriasExistentesDelRestaurante(){
        List <String> categorias = new ArrayList<>();
        for (int i = 0;i < allProducts.size(); i++){
            if(!yaExiste(allProducts.get(i).getCategory(), categorias)){
                categorias.add(allProducts.get(i).getCategory());
            }
        }
        return categorias;
    }

    public boolean yaExiste(String categoria, List <String> listaCategorias){
        return listaCategorias.contains(categoria);
    }

    public static List <Product> getProducts(){
        return allProducts;
    }

}
