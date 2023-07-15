package com.ekumfi.juice.adapter;

/**
 * Created by Nana on 11/10/2017.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ekumfi.juice.realm.RealmProduct;
import com.ekumfi.juice.R;

import java.util.ArrayList;

/**
 * Created by Belal on 6/6/2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private static final String YOUR_DIALOG_TAG = "";
    ListAdapterInterface listAdapterInterface;
    Activity mActivity;
    String title = "";
    private ArrayList<RealmProduct> realmProducts;

    public ProductListAdapter(ListAdapterInterface listAdapterInterface, Activity mActivity, ArrayList<RealmProduct> realmProducts, String title) {
        this.listAdapterInterface = listAdapterInterface;
        this.mActivity = mActivity;
        this.realmProducts = realmProducts;
        this.title = title;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_product_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RealmProduct realmProduct = realmProducts.get(position);

        holder.product_icon.setVisibility(View.VISIBLE);
        Glide.with(mActivity).
                load(realmProduct.getImage_url())
                .into(holder.product_icon);

        holder.product_name.setText(realmProduct.getName());
        holder.parent.setOnClickListener(view -> listAdapterInterface.onListItemClick(realmProducts, position, holder));
    }

    @Override
    public int getItemCount() {
        return realmProducts.size();
    }

    public interface ListAdapterInterface {
        void onListItemClick(ArrayList<RealmProduct> names, int position, ViewHolder holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView product_name;
        public ImageView product_icon;
        public LinearLayout parent;

        public ViewHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            parent = itemView.findViewById(R.id.parent);
            product_icon = itemView.findViewById(R.id.product_icon);
        }
    }
}
