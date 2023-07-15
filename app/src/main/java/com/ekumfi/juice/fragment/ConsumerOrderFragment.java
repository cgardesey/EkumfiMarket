package com.ekumfi.juice.fragment;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.myVolleyError;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.activity.OrderSummaryActivity;
import com.ekumfi.juice.realm.RealmConsumer;
import com.greysonparrelli.permiso.Permiso;
import com.ekumfi.juice.R;
import com.ekumfi.juice.activity.CartItemsActivity;
import com.ekumfi.juice.adapter.ConsumerCartListAdapter;
import com.ekumfi.juice.materialDialog.ChooseServiceContactMethodMaterialDialog;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmCart;
import com.ekumfi.juice.realm.RealmCartProduct;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class ConsumerOrderFragment extends Fragment {
    RecyclerView recyclerview;
    TextView no_data;
    ConsumerCartListAdapter cartListAdapter;
    ArrayList<RealmCart> cartArrayList = new ArrayList<>(), newCart = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_order, container, false);

        recyclerview = rootView.findViewById(R.id.recyclerview);
        no_data = rootView.findViewById(R.id.no_data);

        cartListAdapter = new ConsumerCartListAdapter(new ConsumerCartListAdapter.CartAdapterInterface() {
            @Override
            public void onViewClick(ArrayList<RealmCart> realmCarts, int position, ConsumerCartListAdapter.ViewHolder holder) {
                RealmCart realmCart = realmCarts.get(position);
                String cart_id = realmCart.getCart_id();
                ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();

                StringRequest stringRequest = new StringRequest(
                        com.android.volley.Request.Method.POST,
                        API_URL + "scoped-cart-products",
                        response -> {
                            if (response != null) {
                                dialog.dismiss();
                                try {
                                    final float[] sub_total = {0.00F};
                                    JSONArray jsonArray = new JSONArray(response);
                                    Realm.init(getActivity());
                                    Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                        realm.where(RealmCartProduct.class).findAll().deleteAllFromRealm();
                                        realm.createOrUpdateAllFromJson(RealmCartProduct.class, jsonArray);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            try {
                                                sub_total[0] += (float) jsonArray.getJSONObject(i).getDouble("price");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    startActivity(
                                            new Intent(getActivity(), CartItemsActivity.class)
                                                    .putExtra("IS_INVOICE", realmCart.getStatus() != null && realmCart.getStatus().equals("SUCCESS"))
                                                    .putExtra("INVOICE_SUB_TOTAL", sub_total[0])
                                                    .putExtra("SHIPPING_FEE", (float) realmCart.getShipping_fee())
                                                    .putExtra("CART_ID", realmCart.getCart_id())
                                    );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            myVolleyError(getActivity(), error);
                            dialog.dismiss();
                            Log.d("Cyrilll", error.toString());
                        }
                ) {
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("cart_id", cart_id);
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

            @Override
            public void onContactClick(ArrayList<RealmCart> realmCarts, int position, ConsumerCartListAdapter.ViewHolder holder) {
                RealmCart realmCart = realmCarts.get(position);
                ChooseServiceContactMethodMaterialDialog chooseServiceContactMethodMaterialDialog = new ChooseServiceContactMethodMaterialDialog();
                if (chooseServiceContactMethodMaterialDialog != null && chooseServiceContactMethodMaterialDialog.isAdded()) {

                } else {
                    chooseServiceContactMethodMaterialDialog.setConsumer_id(realmCart.getConsumer_id());
                    chooseServiceContactMethodMaterialDialog.setSeller_id(realmCart.getSeller_id());
                    chooseServiceContactMethodMaterialDialog.setOrder_id(realmCart.getOrder_id());
                    chooseServiceContactMethodMaterialDialog.show(getChildFragmentManager(), "chooseContactMethodMaterialDialog");
                    chooseServiceContactMethodMaterialDialog.setCancelable(true);
                }
            }

            @Override
            public void onOrderClick(ArrayList<RealmCart> realmCarts, int position, ConsumerCartListAdapter.ViewHolder holder) {
                RealmCart realmCart = realmCarts.get(position);

                String cart_id = realmCart.getCart_id();
                ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setIndeterminate(true);
                dialog.show();

                StringRequest stringRequest = new StringRequest(
                        com.android.volley.Request.Method.POST,
                        API_URL + "cart-total",
                        response -> {
                            if (response != null) {
                                dialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.has("updated_cart")) {
                                        Realm.init(getActivity());
                                        Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                            try {
                                                realm.createOrUpdateAllFromJson(RealmCart.class, jsonObject.getJSONArray("updated_cart"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        });
                                        populateCart(getActivity());
                                        Toast.makeText(getActivity(), "Items no longer in cart", Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        startActivity(new Intent(getActivity(), OrderSummaryActivity.class)
                                                .putExtra("ITEM_COUNT", realmCart.getItem_count())
                                                .putExtra("SUB_TOTAL", (float)jsonObject.getJSONArray("cart_total").getJSONObject(0).getDouble("total_amount"))
                                                .putExtra("SHIPPING_FEE", 20.00F)
                                                .putExtra("CART_ID", realmCart.getCart_id()));
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        error -> {
                            error.printStackTrace();
                            myVolleyError(getActivity(), error);
                            dialog.dismiss();
                            Log.d("Cyrilll", error.toString());
                        }
                ) {
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("cart_id", cart_id);
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

            @Override
            public void onDeliveryClick(ArrayList<RealmCart> realmCarts, int position, ConsumerCartListAdapter.ViewHolder holder) {
                Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                                                             @Override
                                                             public void onPermissionResult(Permiso.ResultSet resultSet) {
                                                                 if (resultSet.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && resultSet.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
                                                                     RealmCart realmCart = realmCarts.get(position);

                                                                     /*SessionConfiguration config;
                                                                     config = new SessionConfiguration.Builder()
                                                                             .setClientId("S1x5HfaZ8XrZxeRJJElSYiw_UzWdGyF0") //This is necessary
//                .setRedirectUri("YOUR_REDIRECT_URI") //This is necessary if you'll be using implicit grant
                                                                             .setEnvironment(SessionConfiguration.Environment.SANDBOX) //Useful for testing your app in the sandbox environment
                                                                             .setScopes(Arrays.asList(Scope.PROFILE, Scope.RIDE_WIDGETS)) //Your scopes for authentication here
                                                                             .build();

                                                                     RideParameters rideParams = new RideParameters.Builder()
                                                                             .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                                                                             .setPickupLocation(37.775304, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                                                                             .setDropoffLocation(37.795079, -122.4397805, "Embarcadero", "One Embarcadero Center, San Francisco")
                                                                             .build();
//                                                                     requestButton.setRideParameters(rideParams);

                                                                     RideRequestDeeplink deeplink = new RideRequestDeeplink.Builder(getActivity())
                                                                             .setSessionConfiguration(config)
                                                                             .setRideParameters(rideParams)
                                                                             .build();
                                                                     deeplink.execute();*/

                                                                     Realm.init(getActivity());
                                                                     RealmConsumer realmConsumer = Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).where(RealmConsumer.class).equalTo("consumer_id", androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "CONSUMER_ID", "")).findFirst();
                                                                     /*String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+String.valueOf(realmConsumer.getLatitude())+","+String.valueOf(realmConsumer.getLatitude())+"&daddr="+String.valueOf(realmCart.getLatitude())+","+String.valueOf(realmCart.getLongitude());
                                                                     Intent intent = new Intent(Intent.ACTION_MAIN, Uri.parse(uri));
                                                                     startActivity(Intent.createChooser(intent, "Select an application"));
*/

                                                                     String uri = "geo: "+ String.valueOf(realmCart.getSeller_latitude())+","+String.valueOf(realmCart.getSeller_longitude())+
                                                                             "?q="+  String.valueOf(realmCart.getSeller_latitude())+","+String.valueOf(realmCart.getSeller_longitude());
                                                                     startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
                                                                 }
                                                             }

                                                             @Override
                                                             public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                                                                 Permiso.getInstance().showRationaleInDialog(getActivity().getString(R.string.permissions), getActivity().getString(R.string.this_permission_is_mandatory_pls_allow_access), null, callback);
                                                             }
                                                         },
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }, getActivity(), cartArrayList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(cartListAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateCart(getActivity());
    }

    void populateCart(final Context context) {
        Realm.init(context);
        Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
            RealmResults<RealmCart> results = null;
            if (getArguments().getString("status").equals("Delivered")) {
                results = realm.where(RealmCart.class)
                        .equalTo("delivered", 1)
                        .equalTo("consumer_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "CONSUMER_ID", ""))
                        .findAll();
            } else if (getArguments().getString("status").equals("Unpaid")) {
                results = realm.where(RealmCart.class)
                        .notEqualTo("status", "SUCCESS")
                        .equalTo("consumer_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "CONSUMER_ID", ""))
                        .findAll();
            } else {
                results = realm.where(RealmCart.class)
                        .equalTo("status", "SUCCESS")
                        .equalTo("consumer_id", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + "CONSUMER_ID", ""))
                        .findAll();
            }
            newCart.clear();
            if (results.size() > 0) {
                no_data.setVisibility(View.GONE);
                recyclerview.setVisibility(View.VISIBLE);
            } else {
                no_data.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.GONE);
            }
            for (RealmCart realmCart : results) {
                newCart.add(realmCart);
            }
            cartArrayList.clear();
            cartArrayList.addAll(newCart);
            cartListAdapter.notifyDataSetChanged();
        });
    }
}
