package com.chessytrooper.retailbusinessapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.chessytrooper.retailbusinessapp.model.Product;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImageView;
    private TextView productNameTextView, productDescriptionTextView, productPriceTextView;
    private Button addToCartButton;
    private Product product;

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
        if (CartManager.isProductInCart(product)) {
            CartManager.removeFromCart(product);
        } else {
            CartManager.addToCart(product);
        }
        updateCartButtonStatus();
    }

    private void updateCartButtonStatus() {
        if (CartManager.isProductInCart(product)) {
            addToCartButton.setText("Remove from Cart");
        } else {
            addToCartButton.setText("Add to Cart");
        }
    }
}