package com.chessytrooper.retailbusinessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chessytrooper.retailbusinessapp.adapter.ProductAdapter;
import com.chessytrooper.retailbusinessapp.databinding.ActivityMainBinding;
import com.chessytrooper.retailbusinessapp.model.Product;
import com.chessytrooper.retailbusinessapp.viewmodel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int SHOPPING_CART_REQUEST = 1;
    private ActivityMainBinding binding;
    private ProductViewModel viewModel;
    private ProductAdapter adapter;
    private List<Product> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        viewModel.getProducts().observe(this, products -> {
            if (products != null) {
                adapter = new ProductAdapter(products, new ProductAdapter.OnAddToCartListener() {
                    @Override
                    public void onAddToCart(Product product) {
                        addToCart(product);
                    }
                });
                binding.recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });

        binding.openCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
                intent.putExtra("cartItems", new ArrayList<>(cartItems));
                startActivityForResult(intent, SHOPPING_CART_REQUEST);
            }
        });
    }

    private void addToCart(Product product) {
        // Check if the product is already in the cart
        for (Product cartProduct : cartItems) {
            if (cartProduct.getName().equals(product.getName())) {
                // If it's already in the cart, just increment the quantity
                cartProduct.setQuantity(cartProduct.getQuantity() + 1);
                Toast.makeText(this, product.getName() + " quantity increased in cart", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // If it's not in the cart, add it as a new item
        Product cartProduct = new Product(product);
        cartProduct.setQuantity(1);
        cartItems.add(cartProduct);
        Toast.makeText(this, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SHOPPING_CART_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("updatedCartItems")) {
                cartItems = (List<Product>) data.getSerializableExtra("updatedCartItems");
            }
        }
    }
}
