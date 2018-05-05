package com.example.ifmfo.wannaeatapp.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ifmfo.wannaeatapp.Model.Restaurant;
import com.example.ifmfo.wannaeatapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class RestaurantInformationTab extends Fragment implements OnMapReadyCallback{

    Restaurant thisRestaurant;
    GoogleMap mGoogleMap;
    MapView restaurantMap;
    View fragmentView;
    TextView restaurantOpenningHours;
    TextView restaurantFoodType;
    TextView restaurantDescription;
    TextView restaurantContact;
    TextView restaurantUbication;


    @SuppressLint("ValidFragment")
    public RestaurantInformationTab(Restaurant restaurant){
        this.thisRestaurant = restaurant;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
        fragmentView = inflater.inflate(R.layout.fragment_information_tab,container, false);

        initUI();

        restaurantFoodType.setText(thisRestaurant.getKind_of_food());
        restaurantOpenningHours.setText(thisRestaurant.getOpening_hours());
        restaurantDescription.setText(thisRestaurant.getDescription());
        restaurantContact.setText("Teléfono: " + thisRestaurant.getPhone());
        restaurantUbication.setText("Ubicación: " + thisRestaurant.getAddress());

        return fragmentView;
    }

    private void initUI() {
        restaurantOpenningHours = fragmentView.findViewById(R.id.single_restaurant_openning_hour_value);
        restaurantFoodType = fragmentView.findViewById(R.id.single_restaurant_food_type_value);
        restaurantDescription = fragmentView.findViewById(R.id.single_restaurant_description);
        restaurantContact = fragmentView.findViewById(R.id.single_restaurant_contact);
        restaurantUbication = fragmentView.findViewById(R.id.ubication_card_header);
    }

    @Override
    public void onViewCreated(@NonNull View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restaurantMap = fragmentView.findViewById(R.id.single_restaurant_map);
        if(restaurantMap != null){
            restaurantMap.onCreate(null);
            restaurantMap.onResume();
            restaurantMap.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;


        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(thisRestaurant.getLatitude(), thisRestaurant.getLongitude())).title(thisRestaurant.getName()));

        CameraPosition restaurant = CameraPosition.builder().target(new LatLng(thisRestaurant.getLatitude(), thisRestaurant.getLongitude())).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(restaurant));
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
    }
}
