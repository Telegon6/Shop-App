package com.chessytrooper.retailbusinessapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chessytrooper.retailbusinessapp.adapter.OrderHistoryAdapter;
import com.chessytrooper.retailbusinessapp.database.CartDatabaseHelper;
import com.chessytrooper.retailbusinessapp.model.Order;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.orderHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(this);

        List<Order> orderHistory = cartDatabaseHelper.getAllOrders();
        adapter = new OrderHistoryAdapter(orderHistory, this);
        recyclerView.setAdapter(adapter);
    }
}