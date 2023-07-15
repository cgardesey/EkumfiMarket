package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.activity.GetAuthActivity.MYUSERID;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.isNetworkAvailable;
import static com.ekumfi.juice.fragment.SellerAccountFragment3.identification_image_file;
import static com.ekumfi.juice.fragment.SellerAccountFragment3.identification_type_spinner;
import static com.ekumfi.juice.fragment.SellerAccountFragment3.identification_number;
import static com.ekumfi.juice.fragment.SellerAccountFragment1.shop_name;
import static com.ekumfi.juice.fragment.SellerAccountFragment1.seller_type_spinner;
import static com.ekumfi.juice.fragment.SellerAccountFragment1.shop_image_file;
import static com.ekumfi.juice.fragment.SellerAccountFragment2.momo_number;
import static com.ekumfi.juice.fragment.SellerAccountFragment2.latitude;
import static com.ekumfi.juice.fragment.SellerAccountFragment2.longitude;
import static com.ekumfi.juice.fragment.SellerAccountFragment2.primary_contact;
import static com.ekumfi.juice.receiver.NetworkReceiver.activeActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ekumfi.juice.fragment.SellerAccountFragment3;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.greysonparrelli.permiso.Permiso;
import com.greysonparrelli.permiso.PermisoActivity;
import com.ekumfi.juice.R;
import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.fragment.SellerAccountFragment1;
import com.ekumfi.juice.fragment.SellerAccountFragment2;
import com.ekumfi.juice.other.MyHttpEntity;
import com.ekumfi.juice.pagerAdapter.SellerAccountPageAdapter;
import com.ekumfi.juice.realm.RealmBanner;
import com.ekumfi.juice.realm.RealmSeller;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.ekumfi.juice.util.NonSwipeableViewPager;
import com.ekumfi.juice.util.RealmUtility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import io.realm.Realm;


@SuppressWarnings("HardCodedStringLiteral")
public class SellerAccountActivity extends PermisoActivity {

    public static RealmSeller realmSeller = new RealmSeller();
    static Context context;


    boolean close = false;
    NonSwipeableViewPager mViewPager;
    SellerAccountPageAdapter sellerAccountPageAdapter;
    FloatingActionButton moveprevious, movenext, done;

    RelativeLayout rootview;
    ProgressBar progressBar;
    String tag1 = "android:switcher:" + R.id.pageques_seller + ":" + 0;
    String tag2 = "android:switcher:" + R.id.pageques_seller + ":" + 1;
    String tag3 = "android:switcher:" + R.id.pageques_seller + ":" + 2;

    SellerAccountFragment1 tabFrag1;
    SellerAccountFragment2 tabFrag2;
    SellerAccountFragment3 tabFrag3;

    String seller_id;
    private ProgressDialog mProgress;
    NetworkReceiver networkReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        networkReceiver = new NetworkReceiver();
        Permiso.getInstance().setActivity(this);

        setContentView(R.layout.activity_seller_account);
        getSupportActionBar().hide();

