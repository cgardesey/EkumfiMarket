package com.ekumfi.juice.materialDialog;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.R;
import com.ekumfi.juice.constants.keyConst;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmConsumer;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import it.sephiroth.android.library.numberpicker.NumberPicker;

public class ChooseQuantityMaterialDialog extends DialogFragment {

    private String product_id;
    private String seller_id;
    private String seller_product_id;
    private String image_url;
    private int quantity_available;
    private int unit_quantity;
    private double unit_price;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_product_id() {
        return seller_product_id;
    }

    public void setSeller_product_id(String seller_product_id) {
        this.seller_product_id = seller_product_id;
    }

    public int getQuantity_available() {
        return quantity_available;
    }

    public void setQuantity_available(int quantity_available) {
        this.quantity_available = quantity_available;
    }

    public int getUnit_quantity() {
        return unit_quantity;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public void setUnit_quantity(int unit_quantity) {
        this.unit_quantity = unit_quantity;
    }

    NumberPicker numberPicker;
    TextView price, currency, cancel, ok;
    public Layout layout;


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.list_item_choose_quantity, null);
        cancel = view.findViewById(R.id.cancel);
        ok = view.findViewById(R.id.ok);
        price = view.findViewById(R.id.price);
        currency = view.findViewById(R.id.currency);
        numberPicker = view.findViewById(R.id.numberPicker);
        ImageView product_image = view.findViewById(R.id.product_image);
        Glide.with(getActivity()).load(image_url).apply(new RequestOptions()
                .centerCrop()
                .placeholder(null)
                .error(null))
                .into(product_image);

        numberPicker.setProgress(unit_quantity);
        price.setText(String.format("%.2f", unit_quantity * unit_price));
        numberPicker.setMinValue(unit_quantity);
        numberPicker.setMaxValue(quantity_available);
        numberPicker.setStepSize(unit_quantity);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "ROLE", "");
                if (role.equals("CONSUMER")) {
                    Realm.init(getActivity());
                    RealmConsumer realmConsumer = Realm.getInstance(RealmUtility.getDefaultConfig(getContext())).where(RealmConsumer.class).equalTo("consumer_id", PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.ekumfi.juice" + "CONSUMER_ID", "")).findFirst();
                    String consumer_id = realmConsumer.getConsumer_id();

                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage(getActivity().getString(R.string.please_wait));
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            keyConst.API_URL + "carts",
                            response -> {
                                progressDialog.dismiss();
                                if (response != null) {
                                    progressDialog.dismiss();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        ChooseQuantityMDInterface activity = (ChooseQuantityMDInterface) getActivity();
                                        if (jsonObject.getBoolean("success")) {
                                            activity.onViewClick("Successfully added to cart!", jsonObject.getJSONObject("cart"));
                                        } else {
                                            activity.onViewClick("Item already in cart", jsonObject.getJSONObject("cart"));
                                        }
                                        dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            error -> {
                                error.printStackTrace();
                                progressDialog.dismiss();
                                Const.myVolleyError(getActivity(), error);
                            }
                    ) {
                        @Override
                        public Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("seller_id", seller_id);
                            params.put("consumer_id", consumer_id);
                            params.put("seller_product_id", seller_product_id);
                            params.put("quantity", String.valueOf(numberPicker.getProgress()));
                            params.put("price", price.getText().toString());
                            return params;
                        }

                        /** Passing some request headers* */
                        @Override
                        public Map getHeaders() throws AuthFailureError {
                            HashMap headers = new HashMap();
                            headers.put("accept", "application/json");
                            headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.ekumfi.juice" + APITOKEN, ""));
                            return headers;
                        }
                    };
                    ;
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    InitApplication.getInstance().addToRequestQueue(stringRequest);
                } else if (role.equals("SELLER")) {
                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage(getActivity().getString(R.string.please_wait));
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            keyConst.API_URL + "stock-carts",
                            response -> {
                                progressDialog.dismiss();
                                if (response != null) {
                                    progressDialog.dismiss();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        ChooseQuantityMDInterface fragment = (ChooseQuantityMDInterface) getParentFragment();

                                        if (jsonObject.getBoolean("success")) {
                                            fragment.onStockCartViewClick("Successfully added to cart!", jsonObject.getJSONObject("stock_cart"));
                                        } else {
                                            fragment.onStockCartViewClick("Item already in cart", jsonObject.getJSONObject("stock_cart"));
                                        }
                                        dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            error -> {
                                error.printStackTrace();
                                progressDialog.dismiss();
                                Const.myVolleyError(getActivity(), error);
                            }
                    ) {
                        @Override
                        public Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("seller_id", seller_id);
                            params.put("product_id", product_id);
                            params.put("quantity", String.valueOf(numberPicker.getProgress()));
                            params.put("price", price.getText().toString());
                            return params;
                        }

                        /** Passing some request headers* */
                        @Override
                        public Map getHeaders() throws AuthFailureError {
                            HashMap headers = new HashMap();
                            headers.put("accept", "application/json");
                            headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.ekumfi.juice" + APITOKEN, ""));
                            return headers;
                        }
                    };
                    ;
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    InitApplication.getInstance().addToRequestQueue(stringRequest);
                }
            }
        });
        numberPicker.setNumberPickerChangeListener(new NumberPicker.OnNumberPickerChangeListener() {
            @Override
            public void onProgressChanged(@NonNull NumberPicker numberPicker, int i, boolean b) {
                price.setText(String.format("%.2f", i * unit_price));
            }

            @Override
            public void onStartTrackingTouch(@NonNull NumberPicker numberPicker) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull NumberPicker numberPicker) {

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

    public interface ChooseQuantityMDInterface {
        public void onViewClick(String message, JSONObject jsonObject);
        public void onStockCartViewClick(String message, JSONObject jsonObject);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }
}