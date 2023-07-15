package com.ekumfi.juice.pagerAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ekumfi.juice.fragment.SellerAccountFragment1;
import com.ekumfi.juice.fragment.SellerAccountFragment2;
import com.ekumfi.juice.fragment.SellerAccountFragment3;

public class SellerAccountPageAdapter extends FragmentPagerAdapter {

    public SellerAccountPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                SellerAccountFragment1 sellerAccountFragment1 = new SellerAccountFragment1();
                return sellerAccountFragment1;
            case 1:
                SellerAccountFragment2 sellerAccountFragment2 = new SellerAccountFragment2();
                return sellerAccountFragment2;
            case 2:
                SellerAccountFragment3 sellerAccountFragment3 = new SellerAccountFragment3();
                return sellerAccountFragment3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}