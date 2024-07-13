package com.chessytrooper.retailbusinessapp.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Price implements Serializable {
    @SerializedName("NGN")
    private List<Object> ngn;

    public Double getFirstNgnPrice() {
        if (ngn != null && !ngn.isEmpty()) {
            for (Object obj : ngn) {
                if (obj instanceof Number) {
                    return ((Number) obj).doubleValue();
                }
            }
        }
        return null;
    }
}

