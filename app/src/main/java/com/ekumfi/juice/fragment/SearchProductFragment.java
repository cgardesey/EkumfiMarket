package com.ekumfi.juice.fragment;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.clearAppData;
import static com.ekumfi.juice.constants.Const.isTablet;
import static com.ekumfi.juice.constants.Const.myVolleyError;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultRegistry;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.activity.OrderSummaryActivity;
import com.ekumfi.juice.activity.ProximityAgentProductsActivity;
import com.ekumfi.juice.activity.ProximityProductsActivity;
import com.ekumfi.juice.activity.SearchProductsActivity;
import com.ekumfi.juice.activity.StockCartItemsActivity;
import com.ekumfi.juice.materialDialog.ChooseQuantityMaterialDialog;
import com.ekumfi.juice.realm.RealmAgentProduct;
import com.ekumfi.juice.realm.RealmStockCartProduct;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ekumfi.juice.R;
import com.ekumfi.juice.adapter.ProductListAdapter;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmBanner;
import com.ekumfi.juice.realm.RealmConsumer;
import com.ekumfi.juice.realm.RealmProduct;
import com.ekumfi.juice.realm.RealmSellerProduct;
import com.ekumfi.juice.util.RealmUtility;
import com.ekumfi.juice.util.carousel.ViewPagerCarouselView;
import com.google.android.material.snackbar.Snackbar;
import com.greysonparrelli.permiso.Permiso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;


public class SearchProductFragment extends Fragment implements ChooseQuantityMaterialDialog.ChooseQuantityMDInterface{
    ArrayList<RealmBanner> realmBannerArrayList = new ArrayList<>();
    ArrayList<RealmProduct> realmProducts = new ArrayList<>();
    RecyclerView recyclerView;
    private ShimmerFrameLayout shimmer_view_container;

    static ViewPagerCarouselView viewPagerCarouselView;
    public static RelativeLayout searchlayout;
    public static LinearLayout error_loading;
    ProductListAdapter productListAdapter;
    Button retrybtn;
    FrameLayout frame;
    Activity activity;
    Double longitude = 0.0d, latitude = 0.0d;
    CoordinatorLayout parent;

    private FusedLocationProviderClient fusedLocationClient;

    public SearchProductFragment() {

    }

    public SearchProductFragment(@NonNull ActivityResultRegistry registry) {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_search_product, container, false);

        activity = getActivity();

