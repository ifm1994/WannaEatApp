package com.example.ifmfo.wannaeatapp.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable{

    private int id;
    private String name;
    private String email;
    private String phone;
    private List<Coupon> userCoupons;
    private List<Booking> userBookings;
    private String firebaseToken;

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        userCoupons = new ArrayList<>();
        userBookings = new ArrayList<>();
    }

    public User(String email){
        this.email = email;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Coupon> getUserCoupons() {
        return userCoupons;
    }

    public void setUserCoupons(List<Coupon> userCoupons) {
        this.userCoupons = userCoupons;
    }

    public void setUserBookings(List<Booking> userBookings){
        this.userBookings = userBookings;
    }

    public List<Booking> getUserBookings() {
        return userBookings;
    }

    public void addNewCoupon(Coupon coupon){
        getUserCoupons().add(coupon);
    }

    public void removeCoupon(Coupon couponToRemove){
        if(getUserCoupons().contains(couponToRemove)){
            getUserCoupons().remove(couponToRemove);
        }
    }

    public void addNewBooking(Booking booking){
        getUserBookings().add(booking);
    }
}
