package com.chessytrooper.retailbusinessapp;

import android.content.Context;
import android.content.Intent;
import com.chessytrooper.retailbusinessapp.database.CartDatabaseHelper;
import com.chessytrooper.retailbusinessapp.model.Product;
import java.util.List;

public class CartManager {
    private static CartDatabaseHelper dbHelper;

    public static void initializeDB(Context context) {
        if (dbHelper == null) {
            dbHelper = new CartDatabaseHelper(context.getApplicationContext());
        }
    }

    public static void addToCart(Context context, Product product) {
        initializeDB(context);
        if (!isProductInCart(context, product)) {
            product.setQuantity(1);  // Set initial quantity to 1
            dbHelper.addToCart(product);
            notifyCartUpdated(context);
        }
    }

    public static void removeFromCart(Context context, Product product) {
        initializeDB(context);
        dbHelper.removeFromCart(product);
        notifyCartUpdated(context);
    }

    public static List<Product> getCartItems(Context context) {
        initializeDB(context);
        return dbHelper.getAllCartItems();
    }

    public static void clearCart(Context context) {
        initializeDB(context);
        dbHelper.clearCart();
        notifyCartUpdated(context);
    }

    // Add this method
    public static boolean isProductInCart(Context context, Product product) {
        initializeDB(context);
        List<Product> cartItems = dbHelper.getAllCartItems();
        for (Product cartItem : cartItems) {
            if (cartItem.getName().equals(product.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void updateProductQuantity(Context context, Product product) {
        initializeDB(context);
        dbHelper.updateProductQuantity(product.getName(), product.getQuantity());
        notifyCartUpdated(context);
    }

    private static void notifyCartUpdated(Context context) {
        Intent intent = new Intent("com.chessytrooper.retailbusinessapp.CART_UPDATED");
        context.sendBroadcast(intent);
    }
}