package com.example.ifmfo.wannaeatapp.Model;

import java.io.Serializable;

public class Product implements Serializable {

    private int id;
    private String name;
    private int id_restaurant;
    private double price;
    private String description;
    private int amount;
    private String category;

    public Product(int id, String name, int id_restaurant, double price, String description, int amount, String category) {
        this.id = id;
        this.name = name;
        this.id_restaurant = id_restaurant;
        this.price = price;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", id_restaurant=" + id_restaurant +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getId_restaurant() {
        return id_restaurant;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }
}
