package com.chessytrooper.retailbusinessapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chessytrooper.retailbusinessapp.adapter.OrderItemAdapter;
import com.chessytrooper.retailbusinessapp.model.Order;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView orderIdTextView, orderDateTextView, orderTotalTextView;
    private RecyclerView orderItemsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderIdTextView = findViewById(R.id.orderIdTextView);
        orderDateTextView = findViewById(R.id.orderDateTextView);
        orderTotalTextView = findViewById(R.id.orderTotalTextView);
        orderItemsRecyclerView = findViewById(R.id.orderItemsRecyclerView);

        Order order = (Order) getIntent().getSerializableExtra("order");
        if (order != null) {
            displayOrderDetails(order);
        }
    }

    private void displayOrderDetails(Order order) {
        orderIdTextView.setText("Order ID: " + order.getId());
        orderDateTextView.setText("Date: " + order.getDate());
        orderTotalTextView.setText("Total: $" + order.getTotal());

        OrderItemAdapter adapter = new OrderItemAdapter(order.getItems());
        Log.d("OrderDetail", "oredering: " + order.getItems());
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderItemsRecyclerView.setAdapter(adapter);
    }
}