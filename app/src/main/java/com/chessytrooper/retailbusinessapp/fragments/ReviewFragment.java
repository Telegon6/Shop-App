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

import com.chessytrooper.retailbusinessapp.CheckoutActivity;
import com.chessytrooper.retailbusinessapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
        // Here you would typically process the payment
        // For this example, we'll just simulate a successful purchase
        requireActivity().setResult(Activity.RESULT_OK);
        requireActivity().finish();
    }
}