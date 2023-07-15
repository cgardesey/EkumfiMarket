package com.ekumfi.juice.materialDialog;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.activity.MessageActivity;
import com.ekumfi.juice.R;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmAgent;
import com.ekumfi.juice.realm.RealmCart;
import com.ekumfi.juice.realm.RealmChat;
import com.ekumfi.juice.realm.RealmConsumer;
import com.ekumfi.juice.realm.RealmSeller;
import com.ekumfi.juice.realm.RealmStockCart;
import com.ekumfi.juice.util.RealmUtility;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ChooseServiceContactMethodMaterialDialog extends DialogFragment {
    public static ProgressDialog dialog1;
    private static final String MY_LOGIN_ID = "MY_LOGIN_ID";
    LinearLayout chat, call;

    String agent_id = "";
    String seller_id = "";
    String consumer_id = "";
    String order_id;

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

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.list_item_choose_contact_method, null);
        chat = view.findViewById(R.id.chat);
        call = view.findViewById(R.id.call);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // view.setVisibility(View.GONE);

                ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();

                String role = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "ROLE", "");
                if (role.equals("SELLER")) {
                    if (agent_id != null && !agent_id.equals("")) {
                        final String[] agent_name = new String[1];
                        final String[] profile_image_url = new String[1];

                        Realm.init(getActivity());
                        final RealmAgent[] realmAgent = new RealmAgent[1];
                        final RealmStockCart[] realmStockCart = {Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).where(RealmStockCart.class).findFirst()};
                        agent_name[0] = StringUtils.normalizeSpace((realmStockCart[0].getTitle() + " " + realmStockCart[0].getFirst_name() + " " + realmStockCart[0].getOther_name() + " " + realmStockCart[0].getLast_name()).replace("null", ""));
                        profile_image_url[0] = realmStockCart[0].getProfile_image_url();

                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                API_URL + "seller-chat-data-with-agent",
                                response -> {
                                    if (response != null) {
                                        dialog.dismiss();
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Realm.init(getActivity());
                                            Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                                try {
                                                    realmAgent[0] = realm.createOrUpdateObjectFromJson(RealmAgent.class, jsonObject.getJSONObject("agent"));
                                                    realm.createOrUpdateAllFromJson(RealmChat.class, jsonObject.getJSONArray("chats"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                agent_name[0] = StringUtils.normalizeSpace((realmStockCart[0].getTitle() + " " + realmStockCart[0].getFirst_name() + " " + realmStockCart[0].getOther_name() + " " + realmStockCart[0].getLast_name()).replace("null", ""));
                                                profile_image_url[0] = realmStockCart[0].getProfile_image_url();
                                            });

                                            startActivity(new Intent(getActivity(), MessageActivity.class)
                                                    .putExtra("AGENT_ID", agent_id)
                                                    .putExtra("AGENT_NAME", agent_name[0])
                                                    .putExtra("PROFILE_IMAGE_URL", profile_image_url[0])
                                            );
                                            dismiss();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                error -> {
                                    error.printStackTrace();
                                    startActivity(new Intent(getActivity(), MessageActivity.class)
                                            .putExtra("AGENT_ID", agent_id)
                                            .putExtra("AGENT_NAME", agent_name[0])
                                            .putExtra("PROFILE_IMAGE_URL", profile_image_url[0])
                                    );
                                    dialog.dismiss();
                                    Log.d("Cyrilll", error.toString());
                                }
                        ) {
                            @Override
                            public Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("agent_id", agent_id);
                                params.put("seller_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "SELLER_ID", ""));
                                Realm.init(getActivity());
                                Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                    RealmResults<RealmChat> results = realm.where(RealmChat.class)
                                            .sort("id", Sort.DESCENDING)
                                            .equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "SELLER_ID", ""))
                                            .equalTo("agent_id", agent_id)
                                            .findAll();
                                    ArrayList<RealmChat> myArrayList = new ArrayList<>();
                                    for (RealmChat realmChat : results) {
                                        if (!(realmChat.getChat_id().startsWith("z"))) {
                                            myArrayList.add(realmChat);
                                        }
                                    }
                                    if (results.size() < 3) {
                                        params.put("id", "0");
                                    }
                                    else{
                                        params.put("id", String.valueOf(myArrayList.get(0).getId()));
                                    }
                                });
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
                        final String[] consumer_name = new String[1];
                        final String[] profile_image_url = new String[1];

                        Realm.init(getActivity());
                        RealmCart realmCart = Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).where(RealmCart.class).equalTo("consumer_id", consumer_id).findFirst();
                        consumer_name[0] = realmCart.getConsumer_name();
                        profile_image_url[0] = realmCart.getConsumer_profile_image_url();

                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                API_URL + "seller-chat-data-with-consumer",
                                response -> {
                                    if (response != null) {
                                        dialog.dismiss();
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Realm.init(getActivity());
                                            Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                                try {
                                                    RealmConsumer consumer = realm.createOrUpdateObjectFromJson(RealmConsumer.class, jsonObject.getJSONObject("consumer"));
                                                    realm.createOrUpdateAllFromJson(RealmChat.class, jsonObject.getJSONArray("chats"));

                                                    consumer_name[0] = consumer.getName();
                                                    profile_image_url[0] = consumer.getProfile_image_url();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            });

                                            startActivity(new Intent(getActivity(), MessageActivity.class)
                                                    .putExtra("CONSUMER_ID", consumer_id)
                                                    .putExtra("CONSUMER_NAME", consumer_name[0])
                                                    .putExtra("PROFILE_IMAGE_URL", profile_image_url[0])
                                            );
                                            dismiss();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                error -> {
                                    error.printStackTrace();
                                    startActivity(new Intent(getActivity(), MessageActivity.class)
                                            .putExtra("CONSUMER_ID", consumer_id)
                                            .putExtra("CONSUMER_NAME", consumer_name[0])
                                            .putExtra("PROFILE_IMAGE_URL", profile_image_url[0])
                                    );
                                    dialog.dismiss();
                                    Log.d("Cyrilll", error.toString());
                                }
                        ) {
                            @Override
                            public Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("consumer_id", consumer_id);
                                params.put("seller_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "SELLER_ID", ""));
                                Realm.init(getActivity());
                                Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                    RealmResults<RealmChat> results = realm.where(RealmChat.class)
                                            .sort("id", Sort.DESCENDING)
                                            .equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "SELLER_ID", ""))
                                            .equalTo("consumer_id", consumer_id)
                                            .findAll();
                                    ArrayList<RealmChat> myArrayList = new ArrayList<>();
                                    for (RealmChat realmChat : results) {
                                        if (!(realmChat.getChat_id().startsWith("z"))) {
                                            myArrayList.add(realmChat);
                                        }
                                    }
                                    if (results.size() < 3) {
                                        params.put("id", "0");
                                    }
                                    else{
                                        params.put("id", String.valueOf(myArrayList.get(0).getId()));
                                    }
                                });
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
                    }
                } else {
                    final String[] seller_name = new String[1];
                    final String[] profile_image_url = new String[1];
                    final String[] availability = new String[1];

                    Realm.init(getActivity());
                    RealmCart realmCart = Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).where(RealmCart.class).equalTo("seller_id", seller_id).findFirst();
                    seller_name[0] = realmCart.getShop_name();
                    profile_image_url[0] = realmCart.getShop_image_url();
                    availability[0] = realmCart.getAvailability();

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            API_URL + "consumer-chat-data",
                            response -> {
                                if (response != null) {
                                    dialog.dismiss();
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Realm.init(getActivity());
                                        Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                            try {
                                                RealmSeller seller = realm.createOrUpdateObjectFromJson(RealmSeller.class, jsonObject.getJSONObject("seller"));
                                                realm.createOrUpdateAllFromJson(RealmChat.class, jsonObject.getJSONArray("chats"));

                                                seller_name[0] = seller.getShop_name();
                                                profile_image_url[0] = seller.getShop_image_url();
                                                availability[0] = seller.getAvailability();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        });

                                        startActivity(new Intent(getActivity(), MessageActivity.class)
                                                .putExtra("SELLER_ID", seller_id)
                                                .putExtra("SELLER_NAME", seller_name[0])
                                                .putExtra("PROFILE_IMAGE_URL", profile_image_url[0])
                                                .putExtra("AVAILABILITY", availability[0])
                                        );
                                        dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            error -> {
                                error.printStackTrace();
                                startActivity(new Intent(getActivity(), MessageActivity.class)
                                        .putExtra("SELLER_ID", seller_id)
                                        .putExtra("SELLER_NAME", seller_name[0])
                                        .putExtra("PROFILE_IMAGE_URL", profile_image_url[0])
                                        .putExtra("AVAILABILITY", availability[0])
                                );
                                dialog.dismiss();
                                Log.d("Cyrilll", error.toString());
                            }
                    ) {
                        @Override
                        public Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("seller_id", seller_id);
                            params.put("consumer_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "CONSUMER_ID", ""));
                            Realm.init(getActivity());
                            Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                RealmResults<RealmChat> results = realm.where(RealmChat.class)
                                        .sort("id", Sort.DESCENDING)
                                        .equalTo("seller_id", seller_id)
                                        .equalTo("consumer_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "CONSUMER_ID", ""))
                                        .findAll();
                                ArrayList<RealmChat> myArrayList = new ArrayList<>();
                                for (RealmChat realmChat : results) {
                                    if (!(realmChat.getChat_id().startsWith("z"))) {
                                        myArrayList.add(realmChat);
                                    }
                                }
                                if (results.size() < 3) {
                                    params.put("id", "0");
                                }
                                else{
                                    params.put("id", String.valueOf(myArrayList.get(0).getId()));
                                }
                            });
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
                }
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallSellerMaterialDialog callProviderMaterialDialog = new CallSellerMaterialDialog();
                if (callProviderMaterialDialog != null && callProviderMaterialDialog.isAdded()) {

                } else {
                    String role = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "ROLE", "");
                    String primary_contact;
                    if (role.equals("CONSUMER")) {
                        Realm.init(getActivity());
                        primary_contact = Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).where(RealmConsumer.class).equalTo("consumer_id", androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "CONSUMER_ID", "")).findFirst().getPrimary_contact();
                    }
                    else {
                        primary_contact = Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).where(RealmSeller.class).equalTo("seller_id", androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "SELLER_ID", "")).findFirst().getPrimary_contact();
                    }
                    callProviderMaterialDialog.setPhone_number(primary_contact);
                    callProviderMaterialDialog.setConsumer_id(consumer_id);
                    callProviderMaterialDialog.setSeller_id(seller_id);
                    callProviderMaterialDialog.setAgent_id(agent_id);
                    callProviderMaterialDialog.show(getFragmentManager(), "");

                    dismiss();
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