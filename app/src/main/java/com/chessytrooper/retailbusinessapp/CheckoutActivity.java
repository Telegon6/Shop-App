package com.chessytrooper.retailbusinessapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.chessytrooper.retailbusinessapp.adapter.CheckoutPagerAdapter;
import com.chessytrooper.retailbusinessapp.fragments.PaymentFragment;
import com.chessytrooper.retailbusinessapp.fragments.ShippingFragment;
import com.chessytrooper.retailbusinessapp.fragments.ReviewFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        CheckoutPagerAdapter pagerAdapter = new CheckoutPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("Shipping");
                                tab.setIcon(R.drawable.ic_launcher_foreground);
                                break;
                            case 1:
                                tab.setText("Payment");
                                tab.setIcon(R.drawable.ic_launcher_foreground);
                                break;
                            case 2:
                                tab.setText("Review");
                                tab.setIcon(R.drawable.ic_launcher_foreground);
                                break;
                        }
                    }
                });
        tabLayoutMediator.attach();
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}