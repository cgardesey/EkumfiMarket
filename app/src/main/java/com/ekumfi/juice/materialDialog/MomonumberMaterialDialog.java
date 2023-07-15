package com.ekumfi.juice.materialDialog;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.R;
import com.ekumfi.juice.constants.keyConst;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmPayment;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

public class MomonumberMaterialDialog extends DialogFragment {
    public static ProgressDialog dialog1;
    String type, amount, cart_id, stock_cart_id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getStock_cart_id() {
        return stock_cart_id;
    }

    public void setStock_cart_id(String stock_cart_id) {
        this.stock_cart_id = stock_cart_id;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.list_item_momonumber,null);
        TextView ok = view.findViewById(R.id.ok);
        TextView number = view.findViewById(R.id.number);

        String role = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "ROLE", "");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String momonumber = number.getText().toString();
                if (!TextUtils.isEmpty(momonumber) && !Const.isValidMtnno(momonumber)){
                    Toast.makeText(getActivity(), getString(R.string.invalid_number), Toast.LENGTH_LONG).show();
                }
                else {
                    ProgressDialog mProgress = new ProgressDialog(getActivity());
                    mProgress.setCancelable(false);
                    mProgress.setIndeterminate(true);

                    mProgress.setTitle("Please wait...");
                    mProgress.show();

                    String end_point;
                    if (role.equals("CONSUMER")) {
                        end_point = "consumer-pay";
                    } else {
                        end_point = "seller-pay";
                    }
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            keyConst.API_URL + end_point,
                            response -> {
                                mProgress.dismiss();
                                if (response != null) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        if (jsonObject.has("internal_error")) {
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("Error.")
//                                            .setMessage(getWaitTimeMsg(response.getInt("wait_time")))
                                                    .setMessage("Error occurred. \n\nPlease try again later.")
                                                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                                        getActivity().finish();
                                                    })
                                                    .setCancelable(false)
                                                    .show();
                                        }
                                        else if (jsonObject.has("wait_time")) {
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle(getString(R.string.pending_payment))
//                                            .setMessage(getWaitTimeMsg(response.getInt("wait_time")))
                                                    .setMessage(getString(R.string.try_again_later))
                                                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                                        getActivity().finish();
                                                    })
                                                    .setCancelable(false)
                                                    .show();
                                        }
                                        else if (jsonObject.has("payments")) {
                                            Realm.init(getActivity());
                                            Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                                try {
                                                    realm.createOrUpdateAllFromJson(RealmPayment.class, jsonObject.getJSONArray("payments"));
                                                    getActivity().finish();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            });
                                        }
                                        else {
                                            new AlertDialog.Builder(getActivity())
                                                    .setTitle("Error.")
//                                            .setMessage(getWaitTimeMsg(response.getInt("wait_time")))
                                                    .setMessage("Error occurred. \n\nPlease try again later.")
                                                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                                        getActivity().finish();
                                                    })
                                                    .setCancelable(false)
                                                    .show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            error -> {
                                mProgress.dismiss();
                                error.printStackTrace();
                                Const.myVolleyError(getActivity(), error);
                                Log.d("Cyrilll", error.toString());
                            }
                    ) {
                        @Override
                        public Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("msisdn", "233" + momonumber.substring(1));
                            params.put("country_code", "GH");
                            params.put("network", "MTNGHANA");
                            params.put("currency", "GHS");
                            params.put("amount", amount);
                            if (role.equals("CONSUMER")) {
                                params.put("cart_id", cart_id);
                                params.put("consumer_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("CONSUMER_ID", ""));
                            } else {
                                params.put("stock_cart_id", stock_cart_id);
                                params.put("seller_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "SELLER_ID", ""));
                            }
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
}