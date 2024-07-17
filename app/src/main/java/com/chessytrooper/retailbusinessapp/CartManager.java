package com.chessytrooper.retailbusinessapp;

import com.chessytrooper.retailbusinessapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<Product> cartItems = new ArrayList<>();

    public static void addToCart(Product product) {
        if (!isProductInCart(product)) {
            cartItems.add(product);
        }
    }

    public static void removeFromCart(Product product) {
        cartItems.removeIf(p -> p.getName().equals(product.getName()));
    }

    public static boolean isProductInCart(Product product) {
        return cartItems.stream().anyMatch(p -> p.getName().equals(product.getName()));
    }

    public static List<Product> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public static void clearCart() {
        cartItems.clear();
    }
}