        String role = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "ROLE", "");
        if (role.equals("CONSUMER")) {
            Realm.init(getActivity());
            RealmConsumer realmConsumer = Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).where(RealmConsumer.class).equalTo("consumer_id", androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "CONSUMER_ID", "")).findFirst();
            longitude = realmConsumer.getLongitude();
            latitude = realmConsumer.getLatitude();
            if (realmConsumer == null) {
                clearAppData(getActivity());
            }
        }


        viewPagerCarouselView = rootView.findViewById(R.id.carousel_view);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        parent = rootView.findViewById(R.id.parent);
        frame = rootView.findViewById(R.id.frame);
        searchlayout = rootView.findViewById(R.id.searchlayout);

        searchlayout.setOnClickListener(view -> startActivity(new Intent(getContext(), SearchProductsActivity.class)));
        recyclerView = rootView.findViewById(R.id.recyclerView);
        if (isTablet(getContext())) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        productListAdapter = new ProductListAdapter((realmProducts, position, holder) -> {
            RealmProduct realmProduct = realmProducts.get(position);
            if (role.equals("CONSUMER")) {
                if (longitude == 0.0d && latitude == 0.0d) {
                    if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationClient.getLastLocation()
                                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            launchProximityProducts(realmProduct.getProduct_id(), realmProduct.getName(), location.getLongitude(), location.getLatitude());
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "Error retrieving location.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else {
                        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                                                                     @SuppressLint("MissingPermission")
                                                                     @Override
                                                                     public void onPermissionResult(Permiso.ResultSet resultSet) {
                                                                         if (resultSet.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && resultSet.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {

                                                                             fusedLocationClient.getLastLocation()
                                                                                     .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                                                                         @Override
                                                                                         public void onSuccess(Location location) {
                                                                                             // Got last known location. In some rare situations this can be null.
                                                                                             if (location != null) {
                                                                                                 launchProximityProducts(realmProduct.getProduct_id(), realmProduct.getName(), location.getLongitude(), location.getLatitude());
                                                                                             }
                                                                                             else {
                                                                                                 Toast.makeText(getActivity(), "Error retrieving location.", Toast.LENGTH_SHORT).show();
                                                                                             }
                                                                                         }
                                                                                     });
                                                                         }
                                                                     }

                                                                     @Override
                                                                     public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                                                                         Permiso.getInstance().showRationaleInDialog(getActivity().getString(R.string.permissions), getActivity().getString(R.string.this_permission_is_mandatory_pls_allow_access), null, callback);
                                                                     }
                                                                 },
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                }
                else {
                    launchProximityProducts(realmProduct.getProduct_id(), realmProduct.getName(), longitude, latitude);
                }
            }
            else {
                if (longitude == 0.0d && latitude == 0.0d) {
                    if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationClient.getLastLocation()
                                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            launchProximityAgentProducts(realmProduct.getProduct_id(), realmProduct.getName(), location.getLongitude(), location.getLatitude());
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "Error retrieving location.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                    else {
                        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                                                                     @SuppressLint("MissingPermission")
                                                                     @Override
                                                                     public void onPermissionResult(Permiso.ResultSet resultSet) {
                                                                         if (resultSet.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && resultSet.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {

                                                                             fusedLocationClient.getLastLocation()
                                                                                     .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                                                                         @Override
                                                                                         public void onSuccess(Location location) {
                                                                                             // Got last known location. In some rare situations this can be null.
                                                                                             if (location != null) {
                                                                                                 launchProximityAgentProducts(realmProduct.getProduct_id(), realmProduct.getName(), location.getLongitude(), location.getLatitude());
                                                                                             }
                                                                                             else {
                                                                                                 Toast.makeText(getActivity(), "Error retrieving location.", Toast.LENGTH_SHORT).show();
                                                                                             }
                                                                                         }
                                                                                     });
                                                                         }
                                                                     }

                                                                     @Override
                                                                     public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                                                                         Permiso.getInstance().showRationaleInDialog(getActivity().getString(R.string.permissions), getActivity().getString(R.string.this_permission_is_mandatory_pls_allow_access), null, callback);
                                                                     }
                                                                 },
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION);
                    }
                }
                else {
                    launchProximityProducts(realmProduct.getProduct_id(), realmProduct.getName(), longitude, latitude);
                }
            }

        }, getActivity(), realmProducts, "");

        shimmer_view_container = rootView.findViewById(R.id.shimmer_view_container);
        shimmer_view_container.startShimmerAnimation();
        error_loading = rootView.findViewById(R.id.error_loading);


        recyclerView.setAdapter(productListAdapter);

        frame.setVisibility(View.VISIBLE);
        shimmer_view_container.stopShimmerAnimation();
        shimmer_view_container.setVisibility(View.GONE);

        Realm.init(getContext());
        Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
            RealmResults<RealmBanner> realmBannerRealmResults = realm.where(RealmBanner.class).findAll();
            if (realmBannerRealmResults.size() > 0) {
                realmBannerArrayList.clear();
                for (RealmBanner banner : realmBannerRealmResults) {
                    realmBannerArrayList.add(banner);
                }
                viewPagerCarouselView.setData(getFragmentManager(), realmBannerArrayList, 3500);
                frame.setVisibility(View.VISIBLE);
                //  error_loading.setVisibility(View.GONE);

            } else {
                ///  error_loading.setVisibility(View.VISIBLE);
            }
            RealmResults<RealmProduct> products = realm.where(RealmProduct.class).findAll();
            realmProducts.clear();
            for (RealmProduct realmProduct : products) {
                realmProducts.add(realmProduct);
            }
            if (realmProducts.size() > 0) {
                productListAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getCustomerHomeData();
    }

    private void launchProximityProducts(String product_id, String product_name, double longitude, double lattitude) {
        try {
            ProgressDialog mProgress = new ProgressDialog(getContext());
            mProgress.setMessage("Please wait...");
            mProgress.setCancelable(false);
            mProgress.setIndeterminate(true);
            mProgress.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    API_URL + "proximity-products",
                    response -> {
                        mProgress.dismiss();
                        if (response != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                Realm.init(getContext());
                                Realm.getInstance(RealmUtility.getDefaultConfig(getContext())).executeTransaction(realm -> {
                                    RealmResults<RealmSellerProduct> realmSellerProducts = realm.where(RealmSellerProduct.class).findAll();
                                    realmSellerProducts.deleteAllFromRealm();

                                    realm.createOrUpdateAllFromJson(RealmSellerProduct.class, jsonArray);
                                });
                                if (jsonArray.length() == 0) {
                                    Toast.makeText(getContext(), "No matching sellers available!", Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(getContext(), ProximityProductsActivity.class)
                                            .putExtra("TITLE", product_name)
                                            .putExtra("LONGITUDE", longitude)
                                            .putExtra("LATITUDE", lattitude)
                                    );
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    error -> {
                        mProgress.dismiss();
                        myVolleyError(getContext(), error);
                    }
            ) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("product_id", product_id);
                    return params;
                }
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("accept", "application/json");
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.ekumfi.juice" + APITOKEN, ""));
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

    private void launchProximityAgentProducts(String product_id, String product_name, double longitude, double lattitude) {
        try {
            ProgressDialog mProgress = new ProgressDialog(getContext());
            mProgress.setMessage("Please wait...");
            mProgress.setCancelable(false);
            mProgress.setIndeterminate(true);
            mProgress.show();
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    API_URL + "proximity-agent-products",
                    response -> {
                        mProgress.dismiss();
                        if (response != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                Realm.init(getContext());
                                Realm.getInstance(RealmUtility.getDefaultConfig(getContext())).executeTransaction(realm -> {
                                    RealmResults<RealmAgentProduct> realmAgentProducts = realm.where(RealmAgentProduct.class).findAll();
                                    realmAgentProducts.deleteAllFromRealm();

                                    realm.createOrUpdateAllFromJson(RealmAgentProduct.class, jsonArray);
                                });
                                if (jsonArray.length() == 0) {
                                    Toast.makeText(getContext(), "No matching sellers available!", Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(getContext(), ProximityAgentProductsActivity.class)
                                            .putExtra("TITLE", product_name)
                                            .putExtra("LONGITUDE", longitude)
                                            .putExtra("LATITUDE", lattitude)
                                    );
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    error -> {
                        mProgress.dismiss();
                        myVolleyError(getContext(), error);
                    }
            ) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("product_id", product_id);
                    return params;
                }
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("accept", "application/json");
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.ekumfi.juice" + APITOKEN, ""));
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

    public void getCustomerHomeData() {
        Realm.init(getContext());
        try {
//            shimmer_view_container.startShimmerAnimation();
//            shimmer_view_container.setVisibility(View.VISIBLE);
//            frame.setVisibility(View.GONE);
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    API_URL + "consumer-home-data",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Realm.init(getActivity());
                                    Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                        try {
                                            realm.where(RealmBanner.class).findAll().deleteAllFromRealm();
                                            realm.where(RealmProduct.class).findAll().deleteAllFromRealm();

                                            realm.createOrUpdateAllFromJson(RealmBanner.class, jsonObject.getJSONArray("banners"));
                                            realm.createOrUpdateAllFromJson(RealmProduct.class, jsonObject.getJSONArray("products"));
                                            shimmer_view_container.stopShimmerAnimation();
                                            shimmer_view_container.setVisibility(View.GONE);

                                            RealmResults<RealmBanner> realmBanners = realm.where(RealmBanner.class).findAll();
                                            for (RealmBanner realmBanner : realmBanners) {
                                                realmBannerArrayList.add(realmBanner);
                                            }
                                            if (realmBannerArrayList.size() > 0) {
                                                viewPagerCarouselView.setData(getFragmentManager(), realmBannerArrayList, 3500);
                                                frame.setVisibility(View.VISIBLE);
                                            }

                                            RealmResults<RealmProduct> products = realm.where(RealmProduct.class).findAll();
                                            realmProducts.clear();
                                            for (RealmProduct realmProduct : products) {
                                                realmProducts.add(realmProduct);
                                            }
                                            if (realmProducts.size() > 0) {
                                                productListAdapter.notifyDataSetChanged();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            shimmer_view_container.stopShimmerAnimation();
                            shimmer_view_container.setVisibility(View.GONE);


                            Realm.init(getContext());
                            Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                RealmResults<RealmBanner> realmBannerRealmResults = realm.where(RealmBanner.class).findAll();
                                if (realmBannerRealmResults.size() > 0) {
                                    realmBannerArrayList.clear();
                                    for (RealmBanner banner : realmBannerRealmResults) {
                                        realmBannerArrayList.add(banner);
                                    }
                                    viewPagerCarouselView.setData(getFragmentManager(), realmBannerArrayList, 3500);
                                    frame.setVisibility(View.VISIBLE);
                                    //  error_loading.setVisibility(View.GONE);

                                } else {
                                    ///  error_loading.setVisibility(View.VISIBLE);
                                }
                                RealmResults<RealmProduct> products = realm.where(RealmProduct.class).findAll();
                                realmProducts.clear();
                                for (RealmProduct realmProduct : products) {
                                    realmProducts.add(realmProduct);
                                }
                                if (realmProducts.size() > 0) {
                                    productListAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
            )
            {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("consumer_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "CONSUMER_ID", ""));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewClick(String message, JSONObject jsonObject) {

    }

    @Override
    public void onStockCartViewClick(String message, JSONObject jsonObject) {

        // Create the Snackbar
        LinearLayout.LayoutParams objLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG);

        // Get the Snackbar layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        // Inflate our courseListMaterialDialog viewBitmap bitmap = ((RoundedDrawable)profilePic.getDrawable()).getSourceBitmap();
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);

        TextView messageTextView = snackView.findViewById(R.id.message);
        messageTextView.setText(message);
        TextView textViewOne = snackView.findViewById(R.id.first_text_view);
        textViewOne.setText("View");
        textViewOne.setOnClickListener(v -> {
            ProgressDialog dialog = new ProgressDialog(getContext());
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
                                            sub_total[0] += (float) jsonArray.getJSONObject(i).getDouble("price");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                startActivity(
                                        new Intent(getActivity(), StockCartItemsActivity.class)
                                                .putExtra("IS_INVOICE", jsonObject.getString("status") != null && jsonObject.getString("status").equals("SUCCESS"))
                                                .putExtra("INVOICE_SUB_TOTAL", sub_total[0])
                                                .putExtra("SHIPPING_FEE", (float) jsonObject.getDouble("shipping_fee"))
                                                .putExtra("STOCK_CART_ID", jsonObject.getString("stock_cart_id"))
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
                    try {
                        params.put("stock_cart_id", jsonObject.getString("stock_cart_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        });

        final TextView textViewTwo = snackView.findViewById(R.id.second_text_view);

        textViewTwo.setText("Order");
        textViewTwo.setOnClickListener(v -> {
            ProgressDialog dialog = new ProgressDialog(getContext());
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
                                            sub_total[0] += (float) jsonArray.getJSONObject(i).getDouble("price");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                            /*startActivity(
                                                    new Intent(getActivity(), StockCartItemsActivity.class)
                                                            .putExtra("IS_INVOICE", jsonObject.getString("status") != null && jsonObject.getString("status").equals("SUCCESS"))
                                                            .putExtra("INVOICE_SUB_TOTAL", sub_total[0])
                                                            .putExtra("SHIPPING_FEE", (float) jsonObject.getDouble("shipping_fee"))
                                                            .putExtra("STOCK_CART_ID", jsonObject.getString("stock_cart_id"))
                                            );*/


                                startActivity(new Intent(getActivity(), OrderSummaryActivity.class)
                                        .putExtra("ITEM_COUNT", jsonArray.length())
                                        .putExtra("SUB_TOTAL", sub_total[0])
                                        .putExtra("SHIPPING_FEE", 20.00F)
                                        .putExtra("STOCK_CART_ID", jsonObject.getString("stock_cart_id")));
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
                    try {
                        params.put("stock_cart_id", jsonObject.getString("stock_cart_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
        });

        // Add our courseListMaterialDialog view to the Snackbar's layout
        layout.addView(snackView, objLayoutParams);

        // Show the Snackbar
        snackbar.show();
    }
}
