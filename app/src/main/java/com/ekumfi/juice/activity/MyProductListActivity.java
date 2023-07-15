package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.activity.GetAuthActivity.MYUSERID;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.myVolleyError;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.greysonparrelli.permiso.PermisoActivity;
import com.ekumfi.juice.R;
import com.ekumfi.juice.adapter.ProductListAdapter;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmProductCategory;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class MyProductListActivity extends PermisoActivity {

    NetworkReceiver networkReceiver;
    RecyclerView recyclerView;
    ImageView loadinggif;
    ImageView backbtn;
    Button retrybtn;
    LinearLayout retry_layout;
    TextView titleTextView, text;
    ArrayList<String> newList = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    ProductListAdapter listAdapter;
    String title = "";
    String tag = "";
    String url = "";
    Context mContext;

    ArrayList<RealmProductCategory> realmProductCategoryArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        mContext = getApplicationContext();
        loadinggif = findViewById(R.id.loadinggif);
        retry_layout = findViewById(R.id.retry_layout);
        retrybtn = findViewById(R.id.retrybtn);
        Glide.with(getApplicationContext()).asGif().load(R.drawable.spinner).apply(new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.spinner)
                .error(R.drawable.error)).into(loadinggif);
        text = findViewById(R.id.text);
        recyclerView = findViewById(R.id.recyclerView);
        titleTextView = findViewById(R.id.title);
        backbtn = findViewById(R.id.search);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        title = getIntent().getStringExtra("title");
        tag = getIntent().getStringExtra("tag");
        url = getIntent().getStringExtra("url");
        titleTextView.setText(title);

        populateNames(title.concat(" ").concat(">>").concat(" "));
        backbtn.setOnClickListener(v -> finish());
        retrybtn.setOnClickListener(v -> populateNames(title.concat(" ").concat(">>").concat(" ")));

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
    private void populateNames(String search) {

        try {
            loadinggif.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    API_URL + "sub-product-categories",
                    response -> {
                        loadinggif.setVisibility(View.GONE);
                        retry_layout.setVisibility(View.GONE);
                        if (response != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                int length = jsonArray.length();
                                if (length == 0) {
                                    setResult(RESULT_OK, new Intent()
                                            .putExtra("PRODUCT_CATEGORY", title)
                                            .putExtra("url", url)
                                            .putExtra("tag", tag));
                                    finish();
                                }
                                realmProductCategoryArrayList.clear();
                                for (int i = 0; i < length; i++) {

                                    Realm.init(getApplicationContext());
                                    int finalI = i;
                                    Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            RealmProductCategory realmProductCategory = null;
                                            try {
                                                realmProductCategory = realm.createOrUpdateObjectFromJson(RealmProductCategory.class, jsonArray.getJSONObject(finalI));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            realmProductCategoryArrayList.add(realmProductCategory);
                                        }
                                    });
                                }
//                                listAdapter = new ProductListAdapter((realmProductCategories, position, holder) -> {
//                                    RealmProductCategory realmProductCategory = realmProductCategories.get(position);
//                                    String textViewText = realmProductCategory.getTitle();
//                                    startActivityForResult(new Intent(getApplicationContext(), MyProductListActivity.class)
//                                                    .putExtra("title", realmProductCategory.getTitle())
//                                                    .putExtra("tag", realmProductCategory.getTag())
//                                                    .putExtra("url", realmProductCategory.getUrl())
//                                                    .putExtra("initiator", getIntent().getStringExtra("initiator")),
//                                            1915
//                                    );
//
//                                }, this, realmProductCategoryArrayList, "");
//                                recyclerView.setAdapter(listAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    error -> {
                        loadinggif.setVisibility(View.GONE);
                        retry_layout.setVisibility(View.VISIBLE);
                        myVolleyError(mContext, error);
                    }
            ) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("userid", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + MYUSERID, ""));
                    params.put("search", search);
                    return params;
                }
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("accept", "application/json");
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(mContext).getString("com.ekumfi.juice" + APITOKEN, ""));
                    return headers;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            InitApplication.getInstance().addToRequestQueue(stringRequest);

        } catch (Exception e) {
            Log.e("My error", e.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1915:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null) {
                            setResult(RESULT_OK, new Intent()
                                    .putExtra("PRODUCT_CATEGORY", data.getStringExtra("PRODUCT_CATEGORY"))
                                    .putExtra("tag", data.getStringExtra("tag"))
                                    .putExtra("url", data.getStringExtra("url"))
                            );
                            finish();
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // some stuff that will happen if there's no result
                        break;
                }
                break;
            default:
                break;
        }
    }
}
