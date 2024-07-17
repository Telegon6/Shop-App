package com.chessytrooper.retailbusinessapp;

import android.content.Context;
import android.content.Intent;
import com.chessytrooper.retailbusinessapp.model.Product;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static List<Product> cartItems = new ArrayList<>();

    public static void addToCart(Context context, Product product) {
        if (!isProductInCart(product)) {
            product.setQuantity(1);  // Set initial quantity to 1
            cartItems.add(product);
            notifyCartUpdated(context);
        }
    }

    public static void removeFromCart(Context context, Product product) {
        cartItems.removeIf(p -> p.getName().equals(product.getName()));
        notifyCartUpdated(context);
    }

    public static boolean isProductInCart(Product product) {
        return cartItems.stream().anyMatch(p -> p.getName().equals(product.getName()));
    }

    public static List<Product> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public static void clearCart(Context context) {
        cartItems.clear();
        notifyCartUpdated(context);
    }

    private static void notifyCartUpdated(Context context) {
        Intent intent = new Intent("com.chessytrooper.retailbusinessapp.CART_UPDATED");
        context.sendBroadcast(intent);
    }
}