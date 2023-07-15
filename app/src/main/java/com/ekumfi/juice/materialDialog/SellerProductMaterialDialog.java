package com.ekumfi.juice.materialDialog;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.activity.ProductActivity;
import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.constants.keyConst;
import com.ekumfi.juice.fragment.SellerProductsFragment;
import com.ekumfi.juice.R;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmProduct;
import com.ekumfi.juice.realm.RealmSellerProduct;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

public class SellerProductMaterialDialog extends DialogFragment {
    public static String seller_product_id, product_id, name, description, quantity_available, unit_price;


    EditText unit_price_edittext, quantity_available_edittext;
    public static TextView name_textview;
    Button ok;

    public String getSeller_product_id() {
        return seller_product_id;
    }

    public void setSeller_product_id(String seller_product_id) {
        this.seller_product_id = seller_product_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity_available() {
        return quantity_available;
    }

    public void setQuantity_available(String quantity_available) {
        this.quantity_available = quantity_available;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.list_item_seller_product, null);
        name_textview = view.findViewById(R.id.name);
        quantity_available_edittext = view.findViewById(R.id.quantity_available);
        unit_price_edittext = view.findViewById(R.id.unit_price);
        ok = view.findViewById(R.id.ok);

        name_textview.setText(name);
        unit_price_edittext.setText(unit_price);
        quantity_available_edittext.setText(quantity_available);
        

        name_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();
                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        keyConst.API_URL + "products",
                        response -> {
                            if (response != null) {
                                dialog.dismiss();
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    Realm.init(getActivity());
                                    Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            RealmResults<RealmProduct> realmProducts = realm.where(RealmProduct.class).findAll();
                                            realmProducts.deleteAllFromRealm();

                                            realm.createOrUpdateAllFromJson(RealmProduct.class, jsonArray);
                                        }
                                    });
                                    getActivity().startActivityForResult(new Intent(getContext(), ProductActivity.class), 1815);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            dialog.dismiss();
                            Const.myVolleyError(getActivity(), error);
                        }
                ) {
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
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();
                    String url = keyConst.API_URL + "seller-products";
                    int methodType = Request.Method.POST;
                    if (seller_product_id != null && !seller_product_id.equals("")) {
                        url += "/" + seller_product_id;
                        methodType = Request.Method.PATCH;
                    }

                    int finalMethodType = methodType;
                    StringRequest stringRequest = new StringRequest(
                            methodType,
                            url,
                            response -> {
                                progressDialog.dismiss();
                                if (response != null) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.has("already_exists")) {
                                            Toast.makeText(getActivity(), "Product already exists!", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            final RealmSellerProduct[] realmSellerProduct = new RealmSellerProduct[1];
                                            Realm.init(getActivity());
                                            Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm realm) {
                                                    try {
                                                        realmSellerProduct[0] = realm.createOrUpdateObjectFromJson(RealmSellerProduct.class, jsonObject.getJSONArray("seller_products").getJSONObject(0));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    name_textview.setText(null);
                                                    unit_price_edittext.setText(null);
                                                    quantity_available_edittext.setText(null);

                                                    String sellerid = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "SELLER_ID", "");
                                                    RealmResults<RealmSellerProduct> sellerProducts = realm.where(RealmSellerProduct.class).equalTo("seller_id", sellerid).findAll();
                                                    SellerProductsFragment.productArrayList.clear();
                                                    for (RealmSellerProduct product : sellerProducts) {
                                                        SellerProductsFragment.productArrayList.add(product);
                                                    }

                                                    SellerProductsFragment.recyclerView.setLayoutManager(SellerProductsFragment.layoutManager);
                                                    SellerProductsFragment.recyclerView.setHasFixedSize(true);
                                                    SellerProductsFragment.recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                    SellerProductsFragment.recyclerView.setAdapter(SellerProductsFragment.productAdapter);
//
                                                    if (seller_product_id != null && !seller_product_id.equals("")) {
                                                        Toast.makeText(getActivity(), "Successfully saved!", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        Toast.makeText(getActivity(), "Product successfully added!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                        dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            error -> {
                                error.printStackTrace();
                                Log.d("Cyrilll", error.toString());
                                progressDialog.dismiss();
                                Const.myVolleyError(getActivity(), error);
                            }
                    ) {
                        @Override
                        public Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("product_id", product_id);
                            params.put("seller_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "SELLER_ID", ""));
                            params.put("quantity_available", quantity_available_edittext.getText().toString());
                            params.put("unit_price", unit_price_edittext.getText().toString());
                            return params;
                        }

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
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    }
                });
            }
        }, 5);
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    public boolean validate() {
        boolean validated = true;
        if (TextUtils.isEmpty(unit_price_edittext.getText())) {
            unit_price_edittext.setError(getString(R.string.error_field_required));
            validated = false;
        }
        if (TextUtils.isEmpty(quantity_available_edittext.getText())) {
            quantity_available_edittext.setError(getString(R.string.error_field_required));
            validated = false;
        }
        return validated;
    }
}