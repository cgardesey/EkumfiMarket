package com.ekumfi.juice.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
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
import com.ekumfi.juice.adapter.FaqAdapter;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmFaq;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.ekumfi.juice.util.LocaleHelper;
import com.ekumfi.juice.util.RealmUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.myVolleyError;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;


public class HelpActivity extends AppCompatActivity {

    private ImageView teacher_image, backbtn;
    RecyclerView faqsrecyclerview;
    private static final String MY_LOGIN_ID = "MY_LOGIN_ID";
    private String api_token;
    NetworkReceiver networkReceiver;

    ArrayList<RealmFaq> faqArrayList;
    ProgressDialog dialog;
    FaqAdapter faqAdapter;
    TextView nofaqstext;
    ImageView refresh;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    public void setLanguage() {
        SharedPreferences prefs = getSharedPreferences(MY_LOGIN_ID, MODE_PRIVATE);
        String language = prefs.getString("language", "");
        // Toast.makeText(conferenceCallActivity, language, Toast.LENGTH_SHORT).show();
        if (language.contains("French")) {
//use constructor with country
            Locale locale = new Locale("fr", "BE");

            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        } else {
            Locale locale = new Locale("en", "GB");

            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        faqsrecyclerview = findViewById(R.id.helperview);
        backbtn = findViewById(R.id.search);
        nofaqstext = findViewById(R.id.nofaqstext);
        refresh = findViewById(R.id.refresh);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        refresh.setOnClickListener(v -> refresh());
        faqArrayList = new ArrayList<>();

        populateFaqs();
        faqAdapter = new FaqAdapter(faqArrayList);


        faqsrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        faqsrecyclerview.setNestedScrollingEnabled(false);
        faqsrecyclerview.setItemAnimator(new DefaultItemAnimator());
        faqsrecyclerview.setAdapter(faqAdapter);

        networkReceiver = new NetworkReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activeActivity = this;
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    public void populateFaqs() {
        Realm.init(getApplicationContext());
        Realm.getInstance(RealmUtility.getDefaultConfig(HelpActivity.this)).executeTransaction(realm -> {
            RealmResults<RealmFaq> results = null;
            String role = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "");
            if (role.equals("CONSUMER")) {
                results = realm.where(RealmFaq.class).contains("tag", "consumer").findAll();
            } else if (role.equals("SELLER")) {
                results = realm.where(RealmFaq.class).contains("tag", "seller").findAll();
            }
            if (results.size() > 0) {
                nofaqstext.setVisibility(View.GONE);
                faqsrecyclerview.setVisibility(View.VISIBLE);
            } else {
                nofaqstext.setVisibility(View.VISIBLE);
                faqsrecyclerview.setVisibility(View.GONE);
            }
            faqArrayList.clear();
            for (RealmFaq realmFaq : results) {
//                    boolean attended = realm.where(RealmAttendance.class).equalTo("audioid", realmPayment.getAudioid()).findFirst() != null;
//                    realmPayment.setAttended(attended);
                faqArrayList.add(realmFaq);
            }
        });
    }

    public void refresh() {
        try {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Checking for new data...");
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.show();
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    API_URL + "faqs",
                    null,
                    response -> {
                        if (response != null) {
                            dialog.dismiss();
                            Realm.init(getApplicationContext());
                            Realm.getInstance(RealmUtility.getDefaultConfig(HelpActivity.this)).executeTransaction(realm -> {
                                RealmResults<RealmFaq> result = realm.where(RealmFaq.class).findAll();
                                result.deleteAllFromRealm();
                                realm.createOrUpdateAllFromJson(RealmFaq.class, response);
                            });
                            populateFaqs();
                            faqAdapter.notifyDataSetChanged();
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
}

