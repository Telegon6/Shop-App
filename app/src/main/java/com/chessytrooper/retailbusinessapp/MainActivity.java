package com.chessytrooper.retailbusinessapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chessytrooper.retailbusinessapp.adapter.ProductAdapter;
import com.chessytrooper.retailbusinessapp.databinding.ActivityMainBinding;
import com.chessytrooper.retailbusinessapp.viewmodel.ProductViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ProductViewModel viewModel;
    private ProductAdapter adapter;

    private BroadcastReceiver cartUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateCartButtonsState();
        }
    };

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
                updateCartButtonsState();
            } else {
                Toast.makeText(this, "Failed to load products", Toast.LENGTH_SHORT).show();
            }
        });

        binding.openCartButton.setOnClickListener(v -> openShoppingCart());
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("com.chessytrooper.retailbusinessapp.CART_UPDATED");
        registerReceiver(cartUpdateReceiver, filter);
        updateCartButtonsState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(cartUpdateReceiver);
    }

    private void updateCartButtonsState() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void openShoppingCart() {
        Intent intent = new Intent(MainActivity.this, ShoppingCartActivity.class);
        startActivity(intent);
    }
}