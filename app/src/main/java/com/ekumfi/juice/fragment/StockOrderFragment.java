package com.ekumfi.juice.fragment;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.myVolleyError;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.R;
import com.ekumfi.juice.activity.OrderSummaryActivity;
import com.ekumfi.juice.activity.StockCartItemsActivity;
import com.ekumfi.juice.adapter.StockCartListAdapter;
import com.ekumfi.juice.materialDialog.ChooseServiceContactMethodMaterialDialog;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmStockCart;
import com.ekumfi.juice.realm.RealmStockCartProduct;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class StockOrderFragment extends Fragment {
    RecyclerView recyclerview;
    TextView no_data;
    StockCartListAdapter stockCartListAdapter;
    ArrayList<RealmStockCart> cartArrayList = new ArrayList<>(), newCart = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_stock_order, container, false);

        recyclerview = rootView.findViewById(R.id.recyclerview);
        no_data = rootView.findViewById(R.id.no_data);

        stockCartListAdapter = new StockCartListAdapter(new StockCartListAdapter.StockCartAdapterInterface() {
            @Override
            public void onViewClick(ArrayList<RealmStockCart> realmStockCarts, int position, StockCartListAdapter.ViewHolder holder) {
                RealmStockCart realmStockCart = realmStockCarts.get(position);
                String stock_cart_id = realmStockCart.getStock_cart_id();
                ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();

                StringRequest stringRequest = new StringRequest(
                        com.android.volley.Request.Method.POST,
                        API_URL + "scoped-stock-cart-products",
                        response -> {
                            if (response != null) {
                                dialog.dismiss();
                                try {
                                    final float[] sub_total = {0.00F};
                                    JSONArray jsonArray = new JSONArray(response);
                                    Realm.init(getActivity());
                                    Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                        realm.where(RealmStockCartProduct.class).findAll().deleteAllFromRealm();
                                        realm.createOrUpdateAllFromJson(RealmStockCartProduct.class, jsonArray);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            try {
                                                sub_total[0] += (float)jsonArray.getJSONObject(i).getDouble("price");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    startActivity(
                                            new Intent(getActivity(), StockCartItemsActivity.class)
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
                            myVolleyError(getActivity(), error);
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
                        headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + APITOKEN, ""));
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
                    chooseServiceContactMethodMaterialDialog.setConsumer_id("");
                    chooseServiceContactMethodMaterialDialog.setSeller_id(realmStockCart.getSeller_id());
                    chooseServiceContactMethodMaterialDialog.setOrder_id(realmStockCart.getOrder_id());
                    chooseServiceContactMethodMaterialDialog.show(getChildFragmentManager(), "chooseContactMethodMaterialDialog");
                    chooseServiceContactMethodMaterialDialog.setCancelable(true);
                }
            }

            @Override
            public void onOrderClick(ArrayList<RealmStockCart> realmStockCarts, int position, StockCartListAdapter.ViewHolder holder) {
                RealmStockCart realmStockCart = realmStockCarts.get(position);

                String stock_cart_id = realmStockCart.getStock_cart_id();
                ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();

                StringRequest stringRequest = new StringRequest(
                        com.android.volley.Request.Method.POST,
                        API_URL + "stock-cart-total",
                        response -> {
                            if (response != null) {
                                dialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.has("updated_stock_cart")) {
                                        Realm.init(getActivity());
                                        Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                            try {
                                                realm.createOrUpdateAllFromJson(RealmStockCart.class, jsonObject.getJSONArray("updated_cart"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        });
                                        populateStockCart(getActivity());
                                        Toast.makeText(getActivity(), "Items no longer in cart", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        startActivity(new Intent(getActivity(), OrderSummaryActivity.class)
                                                .putExtra("ITEM_COUNT", realmStockCart.getItem_count())
                                                .putExtra("SUB_TOTAL", (float)jsonObject.getJSONArray("stock_cart_total").getJSONObject(0).getDouble("total_amount"))
                                                .putExtra("SHIPPING_FEE", 20.00F)
                                                .putExtra("STOCK_CART_ID", realmStockCart.getStock_cart_id()));
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            myVolleyError(getActivity(), error);
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
                        headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + APITOKEN, ""));
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

            }
        }, getActivity(), cartArrayList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(stockCartListAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateStockCart(getActivity());
    }

    void populateStockCart(final Context context) {
        Realm.init(context);
        Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
            RealmResults<RealmStockCart> results = null;
            if (getArguments().getString("status").equals("Received")) {
                results = realm.where(RealmStockCart.class)
                        .equalTo("delivered", 1)
                        .equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "SELLER_ID", ""))
                        .findAll();
            } else if (getArguments().getString("status").equals("Unpaid")) {
                results = realm.where(RealmStockCart.class)
                        .notEqualTo("status", "SUCCESS")
                        .equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "SELLER_ID", ""))
                        .findAll();
            } else {
                results = realm.where(RealmStockCart.class)
                        .equalTo("status", "SUCCESS")
                        .equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "SELLER_ID", ""))
                        .findAll();
            }
            newCart.clear();
            if (results.size() > 0) {
                no_data.setVisibility(View.GONE);
                recyclerview.setVisibility(View.VISIBLE);
            } else {
                no_data.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.GONE);
            }
            for (RealmStockCart realmStockCart : results) {
                newCart.add(realmStockCart);
            }
            cartArrayList.clear();
            cartArrayList.addAll(newCart);
            stockCartListAdapter.notifyDataSetChanged();
        });
    }
}
