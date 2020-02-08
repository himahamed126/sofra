package com.example.sofra.data.local.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class OrderItem {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private int item_id;
    private int restaurant_id;
    private double price;
    private Integer quantity;
    private String client_name;
    private String item_name;
    private String note;
    private String photo;

    public OrderItem(int item_id, int restaurant_id, double price, Integer quantity,
                     String client_name, String note, String photo, String item_name) {
        this.item_id = item_id;
        this.restaurant_id = restaurant_id;
        this.price = price;
        this.quantity = quantity;
        this.client_name = client_name;
        this.note = note;
        this.photo = photo;
        this.item_name = item_name;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}