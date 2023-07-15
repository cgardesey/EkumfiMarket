package com.ekumfi.juice.pagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ekumfi.juice.fragment.ConsumerAccountFragment1;

public class ConsumerAccountPageAdapter extends FragmentPagerAdapter {

    public ConsumerAccountPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ConsumerAccountFragment1 tab1 = new ConsumerAccountFragment1();
                return tab1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }
}