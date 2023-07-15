package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.R;
import com.ekumfi.juice.adapter.StockCartItemAdapter;
import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.constants.keyConst;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmCart;
import com.ekumfi.juice.realm.RealmStockCartProduct;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class StockCartItemsActivity extends AppCompatActivity {
    NetworkReceiver networkReceiver;
    Button backbtn1;
    RecyclerView recyclerview;
    StockCartItemAdapter stockCartItemAdapter;
    Button order;
    TextView total, invoice_sub_total, shipping_fee, total_fee;
    LinearLayout invoice_layout;
    RelativeLayout total_layout;
    ArrayList<RealmStockCartProduct> cartItemsArrayList = new ArrayList<>(), newCartItems = new ArrayList<>();

    float totalamt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_cart_items);
        recyclerview = findViewById(R.id.recyclerview);
        order = findViewById(R.id.order);
        total_layout = findViewById(R.id.total_layout);
        invoice_layout = findViewById(R.id.invoice_layout);
        total = findViewById(R.id.total);
        invoice_sub_total = findViewById(R.id.invoice_sub_total);
        shipping_fee = findViewById(R.id.shipping_fee);
        total_fee = findViewById(R.id.total_fee);

        if (getIntent().getBooleanExtra("LAUNCHED_FROM_CHAT", false)) {
            order.setVisibility(View.GONE);
        }

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OrderSummaryActivity.class)
                        .putExtra("ITEM_COUNT", cartItemsArrayList.size())
                        .putExtra("SUB_TOTAL", totalamt)
                        .putExtra("SHIPPING_FEE", 20.00F)
                        .putExtra("STOCK_CART_ID", getIntent().getStringExtra("STOCK_CART_ID")));
            }
        });


        stockCartItemAdapter = new StockCartItemAdapter(new StockCartItemAdapter.StockCartItemAdapterInterface() {

            @Override
            public void onFavClick(ArrayList<RealmStockCartProduct> names, int position, StockCartItemAdapter.ViewHolder holder) {
                Drawable currentDrawable = holder.fav.getDrawable();

                Drawable favFilledDrawable = getResources().getDrawable(R.drawable.fav_filled);
                Drawable favOutlinedIconDrawable = getResources().getDrawable(R.drawable.fav_outlined);

                Drawable.ConstantState favFilledIconConstantState = favFilledDrawable.getConstantState();
                Drawable.ConstantState favOutlinedIconConstantState = favOutlinedIconDrawable.getConstantState();
                Drawable.ConstantState currentIconConstantState = currentDrawable.getConstantState();
                if (currentIconConstantState.equals(favOutlinedIconConstantState)) {
                    holder.fav.setImageDrawable(getResources().getDrawable(R.drawable.fav_filled));
                } else {
                    holder.fav.setImageDrawable(getResources().getDrawable(R.drawable.fav_outlined));
                }
            }

            @Override
            public void onRemoveClick(ArrayList<RealmStockCartProduct> realmStockCartProducts, int position, StockCartItemAdapter.ViewHolder holder) {
                RealmStockCartProduct realmStockCartProduct = realmStockCartProducts.get(position);
                String stock_cart_product_id = realmStockCartProduct.getStock_cart_product_id();
                String stock_cart_id = realmStockCartProduct.getStock_cart_id();
                StringRequest stringRequest = new StringRequest(
                        Request.Method.DELETE,
                        keyConst.API_URL + "stock-cart-products/" + stock_cart_product_id,
                        response -> {
                            if (response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getBoolean("status")) {
                                        Realm.init(getApplicationContext());
                                        Realm.getInstance(RealmUtility.getDefaultConfig(StockCartItemsActivity.this)).executeTransaction(realm -> {
                                            totalamt -= realmStockCartProduct.getUnit_price() * realmStockCartProduct.getQuantity();
                                            realm.where(RealmStockCartProduct.class).equalTo("stock_cart_product_id", stock_cart_product_id).findFirst().deleteFromRealm();
                                        });
                                        Toast.makeText(StockCartItemsActivity.this, "Successfully deleted.", Toast.LENGTH_SHORT).show();
                                        if (realmStockCartProducts.size() == 1) {
                                            Realm.init(getApplicationContext());
                                            Realm.getInstance(RealmUtility.getDefaultConfig(StockCartItemsActivity.this)).executeTransaction(realm -> {
                                                realm.where(RealmCart.class).equalTo("stock_cart_id", stock_cart_id).findFirst().deleteFromRealm();
                                            });
                                            finish();
                                        } else {
                                            total.setText("GHC" + String.format("%.2f", totalamt));
                                            cartItemsArrayList.remove(position);
                                            stockCartItemAdapter.notifyItemRemoved(position);
                                        }
                                    } else {
                                        Toast.makeText(StockCartItemsActivity.this, "Error deleting.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            Const.myVolleyError(StockCartItemsActivity.this, error);
                            Log.d("Cyrilll", error.toString());
                        }
                ) {
                    @Override
                    public Map getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("accept", "application/json");
                        headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + APITOKEN, ""));
                        return headers;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                InitApplication.getInstance().addToRequestQueue(stringRequest);
            }

            @Override
            public void onQuantityUpdateClick(ArrayList<RealmStockCartProduct> realmStockCartProducts, int position, StockCartItemAdapter.ViewHolder holder) {
                RealmStockCartProduct realmStockCartProduct = realmStockCartProducts.get(position);
                int quantity = holder.numberPicker.getValue();
                double price = realmStockCartProduct.getUnit_price() * quantity;
                String stock_cart_product_id = realmStockCartProduct.getStock_cart_product_id();

                ProgressDialog mProgress = new ProgressDialog(StockCartItemsActivity.this);
                mProgress.setCancelable(false);
                mProgress.setIndeterminate(true);

                mProgress.setTitle("Updating quantity...");
                mProgress.show();

                StringRequest stringRequest = new StringRequest(
                        Request.Method.PATCH,
                        keyConst.API_URL + "stock-cart-products/" + stock_cart_product_id,
                        response -> {
                            mProgress.dismiss();
                            if (response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    final RealmStockCartProduct[] cartProduct = new RealmStockCartProduct[1];
                                    Realm.init(getApplicationContext());
                                    Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).executeTransaction(realm -> {
                                        totalamt -= realmStockCartProduct.getUnit_price() * realmStockCartProduct.getQuantity();
                                        cartProduct[0] = realm.createOrUpdateObjectFromJson(RealmStockCartProduct.class, response);
                                        double updatedprice = cartProduct[0].getUnit_price() * cartProduct[0].getQuantity();
                                        //                                        Toast.makeText(getApplicationContext(), String.valueOf(updatedprice), Toast.LENGTH_SHORT).show();
                                        holder.price.setText("GHC" + String.format("%.2f", updatedprice));
                                        StockCartItemsActivity.this.totalamt += updatedprice;

                                    });
                                    Toast.makeText(StockCartItemsActivity.this, "Quantity successfully updated.", Toast.LENGTH_SHORT).show();
                                    total.setText("GHC" + String.format("%.2f", totalamt));
                                    stockCartItemAdapter.notifyItemChanged(position, cartProduct[0]);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            mProgress.dismiss();
                            error.printStackTrace();
                            Const.myVolleyError(StockCartItemsActivity.this, error);
                            Log.d("Cyrilll", error.toString());
                        }
                ) {
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("quantity", String.valueOf(quantity));
                        params.put("price", String.valueOf(price));
                        return params;
                    }

                    @Override
                    public Map getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("accept", "application/json");
                        headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + APITOKEN, ""));
                        return headers;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                InitApplication.getInstance().addToRequestQueue(stringRequest);
            }
        }, StockCartItemsActivity.this, cartItemsArrayList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerview.setAdapter(stockCartItemAdapter);

        populateCartItems(getApplicationContext());

        if (getIntent().getBooleanExtra("IS_INVOICE", false)) {
            invoice_layout.setVisibility(View.VISIBLE);
            total_layout.setVisibility(View.GONE);

            invoice_sub_total.setText("GHC" + String.format("%.2f", getIntent().getFloatExtra("INVOICE_SUB_TOTAL", 0.00F)));
            shipping_fee.setText("GHC" + String.format("%.2f", getIntent().getFloatExtra("SHIPPING_FEE", 0.00F)));
//            total_fee.setText("GHC" + String.format("%.2f", getIntent().getFloatExtra("INVOICE_SUB_TOTAL", 0.00F) + getIntent().getFloatExtra("SHIPPING_FEE", 0.00F)));
            total_fee.setText("GHC" + String.format("%.2f", getIntent().getFloatExtra("INVOICE_SUB_TOTAL", 0.00F)));
        } else {
            invoice_layout.setVisibility(View.GONE);
            total_layout.setVisibility(View.VISIBLE);

            total.setText("GHC" + String.format("%.2f", totalamt));
        }

        networkReceiver = new NetworkReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activeActivity = this;
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    void populateCartItems(final Context context) {
        Realm.init(context);
        Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
            RealmResults<RealmStockCartProduct> results;
            results = realm.where(RealmStockCartProduct.class).findAll();
            newCartItems.clear();
            for (RealmStockCartProduct realmStockCartProduct : results) {
                totalamt += realmStockCartProduct.getUnit_price() * realmStockCartProduct.getQuantity();
                newCartItems.add(realmStockCartProduct);
            }
            cartItemsArrayList.clear();
            cartItemsArrayList.addAll(newCartItems);
            stockCartItemAdapter.notifyDataSetChanged();
        });
    }
}
