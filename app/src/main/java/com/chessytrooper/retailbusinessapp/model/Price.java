package com.chessytrooper.retailbusinessapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Price implements Serializable {
    @SerializedName("NGN")
    private List<Object> ngn;

    public Double getFirstNgnPrice() {
        if (ngn != null && !ngn.isEmpty() && ngn.get(0) instanceof Number) {
            return ((Number) ngn.get(0)).doubleValue();
        }
        return null;
    }

    public void setFirstNgnPrice(Double price) {
        if (ngn == null) {
            ngn = new ArrayList<>();
        }
        if (ngn.isEmpty()) {
            ngn.add(price);
        } else {
            ngn.set(0, price);
        }
        // Ensure the list has at least 3 elements to match the API structure
        while (ngn.size() < 3) {
            ngn.add(null);
        }
    }
}

