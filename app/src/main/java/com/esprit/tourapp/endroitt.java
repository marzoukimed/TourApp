package com.esprit.tourapp;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class endroitt {

    private int id;
    private String nom;
    private String location;
    private String photo;
    private String lat;
    private String lan;


    public endroitt(int id, String nom, String location, String photo ) {
        this.id = id;
        this.nom = nom;
        this.location = location;
        this.photo = photo;

    }

    public endroitt(String lat, String lan) {
        this.lat = lat;
        this.lan = lan;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "endroitt{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", location='" + location + '\'' +
                ", photo='" + photo + '\'' +
                ", lat='" + lat + '\'' +
                ", lan='" + lan + '\'' +
                '}';
    }
}
