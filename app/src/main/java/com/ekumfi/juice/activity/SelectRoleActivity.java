package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.MYUSERID;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.ekumfi.juice.R;
import com.ekumfi.juice.materialDialog.ConsumerNameMaterialDialog;
import com.ekumfi.juice.realm.RealmConsumer;
import com.ekumfi.juice.realm.RealmSeller;
import com.ekumfi.juice.util.RealmUtility;

import io.realm.Realm;

public class SelectRoleActivity extends AppCompatActivity {
    public static Activity selectRoleActivity;
    CardView seller, consumer, superRider;
    TextView guest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);

        selectRoleActivity = this;

        seller = findViewById(R.id.seller);
        consumer = findViewById(R.id.consumer);
        guest = findViewById(R.id.guest);

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm.init(getApplicationContext());

                final RealmSeller[] realmSeller = new RealmSeller[1];
                Realm.getInstance(RealmUtility.getDefaultConfig(SelectRoleActivity.this)).executeTransaction(realm -> {

                    realmSeller[0] = Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext()))
                            .where(RealmSeller.class)
                            .equalTo("user_id", PreferenceManager.getDefaultSharedPreferences(SelectRoleActivity.this).getString("com.ekumfi.juice" + MYUSERID, ""))
                            .findFirst();
                });

                if (realmSeller[0] == null) {
                    startActivity(
                            new Intent(getApplicationContext(), SellerAccountActivity.class)
                                    .putExtra("MODE", "ADD")
                    );
                } else {
                    PreferenceManager
                            .getDefaultSharedPreferences(SelectRoleActivity.this)
                            .edit()
                            .putString("com.ekumfi.juice" + "ROLE", "SELLER")
                            .putString("com.ekumfi.juice" + "SELLER_ID", realmSeller[0].getSeller_id())
                            .apply();
                    startActivity(new Intent(getApplicationContext(), SellerHomeActivity.class));
                    finish();
                }
            }
        });

        consumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm.init(getApplicationContext());

                final RealmConsumer[] realmConsumer = new RealmConsumer[1];
                Realm.getInstance(RealmUtility.getDefaultConfig(SelectRoleActivity.this)).executeTransaction(realm -> {

                    realmConsumer[0] = Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).where(RealmConsumer.class)
                            .equalTo("user_id", PreferenceManager.getDefaultSharedPreferences(SelectRoleActivity.this).getString("com.ekumfi.juice" + MYUSERID, ""))
                            .findFirst();
                });

                if (realmConsumer[0] == null) {
                    ConsumerNameMaterialDialog consumerNameMaterialDialog = new ConsumerNameMaterialDialog();
                    if(consumerNameMaterialDialog != null && consumerNameMaterialDialog.isAdded()) {

                    } else {
                        consumerNameMaterialDialog.show(getSupportFragmentManager(), "ConsumerNameMaterialDialog");
                    }
                } else {
                    PreferenceManager
                            .getDefaultSharedPreferences(SelectRoleActivity.this)
                            .edit()
                            .putString("com.ekumfi.juice" + "ROLE", "CONSUMER")
                            .putString("com.ekumfi.juice" + "CONSUMER_ID", realmConsumer[0].getConsumer_id())
                            .apply();
                    startActivity(new Intent(getApplicationContext(), ConsumerHomeActivity.class));
                    finish();
                }
            }
        });
    }
}