package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.activity.GetAuthActivity.MYUSERID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.constants.keyConst;
import com.ekumfi.juice.fragment.ConsumerAccountFragment1;
import com.ekumfi.juice.other.MyHttpEntity;
import com.ekumfi.juice.pagerAdapter.ConsumerAccountPageAdapter;
import com.ekumfi.juice.realm.RealmConsumer;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.ekumfi.juice.util.RealmUtility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.greysonparrelli.permiso.Permiso;
import com.greysonparrelli.permiso.PermisoActivity;
import com.ekumfi.juice.R;

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
public class ConsumerAccountActivity extends PermisoActivity {

    public static RealmConsumer realmConsumer = new RealmConsumer();
    static Context context;


    boolean close = false;
    ViewPager mViewPager;
    ConsumerAccountPageAdapter consumerAccountPageAdapter;
    FloatingActionButton fab;

    RelativeLayout rootview;
    ProgressBar progressBar;
    String tag1 = "android:switcher:" + R.id.pageques_consumer + ":" + 0;
    private ProgressDialog mProgress;
    NetworkReceiver networkReceiver;

    String consumer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        networkReceiver = new NetworkReceiver();
        Permiso.getInstance().setActivity(this);

        setContentView(R.layout.activity_consumer_account);
        getSupportActionBar().hide();

        mProgress = new ProgressDialog(this);
        mProgress.setTitle(getString(R.string.updating_profile));
        mProgress.setMessage(getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        fab = findViewById(R.id.movenext);
        fab.setOnClickListener(v -> sendData());

        rootview = findViewById(R.id.root);

        progressBar = findViewById(R.id.pbar_pic);
        if (getIntent().getStringExtra("MODE").equals("EDIT")) {
            Realm.init(getApplicationContext());
            realmConsumer = Realm.getInstance(RealmUtility.getDefaultConfig(ConsumerAccountActivity.this)).where(RealmConsumer.class).equalTo("consumer_id", PreferenceManager.getDefaultSharedPreferences(ConsumerAccountActivity.this).getString("com.ekumfi.juice" + "CONSUMER_ID", "")).findFirst();
            if (realmConsumer != null) {
                consumer_id = realmConsumer.getConsumer_id();
            }
        }
        consumerAccountPageAdapter = new ConsumerAccountPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.pageques_consumer);
        mViewPager.setAdapter(consumerAccountPageAdapter);
//        mViewPager.setOffscreenPageLimit(1);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
        NetworkReceiver.activeActivity = this;
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

