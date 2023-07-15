package com.ekumfi.juice.fragment;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.activity.MessageActivity;
import com.ekumfi.juice.R;
import com.ekumfi.juice.activity.SellerIndexActivity;
import com.ekumfi.juice.adapter.ChatIndexAdapter;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmAgent;
import com.ekumfi.juice.realm.RealmCart;
import com.ekumfi.juice.realm.RealmChat;
import com.ekumfi.juice.realm.RealmConsumer;
import com.ekumfi.juice.realm.RealmSeller;
import com.ekumfi.juice.util.RealmUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ChatIndexFragment extends Fragment {
    static RecyclerView recyclerview;
    static TextView no_data;
    static ChatIndexAdapter chatIndexAdapter;
    static ArrayList<RealmChat> cartArrayList = new ArrayList<>();
    static ArrayList<RealmChat> newCart = new ArrayList<>();
    public static Activity chatIndexFragmentContext;
    FloatingActionButton fab;
    static Activity activity;

    public ChatIndexFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatIndexFragmentContext = activity;
        final View rootView = inflater.inflate(R.layout.fragment_chat_index, container, false);
        recyclerview = rootView.findViewById(R.id.recyclerview);
        no_data = rootView.findViewById(R.id.no_data);
        fab = rootView.findViewById(R.id.fab);

        chatIndexAdapter = new ChatIndexAdapter(new ChatIndexAdapter.ChatIndexAdapterInterface() {

            @Override
            public void onItemClick(ArrayList<RealmChat> realmChats, int position, ChatIndexAdapter.ViewHolder holder) {
                RealmChat realmChat = realmChats.get(position);
                ProgressDialog dialog = new ProgressDialog(activity);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();


                String role = PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "ROLE", "");
                if (role.equals("SELLER")) {
                    if (!realmChat.getAgent_id().equals("")) {
                        final String[] agent_name = new String[1];
                        final String[] profile_image_url = new String[1];
                        String consumer_id = realmChat.getConsumer_id();

                        Realm.init(activity);
                        final RealmAgent[] realmAgent = {Realm.getInstance(RealmUtility.getDefaultConfig(activity)).where(RealmAgent.class).findFirst()};
                        agent_name[0] = StringUtils.normalizeSpace((realmAgent[0].getTitle() + " " + realmAgent[0].getFirst_name() + " " + realmAgent[0].getOther_names() + " " + realmAgent[0].getLast_name()).replace("null", ""));
                        profile_image_url[0] = realmAgent[0].getProfile_image_url();

                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                API_URL + "seller-chat-data-with-agent",
                                response -> {
                                    if (response != null) {
                                        dialog.dismiss();
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Realm.init(activity);
                                            Realm.getInstance(RealmUtility.getDefaultConfig(activity)).executeTransaction(realm -> {
                                                try {
                                                    realmAgent[0] = realm.createOrUpdateObjectFromJson(RealmAgent.class, jsonObject.getJSONObject("agent"));
                                                    realm.createOrUpdateAllFromJson(RealmChat.class, jsonObject.getJSONArray("chats"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                agent_name[0] = StringUtils.normalizeSpace((realmAgent[0].getTitle() + " " + realmAgent[0].getFirst_name() + " " + realmAgent[0].getOther_names() + " " + realmAgent[0].getLast_name()).replace("null", ""));
                                                profile_image_url[0] = realmAgent[0].getProfile_image_url();
                                            });

                                            startActivity(new Intent(activity, MessageActivity.class)
                                                    .putExtra("CONSUMER_ID", consumer_id)
                                                    .putExtra("AGENT_NAME", agent_name[0])
                                                    .putExtra("PROFILE_IMAGE_URL", profile_image_url[0])
                                            );
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                error -> {
                                    error.printStackTrace();
                                    startActivity(new Intent(activity, MessageActivity.class)
                                            .putExtra("CONSUMER_ID", consumer_id)
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
                                params.put("consumer_id", consumer_id);
                                params.put("seller_id", PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "SELLER_ID", ""));
                                Realm.init(activity);
                                Realm.getInstance(RealmUtility.getDefaultConfig(activity)).executeTransaction(realm -> {
                                    RealmResults<RealmChat> results = realm.where(RealmChat.class)
                                            .sort("id", Sort.DESCENDING)
                                            .equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "SELLER_ID", ""))
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
                                headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + APITOKEN, ""));
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
                        String consumer_id = realmChat.getConsumer_id();

                        Realm.init(activity);
                        RealmCart realmCart = Realm.getInstance(RealmUtility.getDefaultConfig(activity)).where(RealmCart.class).equalTo("consumer_id", consumer_id).findFirst();
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
                                            Realm.init(activity);
                                            Realm.getInstance(RealmUtility.getDefaultConfig(activity)).executeTransaction(realm -> {
                                                try {
                                                    RealmConsumer consumer = realm.createOrUpdateObjectFromJson(RealmConsumer.class, jsonObject.getJSONObject("consumer"));
                                                    realm.createOrUpdateAllFromJson(RealmChat.class, jsonObject.getJSONArray("chats"));

                                                    consumer_name[0] = consumer.getName();
                                                    profile_image_url[0] = consumer.getProfile_image_url();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            });

                                            startActivity(new Intent(activity, MessageActivity.class)
                                                    .putExtra("CONSUMER_ID", consumer_id)
                                                    .putExtra("CONSUMER_NAME", consumer_name[0])
                                                    .putExtra("PROFILE_IMAGE_URL", profile_image_url[0])
                                            );
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                error -> {
                                    error.printStackTrace();
                                    startActivity(new Intent(activity, MessageActivity.class)
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
                                params.put("seller_id", PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "SELLER_ID", ""));
                                Realm.init(activity);
                                Realm.getInstance(RealmUtility.getDefaultConfig(activity)).executeTransaction(realm -> {
                                    RealmResults<RealmChat> results = realm.where(RealmChat.class)
                                            .sort("id", Sort.DESCENDING)
                                            .equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "SELLER_ID", ""))
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
                                headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + APITOKEN, ""));
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
                    String seller_id = realmChat.getSeller_id();

                    Realm.init(activity);
                    RealmCart realmCart = Realm.getInstance(RealmUtility.getDefaultConfig(activity)).where(RealmCart.class).equalTo("seller_id", seller_id).findFirst();
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
                                        Realm.init(activity);
                                        Realm.getInstance(RealmUtility.getDefaultConfig(activity)).executeTransaction(realm -> {
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

                                        startActivity(new Intent(activity, MessageActivity.class)
                                                .putExtra("SELLER_ID", seller_id)
                                                .putExtra("SELLER_NAME", seller_name[0])
                                                .putExtra("PROFILE_IMAGE_URL", profile_image_url[0])
                                                .putExtra("AVAILABILITY", availability[0])
                                        );
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            error -> {
                                error.printStackTrace();
                                startActivity(new Intent(activity, MessageActivity.class)
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
                            params.put("consumer_id", PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "CONSUMER_ID", ""));
                            Realm.init(activity);
                            Realm.getInstance(RealmUtility.getDefaultConfig(activity)).executeTransaction(realm -> {
                                RealmResults<RealmChat> results = realm.where(RealmChat.class)
                                        .sort("id", Sort.DESCENDING)
                                        .equalTo("seller_id", seller_id)
                                        .equalTo("consumer_id", PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "CONSUMER_ID", ""))
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
                            headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + APITOKEN, ""));
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

            @Override
            public void onImageClick(ArrayList<RealmChat> realmChats, int position, ChatIndexAdapter.ViewHolder holder) {

            }
        }, activity, cartArrayList);
        recyclerview.setLayoutManager(new LinearLayoutManager(activity));
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(chatIndexAdapter);

        String role = PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "ROLE", "");

        if (role.equals("SELLER")) {
            fab.setVisibility(View.GONE);
        }
        else {
            fab.setVisibility(View.VISIBLE);
        }

        fab.setOnClickListener(v -> {
            startActivity(new Intent(activity, SellerIndexActivity.class));
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateChatIndex(activity);

        StringRequest stringRequest = new StringRequest(
                com.android.volley.Request.Method.POST,
                API_URL + "scoped-latest-chats",
                response -> {
                    if (response != null) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            Realm.init(activity);
                            Realm.getInstance(RealmUtility.getDefaultConfig(activity)).executeTransaction(realm -> {
                                realm.createOrUpdateAllFromJson(RealmChat.class, jsonArray);
                            });
                            populateChatIndex(activity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.d("Cyrilll", error.toString());
                }
        ) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String role = PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "ROLE", "");

                if (role.equals("SELLER")) {
                    params.put("seller_id", PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "SELLER_ID", ""));
                } else {
                    params.put("consumer_id", PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + "CONSUMER_ID", ""));
                }
                return params;
            }

             /*Passing some request headers*/
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("accept", "application/json");
                headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(activity).getString("com.ekumfi.juice" + APITOKEN, ""));
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        InitApplication.getInstance().addToRequestQueue(stringRequest);
    }

    public static void populateChatIndex(final Context context) {
        Realm.init(context);
        Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
            RealmResults<RealmChat> results;

            String role = PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + "ROLE", "");
            if (role.equals("CONSUMER")) {
                results = realm.where(RealmChat.class)
                        .sort("id", Sort.DESCENDING)
                        .distinct("seller_id")
                        .equalTo("consumer_id", PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + "CONSUMER_ID", ""))
                        .findAll();
            }
            else {
                results = realm.where(RealmChat.class)
                        .sort("id", Sort.DESCENDING)
                        .distinct("consumer_id")
                        .equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + "SELLER_ID", ""))
                        .findAll();
            }

            if (results.size() < 1) {
                no_data.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.GONE);
            }
            else {
                no_data.setVisibility(View.GONE);
                recyclerview.setVisibility(View.VISIBLE);
            }
            newCart.clear();
            for (RealmChat realmChat : results) {
                newCart.add(realmChat);
            }
            cartArrayList.clear();
            cartArrayList.addAll(newCart);
            chatIndexAdapter.notifyDataSetChanged();
        });
    }
}
