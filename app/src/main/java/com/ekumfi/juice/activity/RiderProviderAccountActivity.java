package com.ekumfi.juice.activity;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.activity.GetAuthActivity.MYUSERID;
import static com.ekumfi.juice.constants.keyConst.API_URL;
import static com.ekumfi.juice.constants.Const.isNetworkAvailable;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment1.firstName;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment1.gender;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment1.lastName;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment1.otherNames;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment1.profile_image_file;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment1.title;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment2.auxiliarycontact;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment2.digital_address;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment2.dob;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment2.latitude;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment2.longitude;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment2.maritalstatus;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment2.primarycontact;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment2.street_address;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment3.identification_image_file;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment4.vehicle_registration_number;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment4.vehicle_type_spinner;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment5.license_image_file;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment6.reverse_license_image_file;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment7.road_worthy_sticker_image_file;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment8.insurance_sticker_image_file;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment3.identification_number;
import static com.ekumfi.juice.fragment.RiderProviderAccountFragment3.identification_type_spinner;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.greysonparrelli.permiso.Permiso;
import com.greysonparrelli.permiso.PermisoActivity;
import com.ekumfi.juice.R;
import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment1;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment2;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment3;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment4;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment5;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment6;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment7;
import com.ekumfi.juice.fragment.RiderProviderAccountFragment8;
import com.ekumfi.juice.other.MyHttpEntity;
import com.ekumfi.juice.pagerAdapter.RiderAccountPageAdapter;
import com.ekumfi.juice.realm.RealmProvider;
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
public class RiderProviderAccountActivity extends PermisoActivity {

    public static RealmProvider realmProvider = new RealmProvider();
    static Context context;


    boolean close = false;
    NonSwipeableViewPager mViewPager;
    RiderAccountPageAdapter riderAccountPageAdapter;
    FloatingActionButton moveprevious, movenext, done;

    RelativeLayout rootview;
    ProgressBar progressBar;
    String tag1 = "android:switcher:" + R.id.pageques_rider + ":" + 0;
    String tag2 = "android:switcher:" + R.id.pageques_rider + ":" + 1;
    String tag3 = "android:switcher:" + R.id.pageques_rider + ":" + 2;
    String tag4 = "android:switcher:" + R.id.pageques_rider + ":" + 3;
    String tag5 = "android:switcher:" + R.id.pageques_rider + ":" + 4;
    String tag6 = "android:switcher:" + R.id.pageques_rider + ":" + 5;
    String tag7 = "android:switcher:" + R.id.pageques_rider + ":" + 6;
    String tag8 = "android:switcher:" + R.id.pageques_rider + ":" + 7;

    RiderProviderAccountFragment1 tabFrag1;
    RiderProviderAccountFragment2 tabFrag2;
    RiderProviderAccountFragment3 tabFrag3;
    RiderProviderAccountFragment4 tabFrag4;
    RiderProviderAccountFragment5 tabFrag5;
    RiderProviderAccountFragment6 tabFrag6;
    RiderProviderAccountFragment7 tabFrag7;
    RiderProviderAccountFragment8 tabFrag8;

    String provider_id;
    private ProgressDialog mProgress;
    NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        networkReceiver = new NetworkReceiver();
        Permiso.getInstance().setActivity(this);

        setContentView(R.layout.activity_rider_provider_account);
        getSupportActionBar().hide();

