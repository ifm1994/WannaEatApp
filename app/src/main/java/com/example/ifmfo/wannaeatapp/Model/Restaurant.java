package com.example.ifmfo.wannaeatapp.Model;

import java.io.Serializable;

public class Restaurant implements Serializable{

    private int id;
    private String name;
    private String address;
    private String kind_of_food;
    private String rating;
    private String image_path;
    private String opening_hours;
    private String description;
    private String phone;
    private double latitude;
    private double longitude;
    private int idAdmin;

    public Restaurant(int id,String name, String address, String kind_of_food, String rating, String image_path, String opening_hours, String description, String phone, double latitude, double longitude, int idAdmin) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.kind_of_food = kind_of_food;
        this.rating = rating;
        this.image_path = image_path;
        this.opening_hours = opening_hours;
        this.description = description;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.idAdmin = idAdmin;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public String getAddress() {
        return address;
    }

    public String getKind_of_food() {
        return kind_of_food;
    }

    public String getRating() {
        return rating;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getOpening_hours() {
        return opening_hours;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setKind_of_food(String kind_of_food) {
        this.kind_of_food = kind_of_food;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public void setOpening_hours(String opening_hours) {
        this.opening_hours = opening_hours;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
