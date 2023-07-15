package com.ekumfi.juice.pagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ekumfi.juice.fragment.RiderProviderAccountFragment1;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment2;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment3;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment4;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment5;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment6;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment7;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment8;

public class RiderAccountPageAdapter extends FragmentPagerAdapter {

    public RiderAccountPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                RiderProviderAccountFragment1 riderProviderAccountFragment1 = new RiderProviderAccountFragment1();
                return riderProviderAccountFragment1;
            case 1:
                RiderProviderAccountFragment2 riderProviderAccountFragment2 = new RiderProviderAccountFragment2();
                return riderProviderAccountFragment2;
            case 2:
                RiderProviderAccountFragment3 riderProviderAccountFragment3 = new RiderProviderAccountFragment3();
                return riderProviderAccountFragment3;
            case 3:
                RiderProviderAccountFragment4 riderProviderAccountFragment4 = new RiderProviderAccountFragment4();
                return riderProviderAccountFragment4;
            case 4:
                RiderProviderAccountFragment5 riderProviderAccountFragment5 = new RiderProviderAccountFragment5();
                return riderProviderAccountFragment5;
            case 5:
                RiderProviderAccountFragment6 riderProviderAccountFragment6 = new RiderProviderAccountFragment6();
                return riderProviderAccountFragment6;
            case 6:
                RiderProviderAccountFragment7 riderProviderAccountFragment7 = new RiderProviderAccountFragment7();
                return riderProviderAccountFragment7;
            case 7:
                RiderProviderAccountFragment8 riderProviderAccountFragment8 = new RiderProviderAccountFragment8();
                return riderProviderAccountFragment8;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 8;
    }
}