        mProgress = new ProgressDialog(this);
        mProgress.setTitle(getString(R.string.updating_profile));
        mProgress.setMessage(getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        rootview = findViewById(R.id.root);

        if (getIntent().getStringExtra("MODE").equals("EDIT")) {
            Realm.init(getApplicationContext());
            realmProvider = Realm.getInstance(RealmUtility.getDefaultConfig(RiderProviderAccountActivity.this)).where(RealmProvider.class).equalTo("provider_id", PreferenceManager.getDefaultSharedPreferences(RiderProviderAccountActivity.this).getString("com.ekumfi.juice" + "PROVIDERID", "")).findFirst();
            if (realmProvider != null) {
                provider_id = realmProvider.getProvider_id();
            }
        }
        progressBar = findViewById(R.id.pbar_pic);
        Realm.init(getApplicationContext());
        riderAccountPageAdapter = new RiderAccountPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.pageques_rider);
        mViewPager.setAdapter(riderAccountPageAdapter);
        mViewPager.setOffscreenPageLimit(7); //posible candidate for bug
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
            tabFrag1 = (RiderProviderAccountFragment1) getSupportFragmentManager().findFragmentByTag(tag1);
            tabFrag2 = (RiderProviderAccountFragment2) getSupportFragmentManager().findFragmentByTag(tag2);
            tabFrag3 = (RiderProviderAccountFragment3) getSupportFragmentManager().findFragmentByTag(tag3);
            tabFrag4 = (RiderProviderAccountFragment4) getSupportFragmentManager().findFragmentByTag(tag4);
            tabFrag5 = (RiderProviderAccountFragment5) getSupportFragmentManager().findFragmentByTag(tag5);
            tabFrag6 = (RiderProviderAccountFragment6) getSupportFragmentManager().findFragmentByTag(tag6);
            tabFrag7 = (RiderProviderAccountFragment7) getSupportFragmentManager().findFragmentByTag(tag7);
            tabFrag8 = (RiderProviderAccountFragment8) getSupportFragmentManager().findFragmentByTag(tag8);

