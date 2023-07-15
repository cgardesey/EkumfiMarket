package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.myVolleyError;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.ekumfi.juice.R;
import com.ekumfi.juice.adapter.ProximityAgentProductAdapter;
import com.ekumfi.juice.materialDialog.ChooseAgentQuantityMaterialDialog;
import com.ekumfi.juice.materialDialog.ChooseServiceContactMethodMaterialDialog;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmCartProduct;
import com.ekumfi.juice.realm.RealmAgentProduct;
import com.ekumfi.juice.realm.RealmStockCart;
import com.ekumfi.juice.util.PixelUtil;
import com.ekumfi.juice.util.RealmUtility;
import com.google.android.material.snackbar.Snackbar;
import com.greysonparrelli.permiso.Permiso;
import com.greysonparrelli.permiso.PermisoActivity;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;


public class ProximityAgentProductsActivity extends PermisoActivity implements ChooseAgentQuantityMaterialDialog.ChooseAgentQuantityMDInterface {

    RecyclerView recyclerview;
    ProximityAgentProductAdapter proximityAgentProductAdapter;
    private ImageView cartIcon, backbtn;
    ArrayList<RealmAgentProduct> realmAgentProducts = new ArrayList<>(), newRealmAgentProducts = new ArrayList<>();
    public static Activity productActivity;
    TextView title;
    RelativeLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_proximity_agent_products);

        recyclerview = findViewById(R.id.recyclerview);
        productActivity = this;
        backbtn = findViewById(R.id.backbtn1);
        title = findViewById(R.id.title);
        cartIcon = findViewById(R.id.cartIcon);
        parent = findViewById(R.id.parent);

        title.setText(getIntent().getStringExtra("TITLE"));

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewStockCart(ProximityAgentProductsActivity.this, null);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        proximityAgentProductAdapter = new ProximityAgentProductAdapter(new ProximityAgentProductAdapter.ContactMethodAdapterInterface() {
            @Override
            public void onListItemClick(ArrayList<RealmAgentProduct> realmAgentProducts, int position, ProximityAgentProductAdapter.ViewHolder holder) {
                RealmAgentProduct realmAgentProduct = realmAgentProducts.get(position);
                ChooseServiceContactMethodMaterialDialog chooseServiceContactMethodMaterialDialog = new ChooseServiceContactMethodMaterialDialog();
                if (chooseServiceContactMethodMaterialDialog != null && chooseServiceContactMethodMaterialDialog.isAdded()) {

                } else {
                    chooseServiceContactMethodMaterialDialog.setAgent_id(realmAgentProduct.getAgent_id());
                    chooseServiceContactMethodMaterialDialog.show(getSupportFragmentManager(), "chooseContactMethodMaterialDialog");
                    chooseServiceContactMethodMaterialDialog.setCancelable(true);
                }
            }
        },
                new ProximityAgentProductAdapter.AddToCartAdapterInterface() {
                    @Override
                    public void onListItemClick(ArrayList<RealmAgentProduct> names, int position, ProximityAgentProductAdapter.ViewHolder holder) {
                        RealmAgentProduct realmAgentProduct = realmAgentProducts.get(position);
                        if (realmAgentProduct.getQuantity_available() <= realmAgentProduct.getUnit_quantity()) {
                            Toast.makeText(productActivity, "This item is out of stock.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ChooseAgentQuantityMaterialDialog chooseAgentQuantityMaterialDialog = new ChooseAgentQuantityMaterialDialog();
                            if (chooseAgentQuantityMaterialDialog != null && chooseAgentQuantityMaterialDialog.isAdded()) {

                            } else {
                                chooseAgentQuantityMaterialDialog.setAgent_id(realmAgentProduct.getAgent_id());
                                chooseAgentQuantityMaterialDialog.setAgent_product_id(realmAgentProduct.getAgent_product_id());
                                chooseAgentQuantityMaterialDialog.setQuantity_available(realmAgentProduct.getQuantity_available());
                                chooseAgentQuantityMaterialDialog.setUnit_quantity(realmAgentProduct.getUnit_quantity());
                                chooseAgentQuantityMaterialDialog.setUnit_price(realmAgentProduct.getUnit_price());
                                chooseAgentQuantityMaterialDialog.setImage_url(realmAgentProduct.getProduct_image_url());
                                chooseAgentQuantityMaterialDialog.setCancelable(false);
                                chooseAgentQuantityMaterialDialog.show(getSupportFragmentManager(), "chooseAgentQuantityMaterialDialog");
                                chooseAgentQuantityMaterialDialog.setCancelable(true);
                            }
                        }
                    }
                },
                new ProximityAgentProductAdapter.LocationClickInterface() {
                    @Override
                    public void onListItemClick(ArrayList<RealmAgentProduct> names, int position, ProximityAgentProductAdapter.ViewHolder holder) {
                        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                                                                     @Override
                                                                     public void onPermissionResult(Permiso.ResultSet resultSet) {
                                                                         if (resultSet.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && resultSet.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                                                             RealmAgentProduct realmAgentProduct = names.get(position);
                                                                             Uri gmmIntentUri = Uri.parse("geo:" + realmAgentProduct.getLatitude() + "," + realmAgentProduct.getLongitude());
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
                },productActivity, realmAgentProducts);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
//        recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerview.setAdapter(proximityAgentProductAdapter);

        populateProducts(getApplicationContext());
        proximityAgentProductAdapter.notifyDataSetChanged();
    }

    public static void order(Context context, JSONObject jsonObject) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        dialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                API_URL + "scoped-cart-products",
                response -> {
                    if (response != null) {
                        dialog.dismiss();
                        try {
                            final float[] sub_total = {0.00F};
                            JSONArray jsonArray = new JSONArray(response);
                            Realm.init(context);
                            Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
                                realm.where(RealmCartProduct.class).findAll().deleteAllFromRealm();
                                realm.createOrUpdateAllFromJson(RealmCartProduct.class, jsonArray);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        sub_total[0] += (float) jsonArray.getJSONObject(i).getDouble("price");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            context.startActivity(new Intent(context, OrderSummaryActivity.class)
                                    .putExtra("ITEM_COUNT", jsonArray.length())
                                    .putExtra("SUB_TOTAL", sub_total[0])
                                    .putExtra("SHIPPING_FEE", 20.00F)
                                    .putExtra("CART_ID", jsonObject.getString("cart_id")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                    myVolleyError(context, error);
                    dialog.dismiss();
                    Log.d("Cyrilll", error.toString());
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                try {
                    params.put("cart_id", jsonObject.getString("cart_id"));
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
                headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + APITOKEN, ""));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        InitApplication.getInstance().addToRequestQueue(stringRequest);
    }

    public static void viewStockCart(Context context, JSONObject jsonObject) {
        ProgressDialog dialog = new ProgressDialog(context);
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
                                Realm.init(context);
                                Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
                                    realm.where(RealmStockCart.class).findAll().deleteAllFromRealm();
                                    realm.createOrUpdateAllFromJson(RealmStockCart.class, jsonArray);
                                });

                                context.startActivity(new Intent(context, StockCartListActivity.class));
                            } else {
                                Toast.makeText(context, "No cart items available.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                    myVolleyError(context, error);
                    dialog.dismiss();
                    Log.d("Cyrilll", error.toString());
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("seller_id", PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + "SELLER_ID", ""));
                return params;
            }
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("accept", "application/json");
                headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + APITOKEN, ""));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        InitApplication.getInstance().addToRequestQueue(stringRequest);
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

    void populateProducts(final Context context) {
        newRealmAgentProducts.clear();

        Realm.init(context);
        Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
            String stringExtra;
            String fieldName;
            RealmResults<RealmAgentProduct> realmAgentProducts = realm.where(RealmAgentProduct.class)
                    .findAll();


            for (RealmAgentProduct realmAgentProduct : realmAgentProducts) {
                newRealmAgentProducts.add(realmAgentProduct);
            }

            this.realmAgentProducts.clear();
            this.realmAgentProducts.addAll(newRealmAgentProducts);

            proximityAgentProductAdapter.notifyDataSetChanged();
            recyclerview.setVisibility(View.VISIBLE);
        });
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
            viewStockCart(ProximityAgentProductsActivity.this, jsonObject);
        });

        final TextView textViewTwo = snackView.findViewById(R.id.second_text_view);

        textViewTwo.setText("Order");
        textViewTwo.setOnClickListener(v -> {
            order(ProximityAgentProductsActivity.this, jsonObject);
        });

        // Add our courseListMaterialDialog view to the Snackbar's layout
        layout.addView(snackView, objLayoutParams);

        // Show the Snackbar
        snackbar.show();
    }
}
