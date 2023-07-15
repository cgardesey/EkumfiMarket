package com.ekumfi.juice.pagerAdapter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ekumfi.juice.fragment.SellerOrderFragment;
import com.ekumfi.juice.fragment.StockOrderFragment;

import java.util.ArrayList;

public class StockOrdersPagerAdapter extends FragmentPagerAdapter {
    ArrayList<String> statuses;

    public StockOrdersPagerAdapter(FragmentManager fm, ArrayList<String> statuses) {
        super(fm);
        this.statuses = statuses;
    }

    @Override
    public Fragment getItem(int position) {
        StockOrderFragment stockOrderFragment = new StockOrderFragment();
        Bundle bundle = new Bundle(2);
        bundle.putString("status", getPageTitle(position).toString());
        stockOrderFragment.setArguments(bundle);

        return stockOrderFragment;
    }

    @Override
    public int getCount() {
        return statuses.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return statuses.get(position);
    }
}