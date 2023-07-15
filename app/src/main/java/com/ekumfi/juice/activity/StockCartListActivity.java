package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.myVolleyError;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.R;
import com.ekumfi.juice.adapter.StockCartListAdapter;
import com.ekumfi.juice.materialDialog.ChooseServiceContactMethodMaterialDialog;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmStockCart;
import com.ekumfi.juice.realm.RealmStockCartProduct;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.ekumfi.juice.util.RealmUtility;
import com.greysonparrelli.permiso.Permiso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class StockCartListActivity extends AppCompatActivity {
    NetworkReceiver networkReceiver;
    RecyclerView recyclerview;
    TextView no_data;
    ImageView refresh;
    StockCartListAdapter cartListAdapter;
    ArrayList<RealmStockCart> cartArrayList = new ArrayList<>(), newCart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_cart_list);
        recyclerview = findViewById(R.id.recyclerview);
        no_data = findViewById(R.id.no_data);
        refresh = findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = new ProgressDialog(StockCartListActivity.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        API_URL + "scoped-stock-carts",
                        response -> {
                            if (response != null) {
                                dialog.dismiss();
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    if (jsonArray.length() > 0) {
                                        Realm.init(getApplicationContext());
                                        Realm.getInstance(RealmUtility.getDefaultConfig(StockCartListActivity.this)).executeTransaction(realm -> {
                                            realm.where(RealmStockCart.class).findAll().deleteAllFromRealm();
                                            realm.createOrUpdateAllFromJson(RealmStockCart.class, jsonArray);
                                        });
                                        populateCart(getApplicationContext());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            myVolleyError(getApplicationContext(), error);
                            dialog.dismiss();
                            Log.d("Cyrilll", error.toString());
                        }
                ) {
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("seller_id", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "SELLER_ID", ""));
                        return params;
                    }

                    /** Passing some request headers* */
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
        });

        cartListAdapter = new StockCartListAdapter(new StockCartListAdapter.StockCartAdapterInterface() {
            @Override
            public void onViewClick(ArrayList<RealmStockCart> realmStockCarts, int position, StockCartListAdapter.ViewHolder holder) {
                RealmStockCart realmStockCart = realmStockCarts.get(position);
                String stock_cart_id = realmStockCart.getStock_cart_id();
                ProgressDialog dialog = new ProgressDialog(StockCartListActivity.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        API_URL + "scoped-stock-cart-products",
                        response -> {
                            if (response != null) {
                                dialog.dismiss();
                                try {
                                    final float[] sub_total = {0.00F};
                                    JSONArray jsonArray = new JSONArray(response);
                                    Realm.init(getApplicationContext());
                                    Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).executeTransaction(realm -> {
                                        realm.where(RealmStockCartProduct.class).findAll().deleteAllFromRealm();
                                        realm.createOrUpdateAllFromJson(RealmStockCartProduct.class, jsonArray);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            try {
                                                sub_total[0] += (float) jsonArray.getJSONObject(i).getDouble("price");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    startActivity(
                                            new Intent(getApplicationContext(), StockCartItemsActivity.class)
                                                    .putExtra("IS_INVOICE", realmStockCart.getStatus() != null && realmStockCart.getStatus().equals("SUCCESS"))
                                                    .putExtra("INVOICE_SUB_TOTAL", sub_total[0])
                                                    .putExtra("SHIPPING_FEE", (float)realmStockCart.getShipping_fee())
                                                    .putExtra("STOCK_CART_ID", realmStockCart.getStock_cart_id())
                                    );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            myVolleyError(getApplicationContext(), error);
                            dialog.dismiss();
                            Log.d("Cyrilll", error.toString());
                        }
                ) {
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("stock_cart_id", stock_cart_id);
                        return params;
                    }

                    /** Passing some request headers* */
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
            public void onContactClick(ArrayList<RealmStockCart> realmStockCarts, int position, StockCartListAdapter.ViewHolder holder) {
                RealmStockCart realmStockCart = realmStockCarts.get(position);
                ChooseServiceContactMethodMaterialDialog chooseServiceContactMethodMaterialDialog = new ChooseServiceContactMethodMaterialDialog();
                if (chooseServiceContactMethodMaterialDialog != null && chooseServiceContactMethodMaterialDialog.isAdded()) {

                } else {
                    chooseServiceContactMethodMaterialDialog.setAgent_id(realmStockCart.getAgent_id());
                    chooseServiceContactMethodMaterialDialog.setSeller_id(realmStockCart.getSeller_id());
                    chooseServiceContactMethodMaterialDialog.setOrder_id(realmStockCart.getOrder_id());
                    chooseServiceContactMethodMaterialDialog.show(getSupportFragmentManager(), "chooseContactMethodMaterialDialog");
                    chooseServiceContactMethodMaterialDialog.setCancelable(true);
                }
            }

            @Override
            public void onOrderClick(ArrayList<RealmStockCart> realmStockCarts, int position, StockCartListAdapter.ViewHolder holder) {
                RealmStockCart realmStockCart = realmStockCarts.get(position);

                String cart_id = realmStockCart.getStock_cart_id();
                ProgressDialog dialog = new ProgressDialog(StockCartListActivity.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        API_URL + "stock-cart-total",
                        response -> {
                            if (response != null) {
                                dialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.has("updated_cart")) {
                                        Realm.init(getApplicationContext());
                                        Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).executeTransaction(realm -> {
                                            try {
                                                realm.createOrUpdateAllFromJson(RealmStockCart.class, jsonObject.getJSONArray("updated_cart"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        });
                                        populateCart(getApplicationContext());
                                        Toast.makeText(getApplicationContext(), "Items no longer in cart", Toast.LENGTH_LONG).show();
                                    } else {
                                        startActivity(new Intent(getApplicationContext(), OrderSummaryActivity.class)
                                                .putExtra("ITEM_COUNT", realmStockCart.getItem_count())
                                                .putExtra("SUB_TOTAL", (float) jsonObject.getJSONArray("cart_total").getJSONObject(0).getDouble("total_amount"))
                                                .putExtra("SHIPPING_FEE", 20.00F)
                                                .putExtra("CART_ID", realmStockCart.getStock_cart_id()));
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            myVolleyError(getApplicationContext(), error);
                            dialog.dismiss();
                            Log.d("Cyrilll", error.toString());
                        }
                ) {
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("cart_id", cart_id);
                        return params;
                    }

                    /** Passing some request headers* */
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
            public void onDeliveryClick(ArrayList<RealmStockCart> realmStockCarts, int position, StockCartListAdapter.ViewHolder holder) {
                Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                                                             @Override
                                                             public void onPermissionResult(Permiso.ResultSet resultSet) {
                                                                 if (resultSet.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && resultSet.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                                                     RealmStockCart realmStockCart = realmStockCarts.get(position);
                                                                     Uri location = Uri.parse("geo:" + realmStockCart.getSeller_latitude() + "," + realmStockCart.getSeller_longitude() + "?z=14"); // z param is zoom level
                                                                     Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                                                                     startActivity(mapIntent);
                                                                 }
                                                             }

                                                             @Override
                                                             public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                                                                 Permiso.getInstance().showRationaleInDialog(getApplicationContext().getString(R.string.permissions), getApplicationContext().getString(R.string.this_permission_is_mandatory_pls_allow_access), null, callback);
                                                             }
                                                         },
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }, this, cartArrayList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(cartListAdapter);

        networkReceiver = new NetworkReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activeActivity = this;
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        populateCart(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    void populateCart(final Context context) {
        Realm.init(context);
        Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
            RealmResults<RealmStockCart> results;
            results = realm.where(RealmStockCart.class)
                    .notEqualTo("status", "SUCCESS")
                    .findAll();
            newCart.clear();
            if (results.size() > 0) {
                no_data.setVisibility(View.GONE);
                recyclerview.setVisibility(View.VISIBLE);
            }
            else {
                no_data.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.GONE);
            }
            for (RealmStockCart realmStockCart : results) {
                newCart.add(realmStockCart);
            }
            cartArrayList.clear();
            cartArrayList.addAll(newCart);
            cartListAdapter.notifyDataSetChanged();
        });
    }
}
