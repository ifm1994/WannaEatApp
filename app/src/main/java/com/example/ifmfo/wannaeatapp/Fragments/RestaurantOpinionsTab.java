package com.example.ifmfo.wannaeatapp.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ifmfo.wannaeatapp.Activities.RestaurantActivity;
import com.example.ifmfo.wannaeatapp.Model.Opinion;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.OpinionCardAdapter;
import com.example.ifmfo.wannaeatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class RestaurantOpinionsTab extends Fragment {

    Restaurant thisRestaurant;
    View fragmentView;
    TextView restaurantRatingView;
    TextView amountOfOpinionsView;
    RecyclerView opinionsContainer;
    public static List<Opinion> allOpinions = new ArrayList<>();
    NestedScrollView mainLayout;

    @SuppressLint("ValidFragment")
    public RestaurantOpinionsTab(Restaurant restaurant){
        thisRestaurant = restaurant;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_restaurant_opinions_tab,container, false);

        initUI();
        obtenerOpinionesDelRestaurante();

        return fragmentView;
    }

    private void initUI() {
        restaurantRatingView = fragmentView.findViewById(R.id.single_restaurant_rating);
        amountOfOpinionsView = fragmentView.findViewById(R.id.single_restaurant_number_of_opinions);
        mainLayout = fragmentView.findViewById(R.id.main_layout);
        restaurantRatingView.setText("0");
    }

    private void obtenerOpinionesDelRestaurante() {
        allOpinions.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String urlPeticion = "https://wannaeatservice.herokuapp.com/api/opinions/restaurant/" + thisRestaurant.getId();
        @SuppressLint({"SetTextI18n", "DefaultLocale"}) JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
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
                            String writerName = productObject.getString("name");
                            Double rating = productObject.getDouble("rating");
                            String description = productObject.getString("description");
                            int idUser = productObject.getInt("id_user");
                            int idRestaurant = productObject.getInt("id_restaurant");
                            String date = productObject.getString("updated_at");
                            String finalDate = date.substring(0, date.indexOf(" "));

                            Opinion opinion = new Opinion(id, idRestaurant, idUser, writerName, rating, description, finalDate);
                            allOpinions.add(opinion);
                        }
                        dibujarListaDeOpiniones();
                        restaurantRatingView.setText(thisRestaurant.getRating());
                        amountOfOpinionsView.setText(Integer.toString(allOpinions.size()));

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Do something when error occurred
                    RestaurantActivity.showMessage("Error en la petición de productos del restaurante, vuelva a intentarlo. Disculpe las molestias");
                }
        );
        requestQueue.add(jsonObjectRequest);


    }

    private void dibujarListaDeOpiniones() {
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        opinionsContainer = fragmentView.findViewById(R.id.single_restaurant_opinion_cards_container);
        opinionsContainer.setHasFixedSize(true);
        opinionsContainer.setAdapter(new OpinionCardAdapter(getContext(), allOpinions));
        opinionsContainer.setLayoutManager(layout);
    }
}
