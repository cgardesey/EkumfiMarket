package com.ekumfi.juice.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ekumfi.juice.constants.Const;
import com.google.android.material.snackbar.Snackbar;


public class NetworkReceiver extends BroadcastReceiver {

    public static String CONNECTEDTONETWORK = "CONNECTEDTONETWORK";
    public static Activity activeActivity;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcastWithFirebase.
        //throw new UnsupportedOperationException("Not yet implemented");

        boolean networkAvailable = Const.isNetworkAvailable(context);
        Log.d("09876", Boolean.toString(networkAvailable));

        if (networkAvailable) {
            if (activeActivity != null) {
//                versionCheck(activeActivity);
//                guidCheck(activeActivity);

                if (networkAvailable != PreferenceManager.getDefaultSharedPreferences(context).getBoolean("com.ekumfi.juice" + CONNECTEDTONETWORK, false)) {
                    Snackbar.make(activeActivity.findViewById(android.R.id.content), "Connected to internet", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(activeActivity.getResources().getColor(android.R.color.holo_green_dark ))
                            .show();
                }
            }
        }
        else {
            if (activeActivity != null) {
                if (networkAvailable != PreferenceManager.getDefaultSharedPreferences(context).getBoolean("com.ekumfi.juice" + CONNECTEDTONETWORK, false)) {
                    Snackbar.make(activeActivity.findViewById(android.R.id.content), "Disconnected from internet", Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(activeActivity.getResources().getColor(android.R.color.holo_red_dark ))
                            .show();
                }
            }
        }

        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean("com.ekumfi.juice" + CONNECTEDTONETWORK, networkAvailable)
                .apply();
    }
}
