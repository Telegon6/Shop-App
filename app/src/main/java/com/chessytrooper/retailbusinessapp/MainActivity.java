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
    private ActivityMainBinding binding;
    private ProductViewModel viewModel;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        viewModel.getProducts().observe(this, products -> {
            if (products != null) {
                adapter = new ProductAdapter(products, this);
                binding.recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });

        binding.openCartButton.setOnClickListener(v -> openShoppingCart());
    }

    private void openShoppingCart() {
        Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
        startActivity(intent);
    }
}
