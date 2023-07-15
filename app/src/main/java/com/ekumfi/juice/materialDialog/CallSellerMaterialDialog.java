package com.ekumfi.juice.materialDialog;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.R;
import com.ekumfi.juice.constants.keyConst;
import com.ekumfi.juice.other.InitApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CallSellerMaterialDialog extends DialogFragment {
    String phone_number, consumer_id, seller_id, agent_id;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getConsumer_id() {
        return consumer_id;
    }

    public void setConsumer_id(String consumer_id) {
        this.consumer_id = consumer_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.list_item_phonenumber,null);
        TextView ok = view.findViewById(R.id.ok);
        TextView number = view.findViewById(R.id.number);
        number.setText(phone_number);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileno = number.getText().toString();
                if (!TextUtils.isEmpty(mobileno) && !Const.isValidMtnno(mobileno)){
                    Toast.makeText(getActivity(), getString(R.string.invalid_number), Toast.LENGTH_LONG).show();
                }
                else {
                    ProgressDialog mProgress = new ProgressDialog(getActivity());
                    mProgress.setTitle("Processing...");
                    mProgress.setMessage("Please wait...");
                    mProgress.setCancelable(false);
                    mProgress.setIndeterminate(true);
                    mProgress.show();
                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            keyConst.API_URL + "group-call" ,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    mProgress.dismiss();
                                    if (response != null) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            dismiss();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    mProgress.dismiss();
                                    Const.myVolleyError(getActivity(), error);
                                }
                            }
                    )
                    {
                        /** Passing some request headers* */
                        @Override
                        public Map getHeaders() throws AuthFailureError {
                            HashMap headers = new HashMap();
                            headers.put("accept", "application/json");
                            headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + APITOKEN, ""));
                            return headers;
                        }

                        @Override
                        public Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<>();

                            String role = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "ROLE", "");
                            if (role.equals("CONSUMER")) {
                                params.put("seller_id", seller_id);
                            }
                            else {
                                if (agent_id != null && !agent_id.equals("")) {
                                    params.put("agent_id", agent_id);
                                } else {
                                    params.put("consumer_id", consumer_id);
                                }
                            }
                            params.put("phone_number", mobileno);
                            return params;
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
        // doneBtn.setOnClickListener(doneAction);
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