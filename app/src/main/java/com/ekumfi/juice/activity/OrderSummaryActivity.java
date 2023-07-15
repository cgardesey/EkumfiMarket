package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.ConsumerAccountActivity.*;
import static com.ekumfi.juice.activity.SellerAccountActivity.*;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.ekumfi.juice.realm.RealmSeller;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.greysonparrelli.permiso.PermisoActivity;
import com.ekumfi.juice.R;
import com.ekumfi.juice.materialDialog.ChoosePaymentMethodMaterialDialog;
import com.ekumfi.juice.realm.RealmConsumer;
import com.ekumfi.juice.util.RealmUtility;

import io.realm.Realm;


public class OrderSummaryActivity extends PermisoActivity {

    NetworkReceiver networkReceiver;
    Button pay;
    TextView name, location, contact, summary, subtotal, totalfee, shippingfee, edit;
    public static Activity orderSummaryActivity;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        orderSummaryActivity = this;

        role = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "");

        name = findViewById(R.id.name);
        edit = findViewById(R.id.edit);
        location = findViewById(R.id.location);
        contact = findViewById(R.id.contact);
        summary = findViewById(R.id.summary);
        subtotal = findViewById(R.id.subtotal);
        totalfee = findViewById(R.id.totalfee);
        shippingfee = findViewById(R.id.shippingfee);

        pay = findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location.getText().toString().toLowerCase().equals("location not set")) {
                    location.setError(getString(R.string.error_field_required));
                    Toast.makeText(getApplicationContext(), "Please set your location.", Toast.LENGTH_SHORT).show();

                } else {
                    location.setError(null);
                    ChoosePaymentMethodMaterialDialog choosePaymentMethodMaterialDialog = new ChoosePaymentMethodMaterialDialog();
                    if (choosePaymentMethodMaterialDialog != null && choosePaymentMethodMaterialDialog.isAdded()) {

                    } else {
                        choosePaymentMethodMaterialDialog.setAmount(String.valueOf(getIntent().getFloatExtra("SUB_TOTAL", 0.00F)));
                        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + "ROLE", "").equals("CONSUMER")) {
                            choosePaymentMethodMaterialDialog.setCart_id(getIntent().getStringExtra("CART_ID"));
                        } else {
                            choosePaymentMethodMaterialDialog.setStock_cart_id(getIntent().getStringExtra("STOCK_CART_ID"));
                        }
                        choosePaymentMethodMaterialDialog.show(getSupportFragmentManager(), "choosePaymentMethodMaterialDialog");
                        choosePaymentMethodMaterialDialog.setCancelable(true);
                    }
                }
            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm.init(getApplicationContext());
                if (role.equals("CONSUMER")) {
                    realmConsumer = Realm.getInstance(RealmUtility.getDefaultConfig(orderSummaryActivity)).where(RealmConsumer.class).equalTo("consumer_id", PreferenceManager.getDefaultSharedPreferences(orderSummaryActivity).getString("com.ekumfi.juice" + "CONSUMER_ID", "")).findFirst();
                    startActivity(new Intent(getApplicationContext(), ConsumerAccountActivity.class)
                            .putExtra("MODE", "EDIT")
                    );
                } else {
                    realmSeller = Realm.getInstance(RealmUtility.getDefaultConfig(orderSummaryActivity)).where(RealmSeller.class).equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(orderSummaryActivity).getString("com.ekumfi.juice" + "SELLER_ID", "")).findFirst();
                    startActivity(new Intent(getApplicationContext(), SellerAccountActivity.class)
                            .putExtra("MODE", "EDIT")
                    );
                }
            }
        });

        networkReceiver = new NetworkReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();

        activeActivity = this;
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        String contact;
        if (role.equals("CONSUMER")) {
            final RealmConsumer[] realmConsumer = new RealmConsumer[1];
            Realm.init(getApplicationContext());
            realmConsumer[0] = Realm.getInstance(RealmUtility.getDefaultConfig(orderSummaryActivity)).where(RealmConsumer.class).equalTo("consumer_id", PreferenceManager.getDefaultSharedPreferences(orderSummaryActivity).getString("com.ekumfi.juice" + "CONSUMER_ID", "")).findFirst();
            name.setText(realmConsumer[0].getName());
            location.setText(realmConsumer[0].getStreet_address() != null && !realmConsumer[0].getStreet_address().equals("") ? realmConsumer[0].getStreet_address() : Html.fromHtml("<i>Location not set</i>"));
            contact = realmConsumer[0].getPrimary_contact();
            /*String auxiliary_contact = realmConsumer[0].getAuxiliary_contact();
            if (auxiliary_contact != null && !auxiliary_contact.equals("")) {
                contact += " / " + auxiliary_contact;
            }*/
        } else {
            final RealmSeller[] realmSeller = new RealmSeller[1];
            Realm.init(getApplicationContext());
            realmSeller[0] = Realm.getInstance(RealmUtility.getDefaultConfig(orderSummaryActivity)).where(RealmSeller.class).equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(orderSummaryActivity).getString("com.ekumfi.juice" + "SELLER_ID", "")).findFirst();
            name.setText(realmSeller[0].getShop_name());
            location.setText(realmSeller[0].getStreet_address() != null && !realmSeller[0].getStreet_address().equals("") ? realmSeller[0].getStreet_address() : Html.fromHtml("<i>Location not set</i>"));
            contact = realmSeller[0].getPrimary_contact();
            /*String auxiliary_contact = realmSeller[0].getAuxiliary_contact();
            if (auxiliary_contact != null && !auxiliary_contact.equals("")) {
                contact += " / " + auxiliary_contact;
            }*/
        }
        this.contact.setText(contact);
        summary.setText("Order Summary (" + String.valueOf(getIntent().getIntExtra("ITEM_COUNT", 0)) + ")");
        subtotal.setText("GHC" + String.format("%.2f", getIntent().getFloatExtra("SUB_TOTAL", 0.00F)));
        shippingfee.setText("GHC" + String.format("%.2f", getIntent().getFloatExtra("SHIPPING_FEE", 0.00F)));
//        totalfee.setText("GHC" + String.format("%.2f", getIntent().getFloatExtra("SHIPPING_FEE", 0.00F) + getIntent().getFloatExtra("SUB_TOTAL", 0.00F)));

        totalfee.setText("GHC" + String.format("%.2f", getIntent().getFloatExtra("SUB_TOTAL", 0.00F)));

        if (location.getText().toString().toLowerCase().equals("location not set")) {
            location.setError(getString(R.string.error_field_required));
        } else {
            location.setError(null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }
}
