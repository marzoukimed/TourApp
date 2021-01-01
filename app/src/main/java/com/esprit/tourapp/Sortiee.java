package com.esprit.tourapp;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
//import java.sql.Date;

public class Sortiee {


    private  int id;

    private  String  dt_debut;

    private String dt_fin;


    private String location;
    private String user_email;

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public int getId() {
        return id;
    }

    public String getDt_debut()
    {
        return dt_debut;
    }

    public String getDt_fin() {
        return dt_fin;
    }

    public String getLocation() {
        return location;
    }

    public Sortiee(int id, String dt_debut, String dt_fin, String location, String user_email) {
        this.id = id;
        this.dt_debut = dt_debut;
        this.dt_fin = dt_fin;
        this.location = location;
        this.user_email = user_email;
    }

    public Sortiee(int id, String dt_debut, String dt_fin, String location) {
        this.id = id;
        this.dt_debut = dt_debut;
        this.dt_fin = dt_fin;
        this.location = location;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDt_debut(String dt_debut) {
        this.dt_debut = dt_debut;
    }

    public void setDt_fin(String dt_fin) {
        this.dt_fin = dt_fin;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Sortiee{" +
                "id=" + id +
                ", dt_debut='" + dt_debut + '\'' +
                ", dt_fin='" + dt_fin + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
