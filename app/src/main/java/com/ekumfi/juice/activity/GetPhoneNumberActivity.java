package com.ekumfi.juice.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;
import com.ekumfi.juice.R;
import com.ekumfi.juice.util.LocaleHelper;

import java.util.Locale;


/**
 * Created by Nana on 11/26/2017.
 */

public class GetPhoneNumberActivity extends AppCompatActivity {

    private static final String MY_LOGIN_ID = "MY_LOGIN_ID";
    Button nextpagebtn, backbtn;
    ImageView imgView;
    CountryCodePicker ccp;
    public static EditText phoneView;
    TextView termsandconditions, termsandconditionstext;
    public static String phone_number;

    TextView useridView;
    private static RecyclerView.LayoutManager layoutManager;
    private ShimmerFrameLayout shimmer_view_container;
    private String api_token;
    int type;
    public static Activity getPhoneNumberActivity;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    public void setLanguage() {
        SharedPreferences prefs = getSharedPreferences(MY_LOGIN_ID, MODE_PRIVATE);
        String language = prefs.getString("language", "");
        // Toast.makeText(activity, language, Toast.LENGTH_SHORT).show();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.activity_getphonenumber);
        getPhoneNumberActivity = this;
        //final View rootView = inflater.inflate(R.layout.getphonenumberfragment, container, false);
        ccp = findViewById(R.id.ccp);
        nextpagebtn = findViewById(R.id.nextpagebtn);
        backbtn = findViewById(R.id.backbtn);
        phoneView = findViewById(R.id.phoneView);
        ccp.registerPhoneNumberTextView(phoneView);

        SharedPreferences prefs = getSharedPreferences(MY_LOGIN_ID, MODE_PRIVATE);
        String userid = prefs.getString("userid", "No name defined");//"No name defined" is the default value.
        String api_token = prefs.getString("api_token", "");

        phoneView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() < 10) {
                    nextpagebtn.setEnabled(false);
                }
                else {
                    nextpagebtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        nextpagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phonenumber = phoneView.getText().toString().replace(" ", "");
                if (phonenumber.length() == 10 && phonenumber.charAt(0) == '0') {
                    GetPhoneNumberActivity.phone_number = new StringBuilder(ccp.getFullNumber()).deleteCharAt(3).toString();
                }
                else {
                    phoneView.setError("Invalid number");
                    return;
                }
                startActivity(new Intent(GetPhoneNumberActivity.this, GetAuthActivity.class));
            }
        });
    }
}