        mProgress = new ProgressDialog(this);
        mProgress.setTitle(getString(R.string.updating_profile));
        mProgress.setMessage(getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        rootview = findViewById(R.id.root);

        if (getIntent().getStringExtra("MODE").equals("EDIT")) {
            Realm.init(getApplicationContext());
            realmSeller = Realm.getInstance(RealmUtility.getDefaultConfig(SellerAccountActivity.this)).where(RealmSeller.class).equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(SellerAccountActivity.this).getString("com.ekumfi.juice" + "SELLER_ID", "")).findFirst();
            if (realmSeller != null) {
                seller_id = realmSeller.getSeller_id();
            }
        }
        progressBar = findViewById(R.id.pbar_pic);
        Realm.init(getApplicationContext());
        sellerAccountPageAdapter = new SellerAccountPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.pageques_seller);
        mViewPager.setAdapter(sellerAccountPageAdapter);
        mViewPager.setOffscreenPageLimit(2); //posible candidate for bug
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem());
                return true;
            }
        });
        progressBar.setVisibility(View.GONE);

        movenext = findViewById(R.id.movenext);
        moveprevious = findViewById(R.id.moveprevious);
        done = findViewById(R.id.done);
        movenext.setOnClickListener(v -> {
            tabFrag1 = (SellerAccountFragment1) getSupportFragmentManager().findFragmentByTag(tag1);
            tabFrag2 = (SellerAccountFragment2) getSupportFragmentManager().findFragmentByTag(tag2);
            tabFrag3 = (SellerAccountFragment3) getSupportFragmentManager().findFragmentByTag(tag3);

            switch (mViewPager.getCurrentItem()) {
                case 0:
                    if (tabFrag1.validate()) {
                        mViewPager.setCurrentItem(1);
                        moveprevious.setVisibility(View.VISIBLE);
                        movenext.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (tabFrag2.validate()) {
                        mViewPager.setCurrentItem(2);
                        moveprevious.setVisibility(View.VISIBLE);
                        movenext.setVisibility(View.GONE);
                        done.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        });
        done.setOnClickListener(v -> {
            if (tabFrag3.validate()) {
                if (isNetworkAvailable(SellerAccountActivity.this)) {
                    if (seller_id != null && !seller_id.equals("")) {
                        new updateSellerAsync(getApplicationContext()).execute();
                    } else {
                        new addSellerAsync(getApplicationContext()).execute();
                    }
                } else {
                    Toast.makeText(SellerAccountActivity.this, getString(R.string.internet_connection_is_needed), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SellerAccountActivity.this, getString(R.string.pls_correct_the_errors), Toast.LENGTH_LONG).show();
            }
        });
        moveprevious.setOnClickListener(v -> {
            tabFrag1 = (SellerAccountFragment1) getSupportFragmentManager().findFragmentByTag(tag1);
            tabFrag2 = (SellerAccountFragment2) getSupportFragmentManager().findFragmentByTag(tag2);
            tabFrag3 = (SellerAccountFragment3) getSupportFragmentManager().findFragmentByTag(tag3);

            switch (mViewPager.getCurrentItem()) {
                case 2:
                    if (tabFrag1.validate()) {
                        mViewPager.setCurrentItem(1);
                        movenext.setVisibility(View.VISIBLE);
                        moveprevious.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                case 1:
                    if (tabFrag1.validate()) {
                        mViewPager.setCurrentItem(0);
                        movenext.setVisibility(View.VISIBLE);
                        moveprevious.setVisibility(View.GONE);
                        done.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
        activeActivity = this;
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void showTwoButtonSnackbar() {

        // Create the Snackbar
        LinearLayout.LayoutParams objLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final Snackbar snackbar = Snackbar.make(rootview, "Exit?", Snackbar.LENGTH_INDEFINITE);

        // Get the Snackbar layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        // Inflate our courseListMaterialDialog viewBitmap bitmap = ((RoundedDrawable)profilePic.getDrawable()).getSourceBitmap();
        View snackView = getLayoutInflater().inflate(R.layout.snackbar, null);


        TextView textViewOne = snackView.findViewById(R.id.first_text_view);
        textViewOne.setText(this.getResources().getString(R.string.yes));
        textViewOne.setOnClickListener(v -> {
            snackbar.dismiss();
            close = true;
            SellerAccountActivity.this.onBackPressed();

            //  finish();
        });

        final TextView textViewTwo = snackView.findViewById(R.id.second_text_view);

        textViewTwo.setText(this.getResources().getString(R.string.no));
        textViewTwo.setOnClickListener(v -> {
            Log.d("Deny", "showTwoButtonSnackbar() : deny clicked");
            snackbar.dismiss();


        });

        // Add our courseListMaterialDialog view to the Snackbar's layout
        layout.addView(snackView, objLayoutParams);

        // Show the Snackbar
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        if (close) {
            super.onBackPressed();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
        showTwoButtonSnackbar();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private class addSellerAsync extends AsyncTask<Void, Integer, String> {

        HttpClient httpClient = new DefaultHttpClient();
        private Context context;

        private addSellerAsync(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpResponse httpResponse = null;
            HttpEntity httpEntity = null;
            String responseString = null;
            String URL = API_URL + "sellers";
            try {
                HttpPost httpPost = new HttpPost(URL);
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

                // Add the file to be uploaded
                if (shop_image_file != null) {
                    multipartEntityBuilder.addPart("shop_image_file", new FileBody(shop_image_file));
                }
                multipartEntityBuilder.addTextBody("shop_name", shop_name.getText().toString().trim());
                multipartEntityBuilder.addTextBody("seller_type", seller_type_spinner.getSelectedItem().toString());

                multipartEntityBuilder.addTextBody("primary_contact", primary_contact.getText().toString());
//                multipartEntityBuilder.addTextBody("auxiliary_contact", auxiliary_contact.getText().toString());
                multipartEntityBuilder.addTextBody("momo_number", momo_number.getText().toString());
                multipartEntityBuilder.addTextBody("longitude", String.valueOf(longitude));
                multipartEntityBuilder.addTextBody("latitude", String.valueOf(latitude));
                /*multipartEntityBuilder.addTextBody("street_address", street_address);
                multipartEntityBuilder.addTextBody("digital_address", digital_address);*/
                multipartEntityBuilder.addTextBody("identification_type", identification_type_spinner.getSelectedItem().toString());
                multipartEntityBuilder.addTextBody("identification_number", identification_number.getText().toString());
                if (identification_image_file != null) {
                    multipartEntityBuilder.addPart("identification_image_file", new FileBody(identification_image_file));
                }

                multipartEntityBuilder.addTextBody("user_id", PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + MYUSERID, ""));


                // Progress listener - updates task's progress
                MyHttpEntity.ProgressListener progressListener =
                        progress -> publishProgress((int) progress);

                // POST
                httpPost.setEntity(new MyHttpEntity(multipartEntityBuilder.build(),
                        progressListener));
                httpPost.setHeader("accept", "application/json");
                httpPost.setHeader("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + APITOKEN, ""));


                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();

                int statusCode = httpResponse.getStatusLine().getStatusCode();

                if (statusCode == 200 || statusCode == 201) {
                    // Server response
                    responseString = EntityUtils.toString(httpEntity);
                }
            } catch (UnsupportedEncodingException | ClientProtocolException e) {
                responseString = e.getMessage();
                e.printStackTrace();
                Log.e("UPLOAD", e.getMessage());
            } catch (IOException e) {
                responseString = e.getMessage();
                Log.e("gardes", e.toString());
//                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPreExecute() {
            mProgress.setTitle("Creating Profile.");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(String result) {
            mProgress.dismiss();
            if (result != null) {
                if (result.contains("connect")){
                    Toast.makeText(getApplicationContext(), context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Realm.init(SellerAccountActivity.this);
                        Realm.getInstance(RealmUtility.getDefaultConfig(SellerAccountActivity.this)).executeTransaction(realm -> {
                            try {
                                RealmSeller realmSeller = realm.createOrUpdateObjectFromJson(RealmSeller.class, jsonObject.getJSONObject("seller"));
                                realm.createOrUpdateAllFromJson(RealmBanner.class, jsonObject.getJSONArray("banners"));

                                PreferenceManager
                                        .getDefaultSharedPreferences(SellerAccountActivity.this)
                                        .edit()
                                        .putString("com.ekumfi.juice" + "ROLE", "SELLER")
                                        .putString("com.ekumfi.juice" + "SELLER_ID", realmSeller.getSeller_id())
                                        .apply();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                                            startActivity(new Intent(getApplicationContext(), SellerHomeActivity.class));
                        SelectRoleActivity.selectRoleActivity.finish();
                        finish();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
            else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Update process
            /*progressbar.setProgress(progress[0]);
            statustext.setText(progress[0].toString() + "%  complete");*/
        }
    }

    private class updateSellerAsync extends AsyncTask<Void, Integer, String> {

        HttpClient httpClient = new DefaultHttpClient();
        private Context context;

        private updateSellerAsync(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpResponse httpResponse = null;
            HttpEntity httpEntity = null;
            String responseString = null;
            String URL = API_URL + "sellers/" + seller_id;
            try {
                HttpPost httpPost = new HttpPost(URL);
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

                // Add the file to be uploaded
                if (shop_image_file != null) {
                    multipartEntityBuilder.addPart("shop_image_file", new FileBody(shop_image_file));
                }
                multipartEntityBuilder.addTextBody("shop_name", shop_name.getText().toString().trim());
                multipartEntityBuilder.addTextBody("seller_type", seller_type_spinner.getSelectedItem().toString());

                multipartEntityBuilder.addTextBody("primary_contact", primary_contact.getText().toString());
//                multipartEntityBuilder.addTextBody("auxiliary_contact", auxiliary_contact.getText().toString());
                multipartEntityBuilder.addTextBody("momo_number", momo_number.getText().toString());
                multipartEntityBuilder.addTextBody("longitude", String.valueOf(longitude));
                multipartEntityBuilder.addTextBody("latitude", String.valueOf(latitude));
                /*multipartEntityBuilder.addTextBody("street_address", street_address);
                multipartEntityBuilder.addTextBody("digital_address", digital_address);*/
                multipartEntityBuilder.addTextBody("identification_type", identification_type_spinner.getSelectedItem().toString());
                multipartEntityBuilder.addTextBody("identification_number", identification_number.getText().toString());
                if (identification_image_file != null) {
                    multipartEntityBuilder.addPart("identification_image_file", new FileBody(identification_image_file));
                }
                multipartEntityBuilder.addTextBody("user_id", PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + MYUSERID,  ""));

                // Progress listener - updates task's progress
                MyHttpEntity.ProgressListener progressListener =
                        progress -> publishProgress((int) progress);

                // POST
                httpPost.setEntity(new MyHttpEntity(multipartEntityBuilder.build(),
                        progressListener));
                httpPost.setHeader("accept", "application/json");
                httpPost.setHeader("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + APITOKEN, ""));


                httpResponse = httpClient.execute(httpPost);
                httpEntity = httpResponse.getEntity();

                int statusCode = httpResponse.getStatusLine().getStatusCode();

                if (statusCode == 200 || statusCode == 201) {
                    // Server response
                    responseString = EntityUtils.toString(httpEntity);
                }
            } catch (UnsupportedEncodingException | ClientProtocolException e) {
                responseString = e.getMessage();
                e.printStackTrace();
                Log.e("UPLOAD", e.getMessage());
            } catch (IOException e) {
                responseString = e.getMessage();
                Log.e("gardes", e.toString());
//                e.printStackTrace();
            }

            return responseString;
        }

        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected void onPostExecute(String result) {
            mProgress.dismiss();
            if (result != null) {
                if (result.contains("connect")){
                    Toast.makeText(getApplicationContext(), context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Realm.getInstance(RealmUtility.getDefaultConfig(SellerAccountActivity.this)).executeTransaction(realm -> {
                            realm.createOrUpdateObjectFromJson(RealmSeller.class, jsonObject);
                            Const.showToast(getApplicationContext(), "Successfully saved!");
                            finish();
                        });
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
            else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_occured), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Update process
        }
    }
}
