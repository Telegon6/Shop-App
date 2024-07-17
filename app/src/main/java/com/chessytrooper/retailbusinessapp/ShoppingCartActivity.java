package com.chessytrooper.retailbusinessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chessytrooper.retailbusinessapp.adapter.ProductAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToCheckout();
            }
        });

        List<Product> cartItems = CartManager.getCartItems();
        shoppingCart = cartItems;
        if (shoppingCart != null && !shoppingCart.isEmpty()) {
            adapter = new ShoppingCartAdapter(shoppingCart, this, this);
            recyclerView.setAdapter(adapter);
            updateTotalPrice();
        } else {
            showEmptyCartMessage();
        }
    }

    private void updateTotalPrice() {
        totalPrice = 0;
        for (Product product : shoppingCart) {
            totalPrice += Double.parseDouble(product.getNgnPrice()) * product.getQuantity();
        }
        totalPriceTextView.setText(String.format("Total: $%.2f", totalPrice));
    }

    private void showEmptyCartMessage() {
        Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
        // You might want to add a TextView to show this message in the UI as well
    }

    @Override
    public void onItemRemoved(int position) {
        shoppingCart.remove(position);
        adapter.notifyItemRemoved(position);
        if (shoppingCart.isEmpty()) {
            showEmptyCartMessage();
        }
        updateTotalPrice();
        sendUpdatedCartBack();
    }

    @Override
    public void onQuantityChanged() {
        updateTotalPrice();
        sendUpdatedCartBack();
    }

    private void sendUpdatedCartBack() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updatedCartItems", new ArrayList<>(shoppingCart));
        setResult(RESULT_OK, resultIntent);
    }

    @Override
    public void onBackPressed() {
        sendUpdatedCartBack();
        super.onBackPressed();
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
            // Handle successful checkout (e.g., clear cart, show confirmation)
            shoppingCart.clear();
            adapter.notifyDataSetChanged();
            updateTotalPrice();
            sendUpdatedCartBack();
            Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();
        }
    }
}
