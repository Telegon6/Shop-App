package com.chessytrooper.retailbusinessapp.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chessytrooper.retailbusinessapp.CartManager;
import com.chessytrooper.retailbusinessapp.CheckoutActivity;
import com.chessytrooper.retailbusinessapp.database.CartDatabaseHelper;
import com.chessytrooper.retailbusinessapp.R;
import com.chessytrooper.retailbusinessapp.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReviewFragment extends Fragment {

    private TextView totalPriceTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        totalPriceTextView = view.findViewById(R.id.totalPriceTextView);

        double totalPrice = ((CheckoutActivity) requireActivity()).getTotalPrice();
        totalPriceTextView.setText(String.format("Total: $%.2f", totalPrice));

        Button confirmPurchaseButton = view.findViewById(R.id.confirmPurchaseButton);
        confirmPurchaseButton.setOnClickListener(v -> confirmPurchase());

        return view;
    }

    private void confirmPurchase() {
        // Create an Order object
        Order order = new Order();
        order.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        order.setTotal(((CheckoutActivity) requireActivity()).getTotalPrice());
        order.setItems(CartManager.getCartItems(requireContext()));

        CartDatabaseHelper cartDatabaseHelper = new CartDatabaseHelper(requireContext());
        // Add the order to the database
        long orderId = cartDatabaseHelper.addOrder(order);

        if (orderId != -1) {
            // Order successfully added
            CartManager.clearCart(requireContext());
            requireActivity().setResult(Activity.RESULT_OK);
            requireActivity().finish();
        } else {
            // Failed to add order
            Toast.makeText(requireContext(), "Failed to place order", Toast.LENGTH_SHORT).show();
        }
    }
}