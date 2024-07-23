package com.chessytrooper.retailbusinessapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.chessytrooper.retailbusinessapp.model.Product;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImageView;
    private TextView productNameTextView, productDescriptionTextView, productPriceTextView;
    private Button addToCartButton;
    private Product product;

    private BroadcastReceiver cartUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateCartButtonStatus();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImageView = findViewById(R.id.productImageView);
        productNameTextView = findViewById(R.id.productNameTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        productPriceTextView = findViewById(R.id.productPriceTextView);
        addToCartButton = findViewById(R.id.addToCartButton);

        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            displayProductDetails();
        }

        addToCartButton.setOnClickListener(v -> toggleCartStatus());
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("com.chessytrooper.retailbusinessapp.CART_UPDATED");
        registerReceiver(cartUpdateReceiver, filter);
        updateCartButtonStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(cartUpdateReceiver);
    }

    private void displayProductDetails() {
        productNameTextView.setText(product.getName());
        productDescriptionTextView.setText(product.getDescription());
        productPriceTextView.setText("Price: $" + product.getNgnPrice());

        if (product.getPhotos() != null && !product.getPhotos().isEmpty()) {
            Glide.with(this)
                    .load("https://api.timbu.cloud/images/" + product.getPhotos().get(0).getUrl())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(productImageView);
        }

        updateCartButtonStatus();
    }

    private void toggleCartStatus() {
        if (CartManager.isProductInCart(this, product)) {
            CartManager.removeFromCart(this, product);
        } else {
            CartManager.addToCart(this, product);
        }
        updateCartButtonStatus();
    }

    private void updateCartButtonStatus() {
        if (CartManager.isProductInCart(this, product)) {
            addToCartButton.setText("Remove from Cart");
        } else {
            addToCartButton.setText("Add to Cart");
        }
    }
}