    public void sendData() {

        final ConsumerAccountFragment1 tabFrag1 = (ConsumerAccountFragment1) getSupportFragmentManager().findFragmentByTag(tag1);
//        final AccountFragment2 tabFrag2 = (AccountFragment2) getSupportFragmentManager().findFragmentByTag(tag2);

        Realm.init(context);
        Realm.getInstance(RealmUtility.getDefaultConfig(ConsumerAccountActivity.this)).executeTransaction(realm -> {
            if (tabFrag1 != null) {

                if (tabFrag1.validate()) {
                    if (Const.isNetworkAvailable(ConsumerAccountActivity.this)) {
                        new updateConsumerAsync(getApplicationContext()).execute();
                    } else {
                        Toast.makeText(ConsumerAccountActivity.this, getString(R.string.internet_connection_is_needed), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ConsumerAccountActivity.this, getString(R.string.pls_correct_the_errors), Toast.LENGTH_LONG).show();
                }
            }
        });
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
            ConsumerAccountActivity.this.onBackPressed();

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

    private class updateConsumerAsync extends AsyncTask<Void, Integer, String> {

        HttpClient httpClient = new DefaultHttpClient();
        private Context context;

        private updateConsumerAsync(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {

            HttpResponse httpResponse = null;
            HttpEntity httpEntity = null;
            String responseString = null;
            String URL = keyConst.API_URL + "consumers/" + consumer_id;
            try {
                HttpPost httpPost = new HttpPost(URL);
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

                // Add the file to be uploaded
                if (ConsumerAccountFragment1.profile_pic_file != null) {
                    multipartEntityBuilder.addPart("profile_image_file", new FileBody(ConsumerAccountFragment1.profile_pic_file));
                }
                String firstname = ConsumerAccountFragment1.name.getText().toString();
                multipartEntityBuilder.addTextBody("name", ConsumerAccountFragment1.name.getText().toString().trim());
//                multipartEntityBuilder.addTextBody("gender", ConsumerAccountFragment1.gender.getSelectedItem().toString());
//                multipartEntityBuilder.addTextBody("employment_category", ConsumerAccountFragment1.employment_category.getSelectedItem().toString());
                multipartEntityBuilder.addTextBody("primary_contact", ConsumerAccountFragment1.primarycontact.getText().toString());
//                multipartEntityBuilder.addTextBody("auxiliary_contact", ConsumerAccountFragment1.auxiliarycontact.getText().toString());
                multipartEntityBuilder.addTextBody("longitude", String.valueOf(ConsumerAccountFragment1.longitude));
                multipartEntityBuilder.addTextBody("latitude", String.valueOf(ConsumerAccountFragment1.latitude));
                /*multipartEntityBuilder.addTextBody("street_address", street_address);
                multipartEntityBuilder.addTextBody("digital_address", digital_address);*/
                multipartEntityBuilder.addTextBody("user_id", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + MYUSERID,  ""));

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
                    Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Realm.getInstance(RealmUtility.getDefaultConfig(ConsumerAccountActivity.this)).executeTransaction(realm -> {
                            realm.createOrUpdateObjectFromJson(RealmConsumer.class, jsonObject);
                            Const.showToast(getApplicationContext(), context.getString(R.string.successfully_updated));
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_CANCELED, returnIntent);
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
            /*progressbar.setProgress(progress[0]);
            statustext.setText(progress[0].toString() + "%  complete");*/
        }
    }

    private class addConsumerAsync extends AsyncTask<Void, Integer, String> {

        HttpClient httpClient = new DefaultHttpClient();
        private Context context;

        private addConsumerAsync(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {

            HttpResponse httpResponse = null;
            HttpEntity httpEntity = null;
            String responseString = null;
            String URL = keyConst.API_URL + "consumers/";
            try {
                HttpPost httpPost = new HttpPost(URL);
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

                // Add the file to be uploaded
                if (ConsumerAccountFragment1.profile_pic_file != null) {
                    multipartEntityBuilder.addPart("profile_image_file", new FileBody(ConsumerAccountFragment1.profile_pic_file));
                }
                String firstname = ConsumerAccountFragment1.name.getText().toString();
                multipartEntityBuilder.addTextBody("name", ConsumerAccountFragment1.name.getText().toString().trim());
//                multipartEntityBuilder.addTextBody("gender", ConsumerAccountFragment1.gender.getSelectedItem().toString());
//                multipartEntityBuilder.addTextBody("employment_category", ConsumerAccountFragment1.employment_category.getSelectedItem().toString());
                multipartEntityBuilder.addTextBody("primary_contact", ConsumerAccountFragment1.primarycontact.getText().toString());
//                multipartEntityBuilder.addTextBody("auxiliary_contact", ConsumerAccountFragment1.auxiliarycontact.getText().toString());
                multipartEntityBuilder.addTextBody("longitude", String.valueOf(ConsumerAccountFragment1.longitude));
                multipartEntityBuilder.addTextBody("latitude", String.valueOf(ConsumerAccountFragment1.latitude));
                /*multipartEntityBuilder.addTextBody("street_address", street_address);
                multipartEntityBuilder.addTextBody("digital_address", digital_address);*/
                multipartEntityBuilder.addTextBody("user_id", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("com.ekumfi.juice" + MYUSERID,  ""));

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
                    Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        Realm.getInstance(RealmUtility.getDefaultConfig(ConsumerAccountActivity.this)).executeTransaction(realm -> {
                            realm.createOrUpdateObjectFromJson(RealmConsumer.class, jsonObject);
                            Const.showToast(getApplicationContext(), context.getString(R.string.successfully_updated));
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_CANCELED, returnIntent);
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
            /*progressbar.setProgress(progress[0]);
            statustext.setText(progress[0].toString() + "%  complete");*/
        }
    }
}
