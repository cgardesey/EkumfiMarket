package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.myVolleyError;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.flyco.tablayout.SlidingTabLayout;
import com.ekumfi.juice.R;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.pagerAdapter.ConsumerOrdersPagerAdapter;
import com.ekumfi.juice.realm.RealmCart;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class ConsumerOrdersActivity extends AppCompatActivity {

    ViewPager mViewPager;
    SlidingTabLayout mTabLayout;
    ArrayList<String> statuses;
    ImageView backbtn, refresh;
    ProgressDialog dialog;
    NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_orders);

        networkReceiver = new NetworkReceiver();

        backbtn = findViewById(R.id.search);
        refresh = findViewById(R.id.refresh);

        backbtn.setOnClickListener(v -> finish());

        statuses = new ArrayList<String>() {{
            add("Unpaid");
            add("Paid");
            add("Delivered");
        }};

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog = new ProgressDialog(ConsumerOrdersActivity.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();

                StringRequest stringRequest = new StringRequest(
                        com.android.volley.Request.Method.POST,
                        API_URL + "scoped-consumer-carts",
                        response -> {
                            if (response != null) {
                                dialog.dismiss();
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    Realm.init(ConsumerOrdersActivity.this);
                                    Realm.getInstance(RealmUtility.getDefaultConfig(ConsumerOrdersActivity.this)).executeTransaction(realm -> {
                                        realm.where(RealmCart.class).findAll().deleteAllFromRealm();
                                        realm.createOrUpdateAllFromJson(RealmCart.class, jsonArray);
                                    });
                                    mViewPager.setAdapter(new ConsumerOrdersPagerAdapter(getSupportFragmentManager(), statuses));
                                    mTabLayout.setViewPager(mViewPager);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            myVolleyError(ConsumerOrdersActivity.this, error);
                            dialog.dismiss();
                            Log.d("Cyrilll", error.toString());
                        }
                ) {
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("consumer_id", PreferenceManager.getDefaultSharedPreferences(ConsumerOrdersActivity.this).getString("com.ekumfi.juice" + "CONSUMER_ID", ""));
                        return params;
                    }
                    /** Passing some request headers* */
                    @Override
                    public Map getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("accept", "application/json");
                        headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(ConsumerOrdersActivity.this).getString("com.ekumfi.juice" + APITOKEN, ""));
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
        
        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager.setAdapter(new ConsumerOrdersPagerAdapter(getSupportFragmentManager(), statuses));
        mTabLayout.setViewPager(mViewPager);
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
}
