package com.example.ifmfo.wannaeatapp.Model;

public class Opinion {
    private int id;
    private int idRestaurant;
    private int idUser;
    private String writerName;
    private double rating;
    private String description;
    private String date;


    public Opinion(int id, int idRestaurant, int idUser, String writerName, double rating, String description, String date) {
        this.id = id;
        this.idRestaurant = idRestaurant;
        this.idUser = idUser;
        this.writerName = writerName;
        this.rating = rating;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getIdRestaurant() {
        return idRestaurant;
    }

    public int getIdUser() {
        return idUser;
    }

    public String getWriterName() {
        return writerName;
    }

    public double getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }


}
