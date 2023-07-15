package com.ekumfi.juice.fragment;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.myVolleyError;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.R;
import com.ekumfi.juice.adapter.SellerProductAdapter;
import com.ekumfi.juice.materialDialog.SellerProductMaterialDialog;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmSellerProduct;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class SellerProductsFragment extends Fragment {
    private static final String TAG = "ProductsFragment";
    public static RecyclerView recyclerView;
    Context mContext;
    LinearLayout clickToAdd;
    CardView cardView;
    public static ArrayList<RealmSellerProduct> productArrayList;
    public static SellerProductAdapter productAdapter;
    public static RecyclerView.LayoutManager layoutManager;
    public static SellerProductMaterialDialog productMaterialDialog = new SellerProductMaterialDialog();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_seller_products, container, false);


        productArrayList = new ArrayList<>();
        mContext= getContext();
        cardView = rootView.findViewById(R.id.cardView);
        clickToAdd = rootView.findViewById(R.id.clickToAdd);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(getActivity());

        init();

        clickToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productMaterialDialog != null && productMaterialDialog.isAdded()) {

                } else {
                    productMaterialDialog.setName("");
                    productMaterialDialog.setUnit_price("");
                    productMaterialDialog.setQuantity_available("");
                    productMaterialDialog.setCancelable(false);
                    productMaterialDialog.show(getFragmentManager(), "addProductMaterialDialog");
                    productMaterialDialog.setCancelable(true);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void init() {
        Realm.init(getContext());
        Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {

            RealmResults<RealmSellerProduct> realmSellerProducts = realm.where(RealmSellerProduct.class).equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(mContext).getString("com.ekumfi.juice" + "SELLER_ID", "")).findAll();
            productArrayList.clear();
            for (RealmSellerProduct realmSellerProduct : realmSellerProducts) {
                productArrayList.add(realmSellerProduct);
            }
        });

        productAdapter = new SellerProductAdapter(new SellerProductAdapter.SellerProductAdapterInterface() {
            @Override
            public void onListItemClick(ArrayList<RealmSellerProduct> realmSellerProducts, int position, SellerProductAdapter.ViewHolder holder) {
                RealmSellerProduct realmSellerProduct = realmSellerProducts.get(position);

                PopupMenu popup = new PopupMenu(mContext, holder.more_details);

                popup.inflate(R.menu.product_menu);

                popup.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.edit) {
                        SellerProductMaterialDialog productMaterialDialog = new SellerProductMaterialDialog();
                        if (productMaterialDialog != null && productMaterialDialog.isAdded()) {

                        } else {
                            productMaterialDialog.setProduct_id(realmSellerProduct.getProduct_id());
                            productMaterialDialog.setSeller_product_id(realmSellerProduct.getSeller_product_id());
                            productMaterialDialog.setName(realmSellerProduct.getProduct_name());
                            productMaterialDialog.setUnit_price(String.format("%.2f", realmSellerProduct.getUnit_price()));
                            productMaterialDialog.setQuantity_available(String.valueOf(realmSellerProduct.getQuantity_available()));

                            productMaterialDialog.setCancelable(false);
                            productMaterialDialog.show(getFragmentManager(), "editProductMaterialDialog");
                            productMaterialDialog.setCancelable(true);
                        }
                        return true;
                    } else if (itemId == R.id.remove) {
                        String product_id = realmSellerProduct.getProduct_id();
                        StringRequest stringRequestDelete = new StringRequest(
                                Request.Method.DELETE,
                                API_URL + "seller-products/" + realmSellerProduct.getSeller_product_id(),
                                response -> {
                                    if (response != null) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if (jsonObject.getBoolean("status")) {
                                                Realm.init(getActivity());
                                                Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                                    realmSellerProducts.get(position).deleteFromRealm();
                                                });
                                                realmSellerProducts.remove(position);
                                                productAdapter.notifyDataSetChanged();
                                                Toast.makeText(mContext, "Successfully deleted.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, "Error deleting.", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                error -> {
                                    error.printStackTrace();
                                    myVolleyError(mContext, error);
                                    Log.d("Cyrilll", error.toString());
                                }
                        ) {
                            @Override
                            public Map getHeaders() throws AuthFailureError {
                                HashMap headers = new HashMap();
                                headers.put("accept", "application/json");
                                headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(mContext).getString("com.ekumfi.juice" + APITOKEN, ""));
                                return headers;
                            }

                            @Override
                            public Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("product_id", product_id);
                                return params;
                            }
                        };
                        stringRequestDelete.setRetryPolicy(new DefaultRetryPolicy(
                                0,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        InitApplication.getInstance().addToRequestQueue(stringRequestDelete);
                        return true;
                    }
                    return false;
                });
                popup.show();
            }
        }, productArrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //  myrecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(productAdapter);
    }

    public boolean validate (){
        boolean validated = true;
        return validated;
    }
}