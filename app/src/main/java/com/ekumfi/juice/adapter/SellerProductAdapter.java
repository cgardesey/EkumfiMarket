package com.ekumfi.juice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ekumfi.juice.realm.RealmSellerProduct;
import com.ekumfi.juice.R;

import java.util.ArrayList;

/**
 * Created by Nana on 9/11/2017.
 */
public class SellerProductAdapter extends RecyclerView.Adapter<SellerProductAdapter.ViewHolder> {

    SellerProductAdapterInterface productAdapterInterface;
    ArrayList<RealmSellerProduct> realmSellerProducts;
    private Context mContext;

    public SellerProductAdapter(SellerProductAdapterInterface productAdapterInterface, ArrayList<RealmSellerProduct> realmSellerProducts) {
        this.productAdapterInterface = productAdapterInterface;
        this.realmSellerProducts = realmSellerProducts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycle_seller_product, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RealmSellerProduct realmSellerProduct = realmSellerProducts.get(position);

        holder.name.setText(realmSellerProduct.getProduct_name());
        holder.quantity_available.setText(String.valueOf(realmSellerProduct.getQuantity_available()));
        if (false) {
            holder.quantity_available.setTextColor(Color.RED);
        }
        else {
            holder.quantity_available.setTextColor(0xFF888888);
        }

        Glide.with(mContext).load(realmSellerProduct.getProduct_image_url())
                .into(holder.featured_image);

        mContext = holder.more_details.getContext();

        holder.more_details.setOnClickListener(view -> {
            productAdapterInterface.onListItemClick(realmSellerProducts, position, holder);
        });
    }

    @Override
    public int getItemCount() {
        return realmSellerProducts.size();
    }

    public interface SellerProductAdapterInterface {
        void onListItemClick(ArrayList<RealmSellerProduct> realmSellerProducts, int position, ViewHolder holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity_available;
        public ImageView more_details, featured_image;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            quantity_available = view.findViewById(R.id.quantity_available);
            more_details = view.findViewById(R.id.more_details);
            featured_image = view.findViewById(R.id.featured_image);
        }
    }
}
