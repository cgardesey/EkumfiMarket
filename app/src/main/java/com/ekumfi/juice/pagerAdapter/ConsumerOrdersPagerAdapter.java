package com.ekumfi.juice.pagerAdapter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ekumfi.juice.fragment.ConsumerOrderFragment;

import java.util.ArrayList;

public class ConsumerOrdersPagerAdapter extends FragmentPagerAdapter {
    ArrayList<String> statuses;

    public ConsumerOrdersPagerAdapter(FragmentManager fm, ArrayList<String> statuses) {
        super(fm);
        this.statuses = statuses;
    }

    @Override
    public Fragment getItem(int position) {
        ConsumerOrderFragment orderFragment = new ConsumerOrderFragment();
        Bundle bundle = new Bundle(3);
        bundle.putString("status", getPageTitle(position).toString());
        orderFragment.setArguments(bundle);

        return orderFragment;
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