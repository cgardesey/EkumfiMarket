package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.R;
import com.ekumfi.juice.adapter.SellerIndexAdapter;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmChat;
import com.ekumfi.juice.realm.RealmSeller;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class SellerIndexActivity extends AppCompatActivity {
    NetworkReceiver networkReceiver;
    static RecyclerView recyclerview;
    static TextView no_data;
    static SellerIndexAdapter sellerIndexAdapter;
    static ArrayList<RealmSeller> cartArrayList = new ArrayList<>();
    static ArrayList<RealmSeller> newCart = new ArrayList<>();
    public static Activity sellerIndexActivity;

    public SellerIndexActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sellerIndexActivity = this;
        setContentView(R.layout.activity_seller_index);
        recyclerview = findViewById(R.id.recyclerview);
        no_data = findViewById(R.id.no_data);

        sellerIndexAdapter = new SellerIndexAdapter(new SellerIndexAdapter.SellerIndexAdapterInterface() {

            @Override
            public void onItemClick(ArrayList<RealmSeller> realmSellers, int position, SellerIndexAdapter.ViewHolder holder) {
                RealmSeller seller = realmSellers.get(position);
                ProgressDialog dialog = new ProgressDialog(sellerIndexActivity);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();
                final String[] seller_name = new String[1];
                final String[] profile_image_url = new String[1];
                final String[] availability = new String[1];
                String seller_id = seller.getSeller_id();

                Realm.init(sellerIndexActivity);
                final RealmSeller[] realmSeller = {seller};
                seller_name[0] = realmSeller[0].getShop_name();
                profile_image_url[0] = realmSeller[0].getShop_image_url();
                availability[0] = realmSeller[0].getAvailability();

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        API_URL + "ekumfi-chat-data",
                        response -> {
                            if (response != null) {
                                dialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Realm.init(sellerIndexActivity);
                                    Realm.getInstance(RealmUtility.getDefaultConfig(sellerIndexActivity)).executeTransaction(realm -> {
                                        try {
                                            realmSeller[0] = realm.createOrUpdateObjectFromJson(RealmSeller.class, jsonObject.getJSONObject("seller"));
                                            realm.createOrUpdateAllFromJson(RealmSeller.class, jsonObject.getJSONArray("chats"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        seller_name[0] = realmSeller[0].getShop_name();
                                        profile_image_url[0] = realmSeller[0].getShop_image_url();
                                        availability[0] = realmSeller[0].getAvailability();
                                    });

                                    startActivity(new Intent(sellerIndexActivity, MessageActivity.class)
                                            .putExtra("SELLER_ID", seller_id)
                                            .putExtra("SELLER_NAME", seller_name[0])
                                            .putExtra("PROFILE_IMAGE_URL", profile_image_url[0])
                                            .putExtra("AVAILABILITY", availability[0])
                                    );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            startActivity(new Intent(sellerIndexActivity, MessageActivity.class)
                                    .putExtra("SELLER_ID", seller_id)
                                    .putExtra("SELLER_NAME", seller_name[0])
                                    .putExtra("PROFILE_IMAGE_URL", profile_image_url[0])
                                    .putExtra("AVAILABILITY", availability[0])
                            );
                            dialog.dismiss();
                            Log.d("Cyrilll", error.toString());
                        }
                ) {
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("seller_id", seller_id);
                        params.put("consumer_id", "");
                        Realm.init(sellerIndexActivity);
                        Realm.getInstance(RealmUtility.getDefaultConfig(sellerIndexActivity)).executeTransaction(realm -> {
                            RealmResults<RealmChat> results = realm.where(RealmChat.class)
                                    .sort("id", Sort.DESCENDING)
                                    .equalTo("seller_id", seller_id)
                                    .equalTo("consumer_id", "")
                                    .findAll();
                            ArrayList<RealmChat> myArrayList = new ArrayList<>();
                            for (RealmChat realmChat : results) {
                                if (realmChat != null && !(realmChat.getChat_id().startsWith("z"))) {
                                    myArrayList.add(realmChat);
                                }
                            }
                            if (results.size() < 3) {
                                params.put("id", "0");
                            }
                            else{
                                params.put("id", String.valueOf(myArrayList.get(0).getId()));
                            }
                        });
                        return params;
                    }
                    /** Passing some request headers* */
                    @Override
                    public Map getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("accept", "application/json");
                        headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(sellerIndexActivity).getString("com.ekumfi.juice" + APITOKEN, ""));
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
            public void onImageClick(ArrayList<RealmSeller> realmSellers, int position, SellerIndexAdapter.ViewHolder holder) {

            }
        }, sellerIndexActivity, cartArrayList);
        recyclerview.setLayoutManager(new LinearLayoutManager(sellerIndexActivity));
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(sellerIndexAdapter);

        networkReceiver = new NetworkReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();

        activeActivity = this;
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        populateChatIndex(sellerIndexActivity);

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                API_URL + "sellers",
                response -> {
                    if (response != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            Realm.init(sellerIndexActivity);
                            Realm.getInstance(RealmUtility.getDefaultConfig(sellerIndexActivity)).executeTransaction(realm -> {
                                realm.createOrUpdateAllFromJson(RealmSeller.class, jsonArray);
                            });
                            populateChatIndex(sellerIndexActivity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.d("Cyrilll", error.toString());
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("seller_id", PreferenceManager.getDefaultSharedPreferences(sellerIndexActivity).getString("com.ekumfi.juice" + "SELLER_ID", ""));
                return params;
            }

             /*Passing some request headers*/
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("accept", "application/json");
                headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(sellerIndexActivity).getString("com.ekumfi.juice" + APITOKEN, ""));
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
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    public static void populateChatIndex(final Context context) {
        Realm.init(context);
        Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
            RealmResults<RealmSeller> results;

            results = realm.where(RealmSeller.class)
                    .sort("id", Sort.DESCENDING)
                    .distinct("seller_id")
                    .findAll();

            if (results.size() < 1) {
                no_data.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.GONE);
            }
            else {
                no_data.setVisibility(View.GONE);
                recyclerview.setVisibility(View.VISIBLE);
            }
            newCart.clear();
            for (RealmSeller realmSeller : results) {
                newCart.add(realmSeller);
            }
            cartArrayList.clear();
            cartArrayList.addAll(newCart);
            sellerIndexAdapter.notifyDataSetChanged();
        });
    }
}
