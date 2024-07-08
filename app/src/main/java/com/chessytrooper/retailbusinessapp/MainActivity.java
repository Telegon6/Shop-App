package com.chessytrooper.retailbusinessapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.chessytrooper.retailbusinessapp.databinding.ActivityMainBinding;
import com.chessytrooper.retailbusinessapp.adapter.ProductAdapter;
import com.chessytrooper.retailbusinessapp.viewmodel.ProductViewModel;

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
                adapter = new ProductAdapter(products);
                binding.recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
