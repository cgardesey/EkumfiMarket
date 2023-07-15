package com.ekumfi.juice.materialDialog;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.activity.GetAuthActivity.MYUSERID;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.ekumfi.juice.activity.ConsumerHomeActivity;
import com.ekumfi.juice.constants.keyConst;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmBanner;
import com.ekumfi.juice.realm.RealmConsumer;
import com.ekumfi.juice.realm.RealmUser;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

public class ConsumerNameMaterialDialog extends DialogFragment {
    public static ProgressDialog dialog1;
    TextView name;
    EditText contact;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.list_item_consumer_name, null);

        name = view.findViewById(R.id.provider_name);
        contact = view.findViewById(R.id.primarycontact);
        Button ok = view.findViewById(R.id.ok);

        Realm.init(getActivity());
        String phone_number = Realm.getInstance(RealmUtility.getDefaultConfig(getContext())).where(RealmUser.class)
                .equalTo("user_id", PreferenceManager.getDefaultSharedPreferences(getContext()).getString("com.ekumfi.juice" + MYUSERID,  ""))
                .findFirst()
                .getPhone_number();
        contact.setText("0" + phone_number.substring(3));
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String consumername = name.getText().toString();
                String primarycontact = contact.getText().toString();
                if (validate()) {
                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage(getActivity().getString(R.string.please_wait));
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            keyConst.API_URL + "consumers",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressDialog.dismiss();

                                    if (response != null) {
                                        JSONObject jsonObject;
                                        try {
                                            jsonObject = new JSONObject(response);
                                            Realm.init(getActivity());
                                            Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                                try {
                                                    RealmConsumer realmConsumer = realm.createOrUpdateObjectFromJson(RealmConsumer.class, jsonObject.getJSONObject("consumer"));
                                                    realm.createOrUpdateAllFromJson(RealmBanner.class, jsonObject.getJSONArray("banners"));

                                                    PreferenceManager
                                                            .getDefaultSharedPreferences(getActivity())
                                                            .edit()
                                                            .putString("com.ekumfi.juice" + "ROLE", "CONSUMER")
                                                            .putString("com.ekumfi.juice" + "CONSUMER_ID", realmConsumer.getConsumer_id())
                                                            .apply();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            });
                                            startActivity(new Intent(getActivity(), ConsumerHomeActivity.class));
                                            getActivity().finish();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
                                    Const.myVolleyError(getActivity(), error);
                                }
                            }
                    )
                    {
                        @Override
                        public Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();
                            params.put("name", consumername.trim());
                            params.put("primary_contact", primarycontact);
                            params.put("user_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + MYUSERID,  ""));
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
                } else {
                    Toast.makeText(getActivity(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
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

    public boolean validate() {
        boolean validated = true;

        if (TextUtils.isEmpty(name.getText())) {
            name.setError(getString(R.string.error_field_required));
            validated = false;
        }
        String phonenumber = contact.getText().toString();
        if (!(phonenumber.length() == 10 && phonenumber.charAt(0) == '0')) {
            contact.setError("Invalid number");
            validated = false;
        }
        return validated;
    }
}