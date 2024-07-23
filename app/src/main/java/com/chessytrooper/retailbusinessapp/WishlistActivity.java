package com.chessytrooper.retailbusinessapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chessytrooper.retailbusinessapp.adapter.ProductAdapter;
import com.chessytrooper.retailbusinessapp.model.Product;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        recyclerView = findViewById(R.id.wishlistRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Product> wishlistItems = WishlistManager.getWishlistItems();
        adapter = new ProductAdapter(wishlistItems, this);
        recyclerView.setAdapter(adapter);
    }
}