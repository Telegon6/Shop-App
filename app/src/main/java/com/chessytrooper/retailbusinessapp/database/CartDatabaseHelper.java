package com.chessytrooper.retailbusinessapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.chessytrooper.retailbusinessapp.model.Order;
import com.chessytrooper.retailbusinessapp.model.Photo;
import com.chessytrooper.retailbusinessapp.model.Price;
import com.chessytrooper.retailbusinessapp.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "CartDatabaseHelper";
    private static final String DATABASE_NAME = "cart.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_CART = "cart";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_ORDER_ITEMS = "order_items";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_IMAGE_URL = "image_url";

    // ORDERS table columns
    private static final String COLUMN_ORDER_DATE = "order_date";
    private static final String COLUMN_ORDER_TOTAL = "order_total";

    // ORDER_ITEMS table columns
    private static final String COLUMN_ORDER_ID = "order_id";

    public CartDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_QUANTITY + " INTEGER,"
                + COLUMN_IMAGE_URL + " TEXT"
                + ")";
        db.execSQL(CREATE_CART_TABLE);

        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_ORDER_DATE + " TEXT,"
                + COLUMN_ORDER_TOTAL + " REAL" + ")";
        db.execSQL(CREATE_ORDERS_TABLE);

        String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE " + TABLE_ORDER_ITEMS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_ORDER_ID + " INTEGER,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_QUANTITY + " INTEGER" + ")";
        db.execSQL(CREATE_ORDER_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        onCreate(db);
    }

    public void addToCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, product.getName());
        values.put(COLUMN_DESCRIPTION, product.getDescription());
        // Handle potential parsing errors
        Double price = product.getFirstNgnPrice();
        if (price != null) {
            values.put(COLUMN_PRICE, price);
        } else {
            values.put(COLUMN_PRICE, 0.0); // Default to 0.0 if no price is available
        }

        values.put(COLUMN_QUANTITY, product.getQuantity());
        if (product.getPhotos() != null && !product.getPhotos().isEmpty()) {
            values.put(COLUMN_IMAGE_URL, product.getPhotos().get(0).getUrl());
        } else {
            values.put(COLUMN_IMAGE_URL, ""); // Set a default empty string if no image URL
        }

        Log.d(TAG, "Adding to cart: " + values.toString());
        db.insert(TABLE_CART, null, values);
        db.close();
    }

    public void removeFromCart(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_NAME + " = ?", new String[]{product.getName()});
        db.close();
    }

    public List<Product> getAllCartItems() {
        List<Product> cartList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CART;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int descIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
                int quantityIndex = cursor.getColumnIndex(COLUMN_QUANTITY);
                int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);
                int imageUrlIndex = cursor.getColumnIndex(COLUMN_IMAGE_URL);

                if (nameIndex != -1) {
                    product.setName(cursor.getString(nameIndex));
                }
                if (descIndex != -1) {
                    product.setDescription(cursor.getString(descIndex));
                }
                if (quantityIndex != -1) {
                    product.setQuantity(cursor.getInt(quantityIndex));
                }
                if (priceIndex != -1) {
                    double price = cursor.getDouble(priceIndex);
                    Price priceObj = new Price();
                    priceObj.setFirstNgnPrice(price);
                    product.setCurrentPrice(Collections.singletonList(priceObj));
                }
                if (imageUrlIndex != -1) {
                    String imageUrl = cursor.getString(imageUrlIndex);
                    List<Photo> photoList = new ArrayList<>();
                    Photo photo = new Photo();
                    photo.setUrl(imageUrl);
                    photoList.add(photo);
                    product.setPhotos(photoList);
                }
                Log.d(TAG, "Retrieved from cart: " + product.toString());

                cartList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cartList;
    }

    public void updateProductQuantity(String productName, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, newQuantity);
        db.update(TABLE_CART, values, COLUMN_NAME + " = ?", new String[]{productName});
        db.close();
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
        db.close();
    }

    // Order operations
    public long addOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_DATE, order.getDate());
        values.put(COLUMN_ORDER_TOTAL, order.getTotal());
        Log.d(TAG, "Adding ORDERS: " + values.toString());

        long orderId = db.insert(TABLE_ORDERS, null, values);

        for (Product item : order.getItems()) {
            ContentValues itemValues = new ContentValues();
            itemValues.put(COLUMN_ORDER_ID, orderId);
            itemValues.put(COLUMN_NAME, item.getName());
            itemValues.put(COLUMN_QUANTITY, item.getQuantity());
            itemValues.put(COLUMN_PRICE, item.getFirstNgnPrice());
            db.insert(TABLE_ORDER_ITEMS, null, itemValues);
            Log.d(TAG, "Adding to order: " + itemValues.toString());
        }

        db.close();
        return orderId;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                order.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DATE)));
                order.setTotal(cursor.getDouble(cursor.getColumnIndex(COLUMN_ORDER_TOTAL)));
                order.setItems(getOrderItems(order.getId()));
                orders.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orders;
    }

    private List<Product> getOrderItems(long orderId) {
        List<Product> orderItems = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDER_ITEMS + " WHERE " + COLUMN_ORDER_ID + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                product.setQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY)));
                // Set other product properties as needed
                int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);
                if (priceIndex != -1) {
                    double price = cursor.getDouble(priceIndex);
                    Price priceObj = new Price();
                    priceObj.setFirstNgnPrice(price);
                    product.setCurrentPrice(Collections.singletonList(priceObj));
                }
                orderItems.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return orderItems;
    }
}