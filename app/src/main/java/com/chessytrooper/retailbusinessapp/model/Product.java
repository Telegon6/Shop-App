package com.chessytrooper.retailbusinessapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {
    private String name;
    private String description;
    @SerializedName("available_quantity")
    private int stock;
    private List<Photo> photos;
    @SerializedName("current_price")
    private List<Price> currentPrice;

    // Getters

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
