package com.chessytrooper.retailbusinessapp;

import android.content.Context;
import android.content.Intent;
import com.chessytrooper.retailbusinessapp.model.Product;
import java.util.ArrayList;
import java.util.List;

public class WishlistManager {
    private static List<Product> wishlistItems = new ArrayList<>();

    public static void addToWishlist(Context context, Product product) {
        if (!isProductInWishlist(product)) {
            wishlistItems.add(product);
            notifyWishlistUpdated(context);
        }
    }

    public static void removeFromWishlist(Context context, Product product) {
        wishlistItems.removeIf(p -> p.getName().equals(product.getName()));
        notifyWishlistUpdated(context);
    }

    public static boolean isProductInWishlist(Product product) {
        return wishlistItems.stream().anyMatch(p -> p.getName().equals(product.getName()));
    }

    public static List<Product> getWishlistItems() {
        return new ArrayList<>(wishlistItems);
    }

    private static void notifyWishlistUpdated(Context context) {
        Intent intent = new Intent("com.chessytrooper.retailbusinessapp.WISHLIST_UPDATED");
        context.sendBroadcast(intent);
    }
}