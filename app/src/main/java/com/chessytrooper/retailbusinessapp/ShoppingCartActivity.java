package com.chessytrooper.retailbusinessapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chessytrooper.retailbusinessapp.adapter.ShoppingCartAdapter;
import com.chessytrooper.retailbusinessapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends AppCompatActivity
        implements ShoppingCartAdapter.OnItemRemovedListener,
        ShoppingCartAdapter.OnQuantityChangedListener {
    private List<Product> shoppingCart;
    private RecyclerView recyclerView;
    private ShoppingCartAdapter adapter;
    private TextView totalPriceTextView;
    private static final int CHECKOUT_REQUEST = 2;
    private Button checkoutButton;
    private double totalPrice;
    private static final String TAG = "ShoppingCartActivity";

    private BroadcastReceiver cartUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateCartItems();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> proceedToCheckout());

        shoppingCart = new ArrayList<>();
        adapter = new ShoppingCartAdapter(shoppingCart, this, this);
        recyclerView.setAdapter(adapter);

        updateCartItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("com.chessytrooper.retailbusinessapp.CART_UPDATED");
        registerReceiver(cartUpdateReceiver, filter);
        updateCartItems();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(cartUpdateReceiver);
    }

    private void updateCartItems() {
        shoppingCart.clear();
        shoppingCart.addAll(CartManager.getCartItems(this));
        Log.d(TAG, "Cart items: " + shoppingCart.toString());
        adapter.notifyDataSetChanged();
        updateTotalPrice();
        updateEmptyCartView();
    }

    private void updateTotalPrice() {
        totalPrice = 0;
        for (Product product : shoppingCart) {
            try {
                double price = Double.parseDouble(product.getNgnPrice());
                totalPrice += price * product.getQuantity();
                Log.d(TAG, "Product: " + product.getName() + ", Price: " + product.getFirstNgnPrice() + ", Quantity: " + product.getQuantity());
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing price for product: " + product.getName() + ", Price string: " + product.getNgnPrice());
            }
        }
        totalPriceTextView.setText(String.format("Total: $%.2f", totalPrice));
    }

    private void updateEmptyCartView() {
        if (shoppingCart.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            // Assuming you have a TextView for empty cart message
            findViewById(R.id.emptyCartMessage).setVisibility(View.VISIBLE);
            checkoutButton.setEnabled(false);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.emptyCartMessage).setVisibility(View.GONE);
            checkoutButton.setEnabled(true);
        }
    }

    @Override
    public void onItemRemoved(int position) {
        Product removedProduct = shoppingCart.get(position);
        CartManager.removeFromCart(this, removedProduct);
        shoppingCart.remove(position);
        adapter.notifyItemRemoved(position);
        updateTotalPrice();
        updateEmptyCartView();
    }

    @Override
    public void onQuantityChanged() {
        updateTotalPrice();
    }

    private void proceedToCheckout() {
        Intent checkoutIntent = new Intent(ShoppingCartActivity.this, CheckoutActivity.class);
        checkoutIntent.putExtra("totalPrice", totalPrice);
        startActivityForResult(checkoutIntent, CHECKOUT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHECKOUT_REQUEST && resultCode == RESULT_OK) {
            // Handle successful checkout
            CartManager.clearCart(this);
            updateCartItems();
            Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();
        }
    }
}