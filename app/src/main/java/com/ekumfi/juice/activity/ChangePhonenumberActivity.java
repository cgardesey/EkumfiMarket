package com.ekumfi.juice.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.ekumfi.juice.R;
import com.ekumfi.juice.util.LocaleHelper;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;


/**
 * Created by Nana on 11/26/2017.
 */

public class ChangePhonenumberActivity extends AppCompatActivity {

    private static final String MY_LOGIN_ID = "MY_LOGIN_ID";
    Button nextpagebtn, backbtn;
    ImageView imgView;
    CountryCodePicker ccp_old, ccp_new;
    public static EditText oldphonenumber, newphonenumber;
    public static String old_phone_number, new_phone_number;
    public static Activity getChangePhoneNumberActivity;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phonenumber);
        getChangePhoneNumberActivity = this;
        ccp_old = findViewById(R.id.ccp_old);
        ccp_new = findViewById(R.id.ccp_new);
        nextpagebtn = findViewById(R.id.nextpagebtn);
        backbtn = findViewById(R.id.backbtn);
        oldphonenumber = findViewById(R.id.oldphonenumber);
        newphonenumber = findViewById(R.id.newphonenumber);
        ccp_old.registerPhoneNumberTextView(oldphonenumber);
        ccp_new.registerPhoneNumberTextView(newphonenumber);

        oldphonenumber.addTextChangedListener(new TextWatcher() {
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

                if (validate()) {
                    startActivity(new Intent(ChangePhonenumberActivity.this, GetPhoneChangeAuthActivity.class));
                }
            }
        });
    }

    public boolean validate() {
        boolean validated = true;

        String phonenumber_old = oldphonenumber.getText().toString().replace(" ", "");
        if (phonenumber_old.length() == 10 && phonenumber_old.charAt(0) == '0') {
            old_phone_number = new StringBuilder(ccp_old.getFullNumber()).deleteCharAt(3).toString();
        }
        else {
            oldphonenumber.setError("Invalid number");
            validated = false;
        }

        String phonenumber_new = newphonenumber.getText().toString().replace(" ", "");
        if (phonenumber_new.length() == 10 && phonenumber_new.charAt(0) == '0') {
            new_phone_number = new StringBuilder(ccp_new.getFullNumber()).deleteCharAt(3).toString();
        }
        else {
            newphonenumber.setError("Invalid number");
            validated = false;
        }
        return validated;
    }
}