            switch (mViewPager.getCurrentItem()) {
                case 0:
                    if (tabFrag1.validate()) {
                        mViewPager.setCurrentItem(1);
                        moveprevious.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (tabFrag2.validate()) {
                        mViewPager.setCurrentItem(2);
                        moveprevious.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    if (tabFrag3.validate()) {
                        mViewPager.setCurrentItem(3);
                        moveprevious.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    if (tabFrag4.validate()) {
                        mViewPager.setCurrentItem(4);
                        moveprevious.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    if (tabFrag5.validate()) {
                        mViewPager.setCurrentItem(5);
                        moveprevious.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    if (tabFrag6.validate()) {
                        mViewPager.setCurrentItem(6);
                        moveprevious.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    if (tabFrag7.validate()) {
                        mViewPager.setCurrentItem(8);
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
            if (tabFrag8.validate()) {
                if (isNetworkAvailable(RiderProviderAccountActivity.this)) {
                    if (provider_id != null && !provider_id.equals("")) {
                        new updateProviderAsync(getApplicationContext()).execute();
                    } else {
                        new addProviderAsync(getApplicationContext()).execute();
                    }
                } else {
                    Toast.makeText(RiderProviderAccountActivity.this, getString(R.string.internet_connection_is_needed), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RiderProviderAccountActivity.this, getString(R.string.pls_correct_the_errors), Toast.LENGTH_LONG).show();
            }
        });
        moveprevious.setOnClickListener(v -> {
            tabFrag1 = (RiderProviderAccountFragment1) getSupportFragmentManager().findFragmentByTag(tag1);
            tabFrag2 = (RiderProviderAccountFragment2) getSupportFragmentManager().findFragmentByTag(tag2);
            tabFrag3 = (RiderProviderAccountFragment3) getSupportFragmentManager().findFragmentByTag(tag3);
            tabFrag4 = (RiderProviderAccountFragment4) getSupportFragmentManager().findFragmentByTag(tag4);
            tabFrag5 = (RiderProviderAccountFragment5) getSupportFragmentManager().findFragmentByTag(tag5);
            tabFrag6 = (RiderProviderAccountFragment6) getSupportFragmentManager().findFragmentByTag(tag6);
            tabFrag7 = (RiderProviderAccountFragment7) getSupportFragmentManager().findFragmentByTag(tag7);
            tabFrag8 = (RiderProviderAccountFragment8) getSupportFragmentManager().findFragmentByTag(tag8);

            switch (mViewPager.getCurrentItem()) {
                case 7:
                    if (tabFrag7.validate()) {
                        mViewPager.setCurrentItem(6);
                        movenext.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 6:
                    if (tabFrag6.validate()) {
                        mViewPager.setCurrentItem(5);
                        movenext.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    if (tabFrag5.validate()) {
                        mViewPager.setCurrentItem(4);
                        movenext.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    if (tabFrag4.validate()) {
                        mViewPager.setCurrentItem(3);
                        movenext.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    if (tabFrag3.validate()) {
                        mViewPager.setCurrentItem(2);
                        movenext.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    if (tabFrag2.validate()) {
                        mViewPager.setCurrentItem(1);
                        movenext.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please correct the errors.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (tabFrag1.validate()) {
                        mViewPager.setCurrentItem(0);
                        movenext.setVisibility(View.VISIBLE);
                        moveprevious.setVisibility(View.GONE);
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
            RiderProviderAccountActivity.this.onBackPressed();

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

    private class addProviderAsync extends AsyncTask<Void, Integer, String> {

        HttpClient httpClient = new DefaultHttpClient();
        private Context context;

        private addProviderAsync(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpResponse httpResponse = null;
            HttpEntity httpEntity = null;
            String responseString = null;
            String URL = API_URL + "providers";
            try {
                HttpPost httpPost = new HttpPost(URL);
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

                // Add the file to be uploaded
                if (profile_image_file != null) {
                    multipartEntityBuilder.addPart("profile_image_file", new FileBody(profile_image_file));
                }
                multipartEntityBuilder.addTextBody("title", title.getSelectedItem().toString());
                multipartEntityBuilder.addTextBody("first_name", firstName.getText().toString());
                multipartEntityBuilder.addTextBody("last_name", lastName.getText().toString());
                multipartEntityBuilder.addTextBody("other_name", otherNames.getText().toString());
                multipartEntityBuilder.addTextBody("gender", gender.getSelectedItem().toString());

                multipartEntityBuilder.addTextBody("primary_contact", primarycontact.getText().toString());
                multipartEntityBuilder.addTextBody("auxiliary_contact", auxiliarycontact.getText().toString());
                multipartEntityBuilder.addTextBody("dob", dob.getText().toString());
                multipartEntityBuilder.addTextBody("longitude", String.valueOf(longitude));
                multipartEntityBuilder.addTextBody("latitude", String.valueOf(latitude));
                multipartEntityBuilder.addTextBody("street_address", street_address.getText() == null ? "" : street_address.getText().toString());
                multipartEntityBuilder.addTextBody("digital_address", digital_address.getText() == null ? "" : digital_address.getText().toString());
                multipartEntityBuilder.addTextBody("marital_status", maritalstatus.getSelectedItem().toString());

                multipartEntityBuilder.addTextBody("identification_type", identification_type_spinner.getSelectedItem().toString());
                multipartEntityBuilder.addTextBody("identification_number", identification_number.getText().toString());
                if (identification_image_file != null) {
                    multipartEntityBuilder.addPart("identification_image_file", new FileBody(identification_image_file));
                }
                multipartEntityBuilder.addTextBody("vehicle_type", vehicle_type_spinner.getSelectedItem().toString());
                multipartEntityBuilder.addTextBody("vehicle_registration_number", vehicle_registration_number.getText().toString());
                if (license_image_file != null) {
                    multipartEntityBuilder.addPart("license_image_file", new FileBody(license_image_file));
                }
                if (reverse_license_image_file != null) {
                    multipartEntityBuilder.addPart("reverse_license_image_file", new FileBody(reverse_license_image_file));
                }
                if (road_worthy_sticker_image_file != null) {
                    multipartEntityBuilder.addPart("road_worthy_sticker_image_file", new FileBody(road_worthy_sticker_image_file));
                }
                if (insurance_sticker_image_file != null) {
                    multipartEntityBuilder.addPart("insurance_sticker_image_file", new FileBody(insurance_sticker_image_file));
                }
                multipartEntityBuilder.addTextBody("category", "Rider");
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
                        Realm.getInstance(RealmUtility.getDefaultConfig(RiderProviderAccountActivity.this)).executeTransaction(realm -> {
                            realm.createOrUpdateObjectFromJson(RealmProvider.class, jsonObject);
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
            /*progressbar.setProgress(progress[0]);
            statustext.setText(progress[0].toString() + "%  complete");*/
        }
    }

    private class updateProviderAsync extends AsyncTask<Void, Integer, String> {

        HttpClient httpClient = new DefaultHttpClient();
        private Context context;

        private updateProviderAsync(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpResponse httpResponse = null;
            HttpEntity httpEntity = null;
            String responseString = null;
            String URL = API_URL + "providers/" + provider_id;
            try {
                HttpPost httpPost = new HttpPost(URL);
                MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

                // Add the file to be uploaded
                if (profile_image_file != null) {
                    multipartEntityBuilder.addPart("profile_image_file", new FileBody(profile_image_file));
                }
                multipartEntityBuilder.addTextBody("title", title.getSelectedItem().toString());
                multipartEntityBuilder.addTextBody("first_name", firstName.getText().toString());
                multipartEntityBuilder.addTextBody("last_name", lastName.getText().toString());
                multipartEntityBuilder.addTextBody("other_name", otherNames.getText().toString());
                multipartEntityBuilder.addTextBody("gender", gender.getSelectedItem().toString());

                multipartEntityBuilder.addTextBody("primary_contact", primarycontact.getText().toString());
                multipartEntityBuilder.addTextBody("auxiliary_contact", auxiliarycontact.getText().toString());
                multipartEntityBuilder.addTextBody("dob", dob.getText().toString());
                multipartEntityBuilder.addTextBody("longitude", String.valueOf(longitude));
                multipartEntityBuilder.addTextBody("latitude", String.valueOf(latitude));
                multipartEntityBuilder.addTextBody("street_address", street_address.getText() == null ? "" : street_address.getText().toString());
                multipartEntityBuilder.addTextBody("digital_address", digital_address.getText() == null ? "" : digital_address.getText().toString());
                multipartEntityBuilder.addTextBody("marital_status", maritalstatus.getSelectedItem().toString());

                multipartEntityBuilder.addTextBody("identification_type", identification_type_spinner.getSelectedItem().toString());
                multipartEntityBuilder.addTextBody("identification_number", identification_number.getText().toString());
                if (identification_image_file != null) {
                    multipartEntityBuilder.addPart("identification_image_file", new FileBody(identification_image_file));
                }
                multipartEntityBuilder.addTextBody("vehicle_type", vehicle_type_spinner.getSelectedItem().toString());
                multipartEntityBuilder.addTextBody("vehicle_registration_number", vehicle_registration_number.getText().toString());
                if (license_image_file != null) {
                    multipartEntityBuilder.addPart("license_image_file", new FileBody(license_image_file));
                }
                if (reverse_license_image_file != null) {
                    multipartEntityBuilder.addPart("reverse_license_image_file", new FileBody(reverse_license_image_file));
                }
                if (road_worthy_sticker_image_file != null) {
                    multipartEntityBuilder.addPart("road_worthy_sticker_image_file", new FileBody(road_worthy_sticker_image_file));
                }
                if (insurance_sticker_image_file != null) {
                    multipartEntityBuilder.addPart("insurance_sticker_image_file", new FileBody(insurance_sticker_image_file));
                }
                multipartEntityBuilder.addTextBody("category", "Rider");
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
                        Realm.getInstance(RealmUtility.getDefaultConfig(RiderProviderAccountActivity.this)).executeTransaction(realm -> {
                            realm.createOrUpdateObjectFromJson(RealmProvider.class, jsonObject);
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
            /*progressbar.setProgress(progress[0]);
            statustext.setText(progress[0].toString() + "%  complete");*/
        }
    }
}
