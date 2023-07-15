package com.ekumfi.juice.materialDialog;

import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.activity.GetAuthActivity.MYUSERID;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.R;
import com.ekumfi.juice.constants.keyConst;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmStudent;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

public class PhonenumberMaterialDialog extends DialogFragment {
    public static ProgressDialog dialog1;
    String type, phonenumber;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.list_item_phonenumber,null);
        TextView ok = view.findViewById(R.id.ok);
        TextView number = view.findViewById(R.id.number);
        number.setText(phonenumber);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileno = number.getText().toString();
                if (!TextUtils.isEmpty(mobileno) && !Const.isValidMtnno(mobileno)){
                    Toast.makeText(getActivity(), getString(R.string.invalid_number), Toast.LENGTH_LONG).show();
                }
                else {
                    final RealmStudent[] realmStudent = new RealmStudent[1];
                    Realm.init(getActivity());
                    Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                        realmStudent[0] = realm.where(RealmStudent.class).equalTo("infoid", PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + MYUSERID,  "")).findFirst();
                        realmStudent[0].setPrimarycontact(number.getText().toString().trim());
                    });

                    if (false) {
                    } else {
                        Map<String, String> params = new HashMap<>();
                        params.put("firstname", realmStudent[0].getFirstname());
                        params.put("lastname", realmStudent[0].getLastname());
                        params.put("othername", realmStudent[0].getOthername());
                        params.put("gender", realmStudent[0].getGender());
                        params.put("emailaddress", realmStudent[0].getEmailaddress());
                        params.put("primarycontact", mobileno);

                        ProgressDialog mProgress;
                        mProgress = new ProgressDialog(getActivity());
                        mProgress.setMessage("Updating phonenumber");
                        mProgress.setCancelable(false);
                        mProgress.setIndeterminate(true);
                        mProgress.show();
                        StringRequest stringRequest = new StringRequest(
                                com.android.volley.Request.Method.POST,
                                keyConst.API_URL +"students/" + PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("com.ekumfi.juice" + MYUSERID,  ""),
                                response -> {

                                    mProgress.dismiss();
                                    if (response != null) {
                                        try {
                                            JSONObject jsonObjectResponse = new JSONObject(response);

                                            Realm.init(getActivity());
                                            Realm.getInstance(RealmUtility.getDefaultConfig(getActivity())).executeTransaction(realm -> {
                                                realm.createOrUpdateObjectFromJson(RealmStudent.class, response);
                                                Toast.makeText(getActivity(), "Phonenumber successfully updated!", Toast.LENGTH_SHORT).show();
                                                dismiss();
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                error -> {
                                    mProgress.dismiss();
                                    error.printStackTrace();
                                    Log.d("Cyrilll", error.toString());
                                    Const.myVolleyError(getActivity(), error);
                                }
                        ) {
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
                }
            }
        });
        // doneBtn.setOnClickListener(doneAction);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    }
                });
            }
        }, 5);
        return builder.create();
    }
    
}