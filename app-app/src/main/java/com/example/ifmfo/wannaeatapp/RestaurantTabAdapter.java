package com.example.ifmfo.wannaeatapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ifmfo.wannaeatapp.Fragments.RestaurantInformationTab;
import com.example.ifmfo.wannaeatapp.Fragments.RestaurantMenuTab;
import com.example.ifmfo.wannaeatapp.Fragments.RestaurantOpinionsTab;
import com.example.ifmfo.wannaeatapp.Model.Restaurant;

public class RestaurantTabAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    private Restaurant thisRestaurant;

    public RestaurantTabAdapter(FragmentManager fm, int numberOfTabs, Restaurant restaurant) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        this.thisRestaurant = restaurant;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
            return new RestaurantMenuTab(thisRestaurant);
            case 1:
                return new RestaurantInformationTab(thisRestaurant);
            case 2:
                return new RestaurantOpinionsTab(thisRestaurant);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.numberOfTabs;
    }
}
