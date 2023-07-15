package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.myVolleyError;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.greysonparrelli.permiso.Permiso;
import com.greysonparrelli.permiso.PermisoActivity;
import com.ekumfi.juice.R;
import com.ekumfi.juice.adapter.ProximityProductAdapter;
import com.ekumfi.juice.materialDialog.ChooseQuantityMaterialDialog;
import com.ekumfi.juice.materialDialog.ChooseServiceContactMethodMaterialDialog;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmCart;
import com.ekumfi.juice.realm.RealmSellerProduct;
import com.ekumfi.juice.util.PixelUtil;
import com.ekumfi.juice.util.RealmUtility;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;


public class SearchProductsActivity extends PermisoActivity {

    NetworkReceiver networkReceiver;
    RecyclerView recyclerview;
    ProximityProductAdapter proximityProductAdapter;
    private EditText searchtext;
    ArrayList<RealmSellerProduct> realmSellerProducts = new ArrayList<>(), newRealmSellerProducts = new ArrayList<>();
    public static Activity searchProductActivity;
    TextView title;
    ImageView loadinggif, search, cartIcon;
    private String tag = "SEARCH_TAG";
    static int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_products);

        recyclerview = findViewById(R.id.recyclerview);
        searchProductActivity = this;
        searchtext = findViewById(R.id.searchtext);

        loadinggif = findViewById(R.id.loadinggif);
        searchtext = findViewById(R.id.searchtext);
        search = findViewById(R.id.search);
        cartIcon = findViewById(R.id.cartIcon);

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = new ProgressDialog(SearchProductsActivity.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();

                Realm.init(SearchProductsActivity.this);
                

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
                                        Realm.getInstance(RealmUtility.getDefaultConfig(SearchProductsActivity.this)).executeTransaction(realm -> {
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

        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                realmSellerProducts.clear();
                proximityProductAdapter.notifyDataSetChanged();
                offset = 0;
                if (!searchtext.getText().toString().equals("")) {
                    populateFilteredProducts(searchtext.getText().toString());
                } else {
                    realmSellerProducts.clear();
                    proximityProductAdapter.notifyDataSetChanged();
                }
            }
        });

        proximityProductAdapter = new ProximityProductAdapter(new ProximityProductAdapter.ContactMethodAdapterInterface() {
            @Override
            public void onListItemClick(ArrayList<RealmSellerProduct> realmSellerProducts, int position, ProximityProductAdapter.ViewHolder holder) {
                RealmSellerProduct realmSellerProduct = realmSellerProducts.get(position);
                ChooseServiceContactMethodMaterialDialog chooseServiceContactMethodMaterialDialog = new ChooseServiceContactMethodMaterialDialog();
                if (chooseServiceContactMethodMaterialDialog != null && chooseServiceContactMethodMaterialDialog.isAdded()) {

                } else {
                    chooseServiceContactMethodMaterialDialog.setConsumer_id(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "CONSUMER_ID", ""));
                    chooseServiceContactMethodMaterialDialog.setSeller_id(realmSellerProduct.getSeller_id());
                    chooseServiceContactMethodMaterialDialog.show(getSupportFragmentManager(), "chooseContactMethodMaterialDialog");
                    chooseServiceContactMethodMaterialDialog.setCancelable(true);
                }
            }
        },
                new ProximityProductAdapter.AddToCartAdapterInterface() {
                    @Override
                    public void onListItemClick(ArrayList<RealmSellerProduct> names, int position, ProximityProductAdapter.ViewHolder holder) {
                        RealmSellerProduct realmSellerProduct = realmSellerProducts.get(position);
                        if (realmSellerProduct.getQuantity_available() <= realmSellerProduct.getUnit_quantity()) {
                            Toast.makeText(getApplicationContext(), "This item is out of stock.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ChooseQuantityMaterialDialog chooseQuantityMaterialDialog = new ChooseQuantityMaterialDialog();
                            if (chooseQuantityMaterialDialog != null && chooseQuantityMaterialDialog.isAdded()) {

                            } else {
                                chooseQuantityMaterialDialog.setSeller_id(realmSellerProduct.getSeller_id());
                                chooseQuantityMaterialDialog.setSeller_product_id(realmSellerProduct.getSeller_product_id());
                                chooseQuantityMaterialDialog.setQuantity_available(realmSellerProduct.getQuantity_available());
                                chooseQuantityMaterialDialog.setUnit_quantity(realmSellerProduct.getUnit_quantity());
                                chooseQuantityMaterialDialog.setUnit_price(realmSellerProduct.getUnit_price());
                                chooseQuantityMaterialDialog.setImage_url(realmSellerProduct.getProduct_image_url());
                                chooseQuantityMaterialDialog.setCancelable(false);
                                chooseQuantityMaterialDialog.show(getSupportFragmentManager(), "chooseQuantityMaterialDialog");
                                chooseQuantityMaterialDialog.setCancelable(true);
                            }
                        }
                    }
                },

                new ProximityProductAdapter.LocationClickInterface() {
                    @Override
                    public void onListItemClick(ArrayList<RealmSellerProduct> names, int position, ProximityProductAdapter.ViewHolder holder) {
                        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                                                                     @Override
                                                                     public void onPermissionResult(Permiso.ResultSet resultSet) {
                                                                         if (resultSet.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && resultSet.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                                                             RealmSellerProduct realmSellerProduct = names.get(position);
                                                                             Uri gmmIntentUri = Uri.parse("geo:" + realmSellerProduct.getLatitude() + "," + realmSellerProduct.getLongitude());
                                                                             Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                                                             mapIntent.setPackage("com.google.android.apps.maps");
                                                                             if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                                                                 startActivity(mapIntent);
                                                                             }
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
                },SearchProductsActivity.this, realmSellerProducts);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerview.setAdapter(proximityProductAdapter);

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

    private UCrop.Options imgOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        options.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        options.setCropFrameColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        options.setCropFrameStrokeWidth(PixelUtil.dpToPx(getApplicationContext(), 4));
        options.setCropGridColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        options.setCropGridStrokeWidth(PixelUtil.dpToPx(getApplicationContext(), 2));
        options.setActiveWidgetColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        options.setToolbarTitle(getString(R.string.crop_image));

        // set rounded cropping guide
        options.setCircleDimmedLayer(true);
        return options;
    }

    private void populateFilteredProducts(String search) {
        try {
            loadinggif.setVisibility(View.VISIBLE);
            InitApplication.getInstance().mRequestQueue.cancelAll(tag);
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    API_URL + "filtered-seller-products",
                    response -> {
                        loadinggif.setVisibility(View.GONE);
                        if (response != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                if (jsonArray.length() > 0) {
                                    Realm.init(getApplicationContext());
                                    Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).executeTransaction(realm -> {
                                        realm.where(RealmSellerProduct.class).findAll().deleteAllFromRealm();
                                        realm.createOrUpdateAllFromJson(RealmSellerProduct.class, jsonArray);
                                        RealmResults<RealmSellerProduct> seller_products = realm.where(RealmSellerProduct.class).findAll();
                                        newRealmSellerProducts.clear();
                                        for (RealmSellerProduct realmSellerProduct : seller_products) {
                                            newRealmSellerProducts.add(realmSellerProduct);
                                        }
                                    });
                                    realmSellerProducts.clear();
                                    realmSellerProducts.addAll(newRealmSellerProducts);
                                    proximityProductAdapter.notifyDataSetChanged();
                                } else {
                                    realmSellerProducts.clear();
                                    proximityProductAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    error -> {
                        loadinggif.setVisibility(View.GONE);
                        myVolleyError(getApplicationContext(), error);
                    }
            ) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("search", search);
                    params.put("offset", String.valueOf(offset));
                    params.put("length", "10");

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
            stringRequest.setTag(tag);
            InitApplication.getInstance().addToRequestQueue(stringRequest);

        } catch (Exception e) {
            Log.e("My error", e.toString());
        }
    }
}
