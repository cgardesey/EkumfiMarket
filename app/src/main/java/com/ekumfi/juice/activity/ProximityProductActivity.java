package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.myVolleyError;
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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmCart;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.greysonparrelli.permiso.PermisoActivity;
import com.ekumfi.juice.R;
import com.ekumfi.juice.adapter.ProductListAdapter;
import com.ekumfi.juice.realm.RealmProduct;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class ProximityProductActivity extends PermisoActivity {

    NetworkReceiver networkReceiver;
    RecyclerView recyclerView;
    ImageView backbtn, cartIcon;
    ArrayList<String> newList = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    ProductListAdapter productListAdapter;
    String product_category_id;
    String title = "";
    Context mContext;

    ArrayList<RealmProduct> realmProductArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity_products);

        mContext = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerView);
        backbtn = findViewById(R.id.backbtn);
        cartIcon = findViewById(R.id.cartIcon);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = new ProgressDialog(ProximityProductActivity.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();

                Realm.init(ProximityProductActivity.this);
                
                StringRequest stringRequest = new StringRequest(
                        com.android.volley.Request.Method.POST,
                        API_URL + "scoped-consumer-carts",
                        response -> {
                            if (response != null) {
                                dialog.dismiss();
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    if (jsonArray.length() > 0) {
                                        Realm.init(getApplicationContext());
                                        Realm.getInstance(RealmUtility.getDefaultConfig(ProximityProductActivity.this)).executeTransaction(realm -> {
                                            realm.where(RealmCart.class).findAll().deleteAllFromRealm();
                                            realm.createOrUpdateAllFromJson(RealmCart.class, jsonArray);
                                        });

                                        startActivity(new Intent(getApplicationContext(), CartListActivity.class));
                                    } else {
                                        Toast.makeText(getApplicationContext(), "No cart items available.", Toast.LENGTH_SHORT).show();
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
                        params.put("consumer_id", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "CONSUMER_ID", ""));
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

        backbtn.setOnClickListener(v -> finish());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        productListAdapter = new ProductListAdapter((realmProducts, position, holder) -> {
            RealmProduct realmProduct = realmProducts.get(position);

            setResult(Activity.RESULT_OK, new Intent()
                    .putExtra("NAME", realmProduct.getName())
                    .putExtra("PRODUCT_ID", realmProduct.getProduct_id())
            );
            finish();

        }, ProximityProductActivity.this, realmProductArrayList, "");
        recyclerView.setAdapter(productListAdapter);
        populateProducts();

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

    private void populateProducts() {
        Realm.init(getApplicationContext());
        Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmProduct> realmProductCategories = realm.where(RealmProduct.class).findAll();
                realmProductArrayList.clear();
                for (RealmProduct realmProviderCategory : realmProductCategories) {
                    realmProductArrayList.add(realmProviderCategory);
                }
                if (realmProductArrayList.size() > 0) {
                    productListAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
