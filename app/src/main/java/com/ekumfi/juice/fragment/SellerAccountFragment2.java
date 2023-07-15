package com.ekumfi.juice.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.ekumfi.juice.activity.MapsActivity;
import com.ekumfi.juice.activity.SellerAccountActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.greysonparrelli.permiso.Permiso;
import com.ekumfi.juice.R;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class SellerAccountFragment2 extends Fragment implements DatePickerDialog.OnDateSetListener {
    private static final String TAG = "PersonalProviderAccountFragment2";

    public static double longitude = 0.0d;
    public static double latitude = 0.0d;

    public static EditText momo_number, primary_contact, auxiliary_contact;
    public static TextView google_location;
    RelativeLayout google_location_layout;
    CardView cardView;
    Context mContext;
    SimpleDateFormat simpleDateFormat;
    public static String street_address = "", digital_address = "";
    public static int PLACE_PICKER_REQUEST = 100;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_seller_account2, container, false);

        primary_contact = rootView.findViewById(R.id.primary_contact);
        auxiliary_contact = rootView.findViewById(R.id.auxiliary_contact);
        momo_number = rootView.findViewById(R.id.momo_number);

        mContext = getContext();
        google_location = rootView.findViewById(R.id.google_location);
        google_location_layout = rootView.findViewById(R.id.google_location_layout);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        google_location_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                                                             @Override
                                                             public void onPermissionResult(Permiso.ResultSet resultSet) {
                                                                 if (resultSet.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && resultSet.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {

                                                                     if (SellerAccountActivity.realmSeller != null && SellerAccountActivity.realmSeller.getSeller_id() != null && !SellerAccountActivity.realmSeller.getSeller_id().equals("")) {
                                                                         startActivityForResult(new Intent(getContext(), MapsActivity.class)
                                                                                         .putExtra("LONGITUDE", SellerAccountActivity.realmSeller.getLongitude())
                                                                                         .putExtra("LATITUDE", SellerAccountActivity.realmSeller.getLatitude())
                                                                                         .putExtra("BUTTON_TEXT", "CONFIRM LOCATION")
                                                                                 , MapsActivity.RC_CONFIRM_LOCATION);
                                                                     } else {
                                                                         startActivityForResult(new Intent(getContext(), MapsActivity.class)
                                                                                         .putExtra("BUTTON_TEXT", "CONFIRM LOCATION")
                                                                                 , MapsActivity.RC_CONFIRM_LOCATION);
                                                                     }
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
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // txtData = (TextView)view.findViewById(R.id.txtData);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MapsActivity.RC_CONFIRM_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null) {
                            longitude = data.getDoubleExtra("LONGITUDE", 0.0d);
                            latitude = data.getDoubleExtra("LATITUDE", 0.0d);
                            if (longitude != 0.0d && latitude != 0.0d) {
                                DecimalFormat formatter = new DecimalFormat("#0.00");
                                google_location.setError(null);
                                google_location.setText(formatter.format(latitude) + ", " + formatter.format(longitude));
                                /*ProgressDialog dialog = new ProgressDialog(getActivity());
                                dialog.setMessage("Please wait...");
                                dialog.setCancelable(false);
                                dialog.setIndeterminate(true);
                                dialog.show();

                                StringRequest stringRequest = new StringRequest(
                                        com.android.volley.Request.Method.POST,
                                        "https://ghanapostgps.sperixlabs.org/get-address",
                                        response -> {
                                            dialog.dismiss();
                                            if (response != null) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    if (jsonObject.getBoolean("found")) {
                                                        digital_address = jsonObject.getJSONObject("data").getJSONArray("Table").getJSONObject(0).getString("GPSName");
                                                        street_address = jsonObject.getJSONObject("data").getJSONArray("Table").getJSONObject(0).getString("Street");
                                                    } else {
                                                        digital_address = "";
                                                        street_address = "";
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
                                        params.put("lat", String.valueOf(latitude));
                                        params.put("long", String.valueOf(longitude));
                                        return params;
                                    }

                                    @Override
                                    public Map getHeaders() throws AuthFailureError {
                                        HashMap headers = new HashMap();
                                        headers.put("Content-Type", "application/x-www-form-urlencoded");
                                        return headers;
                                    }
                                };
                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                        0,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                InitApplication.getInstance().addToRequestQueue(stringRequest);*/
                            } else {
                                google_location.setText("");
                            }

                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        // some stuff that will happen if there's no result
                        break;
                }
                break;

            default:
                break;

        }
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

    }

    @VisibleForTesting
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        new SpinnerDatePickerDialogBuilder()
                .context(getContext())
                .callback(this)
                .spinnerTheme(R.style.NumberPickerStyle)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .build()
                .show();
    }

    public void init() {
        if (SellerAccountActivity.realmSeller != null) {
            primary_contact.setText(SellerAccountActivity.realmSeller.getPrimary_contact());
//            auxiliary_contact.setText(SellerAccountActivity.realmSeller.getAuxiliary_contact());
            momo_number.setText(SellerAccountActivity.realmSeller.getMomo_number());
            DecimalFormat formatter = new DecimalFormat("#0.00");
            if (SellerAccountActivity.realmSeller.getLatitude() != 0.0d && SellerAccountActivity.realmSeller.getLongitude() != 0.0d) {
                google_location.setText(formatter.format(SellerAccountActivity.realmSeller.getLatitude()) + ", " + formatter.format(SellerAccountActivity.realmSeller.getLongitude()));
            }
            longitude = SellerAccountActivity.realmSeller.getLongitude();
            latitude = SellerAccountActivity.realmSeller.getLatitude();
            street_address = SellerAccountActivity.realmSeller.getStreet_address() == null ? "" : SellerAccountActivity.realmSeller.getStreet_address();
            digital_address = SellerAccountActivity.realmSeller.getDigital_address() == null ? "" : SellerAccountActivity.realmSeller.getDigital_address();
        }
    }

    public boolean validate() {
        boolean validated = true;

        String phonenumber = primary_contact.getText().toString();
        if (!(phonenumber.length() == 10 && phonenumber.charAt(0) == '0')) {
            primary_contact.setError("Invalid number");
            validated = false;
        }

        /*String auxphonenumber = auxiliary_contact.getText().toString();
        if (!auxphonenumber.equals("") && !(auxphonenumber.length() == 10 && auxphonenumber.charAt(0) == '0')) {
            auxiliary_contact.setError("Invalid number");
            validated = false;
        }*/
        String momonumber = momo_number.getText().toString();
        if (
                !(
                        momonumber.length() == 10 && (momonumber.startsWith("024") || momonumber.startsWith("054") || momonumber.startsWith("055") || momonumber.startsWith("059"))
                )
        ) {
            momo_number.setError("Invalid number");
            validated = false;
        }
        if (TextUtils.isEmpty(google_location.getText().toString())) {
            google_location.setError(getString(R.string.error_field_required));
            validated = false;
        }

        return validated;
    }
}
