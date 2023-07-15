package com.ekumfi.juice.adapter;

/**
 * Created by Nana on 11/10/2017.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ekumfi.juice.realm.RealmSellerProduct;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.ekumfi.juice.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Belal on 6/6/2017.
 */

public class ProximityProductAdapter extends RecyclerView.Adapter<ProximityProductAdapter.ViewHolder> {

    private static final String YOUR_DIALOG_TAG = "";
    ContactMethodAdapterInterface contactMethodAdapterInterface;
    AddToCartAdapterInterface addToCartAdapterInterface;
    LocationClickInterface locationClickInterface;
    Activity mActivity;
    private ArrayList<RealmSellerProduct> realmSellerProducts;

    public ProximityProductAdapter(ContactMethodAdapterInterface contactMethodAdapterInterface, AddToCartAdapterInterface addToCartAdapterInterface, LocationClickInterface locationClickInterface, Activity mActivity, ArrayList<RealmSellerProduct> realmSellerProducts) {
        this.contactMethodAdapterInterface = contactMethodAdapterInterface;
        this.addToCartAdapterInterface = addToCartAdapterInterface;
        this.locationClickInterface = locationClickInterface;
        this.mActivity = mActivity;
        this.realmSellerProducts = realmSellerProducts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_proximity_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        RealmSellerProduct realmSellerProduct = realmSellerProducts.get(position);

        if (realmSellerProduct.getShop_image_url() != null && !realmSellerProduct.getShop_image_url().equals("")) {
            Glide.with(mActivity).
                    load(realmSellerProduct.getShop_image_url())
                    .into(holder.image);
        }
        
        Realm.init(mActivity);
        double distance = SphericalUtil.computeDistanceBetween(new LatLng(mActivity.getIntent().getDoubleExtra("LATITUDE", 0.0d), mActivity.getIntent().getDoubleExtra("LONGITUDE", 0.0d)), new LatLng(realmSellerProduct.getLatitude(), realmSellerProduct.getLongitude()));
        holder.distance.setText(String.format("%.2f", distance / 1000) + "km");
        holder.seller_name.setText(realmSellerProduct.getShop_name());
        holder.product_name.setText(realmSellerProduct.getProduct_name());
        holder.availability.setText(realmSellerProduct.getAvailability());
        holder.unit_price.setText("GHC" + String.format("%.2f", realmSellerProduct.getUnit_price()));

        switch (realmSellerProduct.getAvailability()) {
            case "Closed":
                holder.availability.setTextColor(Color.RED);
                break;
            case "Busy":
                holder.availability.setTextColor(0xFFDAA520);
                break;
            case "Available":
                holder.availability.setTextColor(0xFF32CD32);
                break;
            default:
                break;
        }
        holder.addtocart.setOnClickListener(view -> addToCartAdapterInterface.onListItemClick(realmSellerProducts, position, holder));

        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactMethodAdapterInterface.onListItemClick(realmSellerProducts, position, holder);
            }
        });

        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationClickInterface.onListItemClick(realmSellerProducts, position, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return realmSellerProducts.size();
    }

    public interface ContactMethodAdapterInterface {
        void onListItemClick(ArrayList<RealmSellerProduct> realmSellerProducts, int position, ViewHolder holder);
    }

    public interface AddToCartAdapterInterface {
        void onListItemClick(ArrayList<RealmSellerProduct> realmSellerProducts, int position, ViewHolder holder);
    }

    public interface LocationClickInterface {
        void onListItemClick(ArrayList<RealmSellerProduct> realmSellerProducts, int position, ViewHolder holder);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productcategory, seller_name, product_name, distance, contact, availability, unit_price;
        public LinearLayout parent, productInfoArea;
        public ImageView image;
        public Button addtocart;
        public TextView location;

        public ViewHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            productInfoArea = itemView.findViewById(R.id.productInfoArea);
            productcategory = itemView.findViewById(R.id.productcategory);
            seller_name = itemView.findViewById(R.id.seller_name);
            product_name = itemView.findViewById(R.id.product_name);
            image = itemView.findViewById(R.id.image);
            distance = itemView.findViewById(R.id.distance);
            addtocart = itemView.findViewById(R.id.addtocart);
            contact = itemView.findViewById(R.id.contact);
            availability = itemView.findViewById(R.id.availability);
            unit_price = itemView.findViewById(R.id.unit_price);
            location = itemView.findViewById(R.id.location);
        }
    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
}
