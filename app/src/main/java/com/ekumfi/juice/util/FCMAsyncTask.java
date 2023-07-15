package com.ekumfi.juice.util;

import static com.ekumfi.juice.activity.GetAuthActivity.ACCESSTOKEN;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class FCMAsyncTask extends AsyncTask<Void, Integer, String> {


    private Context context;
    private Exception exception;
    // private ProgressDialog progressDialog;
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

    public FCMAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        String access_token = "";
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getAssets().openFd("service-account.json");
            FileInputStream serviceAccount = fileDescriptor.createInputStream();

            GoogleCredential googleCredential = GoogleCredential
                    .fromStream(serviceAccount)
                    .createScoped(Arrays.asList(SCOPES));
            googleCredential.refreshToken();
            access_token  = googleCredential.getAccessToken();
            return access_token;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {

        // Init and show dialog

    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("Engineer:bbbbaaerre", "" + result);
        if (result != null) {
            PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .edit()
                    .putString("com.ekumfi.juice" + ACCESSTOKEN, result)
                    .apply();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // Update process

    }
}

