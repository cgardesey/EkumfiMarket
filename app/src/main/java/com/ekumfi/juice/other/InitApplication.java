package com.ekumfi.juice.other;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ekumfi.juice.util.Socket;


/**
 * Created by anupamchugh on 01/03/18.
 */

public class InitApplication extends MultiDexApplication {
    public static final String NIGHT_MODE = "NIGHT_MODE";
    public static final String TAG = MyApplication.class
            .getSimpleName();
    private static MyApplication mInstance;
    private static InitApplication singleton = null;
    private boolean isNightModeEnabled = false;
    public RequestQueue mRequestQueue;
    private Socket socket;
    public static String versionName;

    public static InitApplication getInstance() {

        if (singleton == null) {
            singleton = new InitApplication();
        }
        return singleton;
    }

    @Override

    public void onCreate() {
        super.onCreate();
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            Log.d("sadf-08", versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        singleton = this;
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.isNightModeEnabled = mPrefs.getBoolean("com.ekumfi.juice" + NIGHT_MODE, false);
    }

    public boolean isNightModeEnabled() {
        return isNightModeEnabled;
    }

    public void setIsNightModeEnabled(boolean isNightModeEnabled) {
        this.isNightModeEnabled = isNightModeEnabled;

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean("com.ekumfi.juice" + NIGHT_MODE, isNightModeEnabled);
        editor.apply();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
