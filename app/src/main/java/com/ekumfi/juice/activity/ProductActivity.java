package com.ekumfi.juice.activity;

import static com.ekumfi.juice.constants.Const.isTablet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.greysonparrelli.permiso.PermisoActivity;
import com.ekumfi.juice.R;
import com.ekumfi.juice.adapter.ProductListAdapter;
import com.ekumfi.juice.realm.RealmProduct;
import com.ekumfi.juice.util.RealmUtility;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class ProductActivity extends PermisoActivity {

    RecyclerView recyclerView;
    ImageView backbtn;
    ArrayList<String> newList = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    ProductListAdapter productListAdapter;
    String product_category_id;
    String title = "";
    Context mContext;

    ArrayList<RealmProduct> realmProductArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mContext = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerView);
        backbtn = findViewById(R.id.backbtn);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        backbtn.setOnClickListener(v -> finish());
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        if (isTablet(getApplicationContext())) {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        }
        productListAdapter = new ProductListAdapter((realmProducts, position, holder) -> {
            RealmProduct realmProduct = realmProducts.get(position);

            setResult(Activity.RESULT_OK, new Intent()
                    .putExtra("NAME", realmProduct.getName())
                    .putExtra("PRODUCT_ID", realmProduct.getProduct_id())
            );
            finish();

        }, ProductActivity.this, realmProductArrayList, "");
        recyclerView.setAdapter(productListAdapter);
        populateProducts();
    }

    private void populateProducts() {
        Realm.init(getApplicationContext());
        Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<RealmProduct> realmProductCategories = realm.where(RealmProduct.class).findAll();
                realmProductArrayList.clear();
                for (RealmProduct realmProviderCategory : realmProductCategories) {
                    realmProductArrayList.add(realmProviderCategory);
                }
                if (realmProductArrayList.size() > 0) {
                    productListAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
