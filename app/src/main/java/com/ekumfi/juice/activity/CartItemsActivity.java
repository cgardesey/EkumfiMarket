package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.ekumfi.juice.adapter.CartItemAdapter;
import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.constants.keyConst;
import com.ekumfi.juice.materialDialog.ChoosePaymentMethodMaterialDialog;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmCartProduct;
import com.ekumfi.juice.realm.RealmCart;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.ekumfi.juice.util.RealmUtility;
import com.ekumfi.juice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class CartItemsActivity extends AppCompatActivity {
    NetworkReceiver networkReceiver;
    Button backbtn1;
    RecyclerView recyclerview;
    CartItemAdapter cartItemAdapter;
    Button order;
    TextView total, invoice_sub_total, shipping_fee, total_fee;
    LinearLayout invoice_layout;
    RelativeLayout total_layout;
    ArrayList<RealmCartProduct> cartItemsArrayList = new ArrayList<>(), newCartItems = new ArrayList<>();

    float totalamt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_items);
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
                /*startActivity(new Intent(getApplicationContext(), OrderSummaryActivity.class)
                        .putExtra("ITEM_COUNT", cartItemsArrayList.size())
                        .putExtra("SUB_TOTAL", totalamt)
                        .putExtra("SHIPPING_FEE", 20.00F)
                        .putExtra("CART_ID", getIntent().getStringExtra("CART_ID")));*/

                ChoosePaymentMethodMaterialDialog choosePaymentMethodMaterialDialog = new ChoosePaymentMethodMaterialDialog();
                if (choosePaymentMethodMaterialDialog != null && choosePaymentMethodMaterialDialog.isAdded()) {

                } else {
                    choosePaymentMethodMaterialDialog.setAmount(String.valueOf(getIntent().getFloatExtra("SUB_TOTAL", 0.00F)));
                    if (androidx.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER")) {
                        choosePaymentMethodMaterialDialog.setCart_id(getIntent().getStringExtra("CART_ID"));
                    } else {
                        choosePaymentMethodMaterialDialog.setStock_cart_id(getIntent().getStringExtra("STOCK_CART_ID"));
                    }
                    choosePaymentMethodMaterialDialog.show(getSupportFragmentManager(), "choosePaymentMethodMaterialDialog");
                    choosePaymentMethodMaterialDialog.setCancelable(true);
                }
            }
        });


        cartItemAdapter = new CartItemAdapter(new CartItemAdapter.CartItemAdapterInterface() {

            @Override
            public void onFavClick(ArrayList<RealmCartProduct> names, int position, CartItemAdapter.ViewHolder holder) {
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
            public void onRemoveClick(ArrayList<RealmCartProduct> realmCartProducts, int position, CartItemAdapter.ViewHolder holder) {
                RealmCartProduct realmCartProduct = realmCartProducts.get(position);
                String cart_product_id = realmCartProduct.getCart_product_id();
                String cart_id = realmCartProduct.getCart_id();
                StringRequest stringRequest = new StringRequest(
                        Request.Method.DELETE,
                        keyConst.API_URL + "cart-products/" + cart_product_id,
                        response -> {
                            if (response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getBoolean("status")) {
                                        Realm.init(getApplicationContext());
                                        Realm.getInstance(RealmUtility.getDefaultConfig(CartItemsActivity.this)).executeTransaction(realm -> {
                                            totalamt -= realmCartProduct.getUnit_price() * realmCartProduct.getQuantity();
                                            realm.where(RealmCartProduct.class).equalTo("cart_product_id", cart_product_id).findFirst().deleteFromRealm();
                                        });
                                        Toast.makeText(CartItemsActivity.this, "Successfully deleted.", Toast.LENGTH_SHORT).show();
                                        if (realmCartProducts.size() == 1) {
                                            Realm.init(getApplicationContext());
                                            Realm.getInstance(RealmUtility.getDefaultConfig(CartItemsActivity.this)).executeTransaction(realm -> {
                                                realm.where(RealmCart.class).equalTo("cart_id", cart_id).findFirst().deleteFromRealm();
                                            });
                                            finish();
                                        } else {
                                            total.setText("GHC" + String.format("%.2f", totalamt));
                                            cartItemsArrayList.remove(position);
                                            cartItemAdapter.notifyItemRemoved(position);
                                        }
                                    } else {
                                        Toast.makeText(CartItemsActivity.this, "Error deleting.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            Const.myVolleyError(CartItemsActivity.this, error);
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
            public void onQuantityUpdateClick(ArrayList<RealmCartProduct> realmCartProducts, int position, CartItemAdapter.ViewHolder holder) {
                RealmCartProduct realmCartProduct = realmCartProducts.get(position);
                int quantity = holder.numberPicker.getValue();
                double price = realmCartProduct.getUnit_price() * quantity;
                String cart_product_id = realmCartProduct.getCart_product_id();

                ProgressDialog mProgress = new ProgressDialog(CartItemsActivity.this);
                mProgress.setCancelable(false);
                mProgress.setIndeterminate(true);

                mProgress.setTitle("Updating quantity...");
                mProgress.show();

                StringRequest stringRequest = new StringRequest(
                        Request.Method.PATCH,
                        keyConst.API_URL + "cart-products/" + cart_product_id,
                        response -> {
                            mProgress.dismiss();
                            if (response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    final RealmCartProduct[] cartProduct = new RealmCartProduct[1];
                                    Realm.init(getApplicationContext());
                                    Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).executeTransaction(realm -> {
                                        totalamt -= realmCartProduct.getUnit_price() * realmCartProduct.getQuantity();
                                        cartProduct[0] = realm.createOrUpdateObjectFromJson(RealmCartProduct.class, response);
                                        double updatedprice = cartProduct[0].getUnit_price() * cartProduct[0].getQuantity();
                                        //                                        Toast.makeText(getApplicationContext(), String.valueOf(updatedprice), Toast.LENGTH_SHORT).show();
                                        holder.price.setText("GHC" + String.format("%.2f", updatedprice));
                                        CartItemsActivity.this.totalamt += updatedprice;

                                    });
                                    Toast.makeText(CartItemsActivity.this, "Quantity successfully updated.", Toast.LENGTH_SHORT).show();
                                    total.setText("GHC" + String.format("%.2f", totalamt));
                                    cartItemAdapter.notifyItemChanged(position, cartProduct[0]);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            mProgress.dismiss();
                            error.printStackTrace();
                            Const.myVolleyError(CartItemsActivity.this, error);
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
        }, CartItemsActivity.this, cartItemsArrayList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerview.setAdapter(cartItemAdapter);

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
            RealmResults<RealmCartProduct> results;
            results = realm.where(RealmCartProduct.class).findAll();
            newCartItems.clear();
            for (RealmCartProduct realmCartProduct : results) {
                totalamt += realmCartProduct.getUnit_price() * realmCartProduct.getQuantity();
                newCartItems.add(realmCartProduct);
            }
            cartItemsArrayList.clear();
            cartItemsArrayList.addAll(newCartItems);
            cartItemAdapter.notifyDataSetChanged();
        });
    }
}
