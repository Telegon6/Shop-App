package com.chessytrooper.retailbusinessapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private String name;
    private String description;
    @SerializedName("available_quantity")
    private int stock;
    private List<Photo> photos;
    @SerializedName("current_price")
    private List<Price> currentPrice;
    private int quantity;

    // Constructor
    public Product(Product other) {
        this.name = other.name;
        this.description = other.description;
        this.stock = other.stock;
        this.photos = other.photos;
        this.currentPrice = other.currentPrice;
        this.quantity = 0; // Initialize quantity to 0 for new cart items
    }

    // Getters
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getStock() {
        return stock;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

        public String getNgnPrice() {
        if (currentPrice != null && !currentPrice.isEmpty()) {
            Double price = currentPrice.get(0).getFirstNgnPrice();
            if (price != null) {
                return String.valueOf(price);
            }
        }
        return "N/A";
    }
}
