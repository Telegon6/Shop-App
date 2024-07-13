package com.chessytrooper.retailbusinessapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.chessytrooper.retailbusinessapp.fragments.PaymentFragment;
import com.chessytrooper.retailbusinessapp.fragments.ReviewFragment;
import com.chessytrooper.retailbusinessapp.fragments.ShippingFragment;

public class CheckoutPagerAdapter extends FragmentStateAdapter {

    public CheckoutPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ShippingFragment();
            case 1:
                return new PaymentFragment();
            case 2:
                return new ReviewFragment();
            default:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}