package com.chessytrooper.retailbusinessapp.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private long id;
    private String date;
    private double total;
    private List<Product> items;

    // Getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public List<Product> getItems() { return items; }
    public void setItems(List<Product> items) { this.items = items; }
}