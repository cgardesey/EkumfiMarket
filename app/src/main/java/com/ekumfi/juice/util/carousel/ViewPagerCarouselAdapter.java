package com.ekumfi.juice.util.carousel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ekumfi.juice.realm.RealmBanner;

import java.util.ArrayList;

/**
 * Created by Nana on 11/11/2017.
 */

public class ViewPagerCarouselAdapter extends FragmentStatePagerAdapter {
    private ArrayList<RealmBanner> imageResourceIds;

    public ViewPagerCarouselAdapter(FragmentManager fm, ArrayList<RealmBanner> imageResourceIds) {
        super(fm);
        this.imageResourceIds = imageResourceIds;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(ViewPagerCarouselFragment.IMAGE_RESOURCE_ID, imageResourceIds.get(position).getUrl());
        ViewPagerCarouselFragment frag = new ViewPagerCarouselFragment();
        frag.setArguments(bundle);

        return frag;
    }

    @Override
    public int getCount() {
        return (imageResourceIds == null) ? 0: imageResourceIds.size();
    }

}