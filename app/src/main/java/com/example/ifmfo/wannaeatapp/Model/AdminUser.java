package com.example.ifmfo.wannaeatapp.Model;

public class AdminUser extends User {

    private Boolean hasRestaurant;

    public AdminUser(String email, Boolean hasRestaurant) {
        super(email);
        this.hasRestaurant = hasRestaurant;
    }

    public Boolean getHasRestaurant() {
        return hasRestaurant;
    }

    public void setHasRestaurant(Boolean hasRestaurant) {
        this.hasRestaurant = hasRestaurant;
    }
}
