package com.example.ifmfo.wannaeatapp.Model;

public class CommensalPerHour {

    private int id;
    private int id_restaurant;
    private String hour;
    private int commensal_capacity;

    public CommensalPerHour(int id, int id_restaurant, String hour, int commensal_capacity) {
        this.id = id;
        this.id_restaurant = id_restaurant;
        this.hour = hour;
        this.commensal_capacity = commensal_capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_restaurant() {
        return id_restaurant;
    }

    public void setId_restaurant(int id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getCommensal_capacity() {
        return commensal_capacity;
    }

    public void setCommensal_capacity(int commensal_capacity) {
        this.commensal_capacity = commensal_capacity;
    }
}
