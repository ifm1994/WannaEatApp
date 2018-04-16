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

    public Restaurant(String name, String address, String kind_of_food, String rating, String image_path, String opening_hours, String description, String phone, double latitude, double longitude) {
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
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", kind_of_food='" + kind_of_food + '\'' +
                ", rating='" + rating + '\'' +
                ", image_path='" + image_path + '\'' +
                ", opening_hours='" + opening_hours + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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


}
