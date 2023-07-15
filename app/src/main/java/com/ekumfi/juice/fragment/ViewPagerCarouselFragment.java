package com.ekumfi.juice.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ekumfi.juice.R;

public class ViewPagerCarouselFragment extends Fragment {
    public static final String IMAGE_RESOURCE_ID = "image_resource_id";

    private ImageView ivCarouselImage;
    private String imageResourceId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_pager_carousel_fragment, container, false);
        ivCarouselImage = v.findViewById(R.id.iv_carousel_image);
        imageResourceId = getArguments().getString(IMAGE_RESOURCE_ID); // default to car1 image resource
       // ivCarouselImage.setImageResource(imageResourceId);
        Glide.with(getActivity())
                .load(imageResourceId) // image url
                .into(ivCarouselImage);
        v.setOnClickListener(v1 -> {
        });

        return v;
    }
}