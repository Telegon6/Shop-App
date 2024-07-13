package com.chessytrooper.retailbusinessapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.chessytrooper.retailbusinessapp.adapter.CheckoutPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class CheckoutActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        CheckoutPagerAdapter pagerAdapter = new CheckoutPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Shipping");
                            break;
                        case 1:
                            tab.setText("Payment");
                            break;
                        case 2:
                            tab.setText("Review");
                            break;
                    }
                }
        ).attach();
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}