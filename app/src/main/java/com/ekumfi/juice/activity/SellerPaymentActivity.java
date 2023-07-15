package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.myVolleyError;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.ekumfi.juice.R;
import com.ekumfi.juice.adapter.PaymentsAdapter;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmPayment;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.ekumfi.juice.util.RealmUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class SellerPaymentActivity extends AppCompatActivity {

    public static final int
        ALL = 1,
        PENDING = 2,
        SUCCESSFUL = 3;

    static int LISTTYPE = ALL;
    PaymentsAdapter paymentsAdapter;
    ArrayList<RealmPayment> payments = new ArrayList<>(), allPayments = new ArrayList<>(), pendingPayments = new ArrayList<>(), successfulPayments = new ArrayList<>();
    RecyclerView recyclerview_payments;
    ImageView searchIcon, menu;
    public static ImageView refresh;
    TextView nopaymenttext;
    ProgressDialog dialog;
    EditText search;
    NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_payment);

        networkReceiver = new NetworkReceiver();

        nopaymenttext = findViewById(R.id.nopaymenttext);
        payments = new ArrayList<>();
        recyclerview_payments = findViewById(R.id.recyclerview_payments);
        searchIcon = findViewById(R.id.searchIcon);
        search = findViewById(R.id.search);
//        menu = findViewById(R.id.menu);

        refresh = findViewById(R.id.refresh);

        refresh.setOnClickListener(v -> refresh());
        populateAllPayments();
        paymentsAdapter = new PaymentsAdapter(payments);

        recyclerview_payments.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_payments.setNestedScrollingEnabled(false);
        recyclerview_payments.setItemAnimator(new DefaultItemAnimator());
        recyclerview_payments.setAdapter(paymentsAdapter);

        /*searchIcon.setOnClickListener(v -> finish());*/

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(payments, s.toString());
                paymentsAdapter.setFilter(filter(payments, s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        activeActivity = this;
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                API_URL + "seller-payments",
                null,
                response -> {
                    if (response != null) {
                        Realm.init(getApplicationContext());
                        Realm.getInstance(RealmUtility.getDefaultConfig(SellerPaymentActivity.this)).executeTransaction(realm -> {
                            RealmResults<RealmPayment> result = realm.where(RealmPayment.class).findAll();
                            result.deleteAllFromRealm();
                            realm.createOrUpdateAllFromJson(RealmPayment.class, response);
                        });
                        payments.clear();
                        switch (LISTTYPE) {
                            case ALL:
                                populateAllPayments();
                                payments.addAll(allPayments);
                                break;
                            case PENDING:
                                populatePendingPayments();
                                payments.addAll(pendingPayments);
                                break;
                            case SUCCESSFUL:
                                populateSuccessfulPayments();
                                payments.addAll(successfulPayments);
                                break;
                        }
                        paymentsAdapter.notifyDataSetChanged();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Log.d("Cyrilll", error.toString());
                    myVolleyError(getApplicationContext(), error);
                }
        ) {
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("accept", "application/json");
                headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + APITOKEN, ""));
                return headers;
            }
        };
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                +
                        0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        InitApplication.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    public void populateAllPayments() {
        LISTTYPE = ALL;
        Realm.init(getApplicationContext());
        Realm.getInstance(RealmUtility.getDefaultConfig(SellerPaymentActivity.this)).executeTransaction(realm -> {

            RealmResults<RealmPayment> realmPayments = realm.where(RealmPayment.class)
                    .isNotNull("stock_cart_id")
                    .sort("created_at", Sort.DESCENDING)
                    .findAll();
            if (realmPayments.size() > 0) {
                nopaymenttext.setVisibility(View.GONE);
                recyclerview_payments.setVisibility(View.VISIBLE);
            } else {
                nopaymenttext.setVisibility(View.VISIBLE);
                recyclerview_payments.setVisibility(View.GONE);
            }
            payments.clear();
            for (RealmPayment realmPayment : realmPayments) {
                payments.add(realmPayment);
            }
        });
    }

    public void populatePendingPayments() {
        LISTTYPE = PENDING;
        Realm.init(getApplicationContext());
        Realm.getInstance(RealmUtility.getDefaultConfig(SellerPaymentActivity.this)).executeTransaction(realm -> {

            RealmResults<RealmPayment> results = realm.where(RealmPayment.class).equalTo("paymentstatus", "Processing").findAll();
            pendingPayments.clear();
            for (RealmPayment realmPayment : results) {
//                    boolean attended = realm.where(RealmAttendance.class).equalTo("audioid", realmPayment.getAudioid()).findFirst() != null;
//                    realmPayment.setAttended(attended);
                pendingPayments.add(realmPayment);
            }
        });
    }

    public void populateSuccessfulPayments() {
        LISTTYPE = SUCCESSFUL;
        Realm.init(getApplicationContext());
        Realm.getInstance(RealmUtility.getDefaultConfig(SellerPaymentActivity.this)).executeTransaction(realm -> {

            RealmResults<RealmPayment> results = realm.where(RealmPayment.class).equalTo("paymentstatus", "Successful").findAll();
            successfulPayments.clear();
            for (RealmPayment realmPayment : results) {
//                    boolean attended = realm.where(RealmAttendance.class).equalTo("audioid", realmPayment.getAudioid()).findFirst() != null;
//                    realmPayment.setAttended(attended);
                successfulPayments.add(realmPayment);
            }
        });
    }

    public void refresh() {
        try {
            dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.refreshing_payment_status));
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.show();
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    API_URL + "seller-payments",
                    null,
                    response -> {
                        if (response != null) {
                            dialog.dismiss();
                            Realm.init(getApplicationContext());
                            Realm.getInstance(RealmUtility.getDefaultConfig(SellerPaymentActivity.this)).executeTransaction(realm -> {
                                RealmResults<RealmPayment> result = realm.where(RealmPayment.class).findAll();
                                result.deleteAllFromRealm();
                                realm.createOrUpdateAllFromJson(RealmPayment.class, response);
                            });
                            payments.clear();
                            switch (LISTTYPE) {
                                case ALL:
                                    populateAllPayments();
                                    payments.addAll(allPayments);
                                    break;
                                case PENDING:
                                    populatePendingPayments();
                                    payments.addAll(pendingPayments);
                                    break;
                                case SUCCESSFUL:
                                    populateSuccessfulPayments();
                                    payments.addAll(successfulPayments);
                                    break;
                            }
                            paymentsAdapter.notifyDataSetChanged();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        Log.d("Cyrilll", error.toString());
                        dialog.dismiss();
                        myVolleyError(getApplicationContext(), error);
                    }
            ) {
                /** Passing some request headers* */
                @Override
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("accept", "application/json");
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + APITOKEN, ""));
                    return headers;
                }
            };
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                    +
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            InitApplication.getInstance().addToRequestQueue(jsonArrayRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<RealmPayment> filter(ArrayList<RealmPayment> models, String search_txt) {

        final ArrayList<RealmPayment> filteredModelList = new ArrayList<>();
        for (RealmPayment model : models) {

            if (
                    model.getStatus().toLowerCase().contains(search_txt.toLowerCase()) ||
                            model.getMsisdn().toLowerCase().contains(search_txt.toLowerCase())
            ) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
