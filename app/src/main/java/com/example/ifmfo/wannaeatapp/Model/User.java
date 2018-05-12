package com.example.ifmfo.wannaeatapp.Model;

import java.util.List;

public class User {

    private int id;
    private String name;
    private String email;
    private String phone;
    private List<Coupon> userCoupons;

    public User(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
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
}
