package com.ekumfi.juice.adapter;

/**
 * Created by Nana on 11/10/2017.
 */

import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ekumfi.juice.realm.RealmCartProduct;
import com.ekumfi.juice.R;

import java.util.ArrayList;

import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

/**
 * Created by Belal on 6/6/2017.
 */

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    private static final String YOUR_DIALOG_TAG = "";
    CartItemAdapterInterface cartItemAdapterInterface;
    Activity mActivity;
    private ArrayList<RealmCartProduct> realmCartProducts;

    public CartItemAdapter(CartItemAdapterInterface cartItemAdapterInterface, Activity mActivity, ArrayList<RealmCartProduct> realmCartProducts) {
        this.cartItemAdapterInterface = cartItemAdapterInterface;
        this.mActivity = mActivity;
        this.realmCartProducts = realmCartProducts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_cart_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        RealmCartProduct realmCartProduct = realmCartProducts.get(position);

        if (mActivity.getIntent().getBooleanExtra("IS_INVOICE", false) || mActivity.getIntent().getBooleanExtra("LAUNCHED_FROM_CHAT", false)) {
            holder.details_layout.setVisibility(View.GONE);
        } else {
            holder.details_layout.setVisibility(View.VISIBLE);

            holder.numberPicker.setValue(realmCartProduct.getQuantity());
            holder.numberPicker.setMin(realmCartProduct.getUnit_quantity());
            holder.numberPicker.setMax(realmCartProduct.getQuantity_available());
            holder.numberPicker.setUnit(realmCartProduct.getUnit_quantity());

            holder.numberPicker.setValueChangedListener(new ValueChangedListener() {
                @Override
                public void valueChanged(int value, ActionEnum action) {
                    cartItemAdapterInterface.onQuantityUpdateClick(realmCartProducts, position, holder);
                }
            });
            holder.numberPicker.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    cartItemAdapterInterface.onQuantityUpdateClick(realmCartProducts, position, holder);
                    holder.numberPicker.clearFocus();
                    return false;
                }
            });
        }
        if (realmCartProduct.getImage_url() != null && !realmCartProduct.getImage_url().equals("")) {
            Glide.with(mActivity).
                    load(realmCartProduct.getImage_url())
                    .into(holder.image);
        }
        String[] split = realmCartProduct.getName().split(" >> ");
        holder.product.setText(split[split.length - 1]);
        holder.price.setText("GHC" + String.format("%.2f", realmCartProduct.getUnit_price() * realmCartProduct.getQuantity()));

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItemAdapterInterface.onFavClick(realmCartProducts, position, holder);
            }
        });

        holder.remove_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartItemAdapterInterface.onRemoveClick(realmCartProducts, position, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return realmCartProducts.size();
    }

    public interface CartItemAdapterInterface {
        void onFavClick(ArrayList<RealmCartProduct> names, int position, ViewHolder holder);
        void onRemoveClick(ArrayList<RealmCartProduct> names, int position, ViewHolder holder);
        void onQuantityUpdateClick(ArrayList<RealmCartProduct> realmCartProducts, int position, ViewHolder holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView product, currency, price;
        public ImageView image, fav;
        public LinearLayout remove_layout;
        public NumberPicker numberPicker;
        public LinearLayout details_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            currency = itemView.findViewById(R.id.currency);
            price = itemView.findViewById(R.id.price);
            product = itemView.findViewById(R.id.product);
            image = itemView.findViewById(R.id.image);
            fav = itemView.findViewById(R.id.fav);
            numberPicker = itemView.findViewById(R.id.numberPicker);
            remove_layout = itemView.findViewById(R.id.remove_layout);
            details_layout = itemView.findViewById(R.id.details_layout);
        }
    }
}
