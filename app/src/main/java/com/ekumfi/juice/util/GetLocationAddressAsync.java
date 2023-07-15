package com.ekumfi.juice.util;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

public class GetLocationAddressAsync extends AsyncTask<String, Void, List<Address>> {

    Activity activity;
    OnTaskCompletedInterface onTaskCompletedInterface;
    OnTaskCancelledInterface onTaskCancelledInterface;
    Double latitude;
    Double longitude;

    public GetLocationAddressAsync(Activity activity, Double latitude, Double longitude, OnTaskCompletedInterface onTaskCompletedInterface, OnTaskCancelledInterface onTaskCancelledInterface) {
        this.onTaskCompletedInterface = onTaskCompletedInterface;
        this.onTaskCancelledInterface = onTaskCancelledInterface;
        this.activity = activity;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected List<Address> doInBackground(String... locationName) {
        // Creating an instance of Geocoder class
        Geocoder geocoder = new Geocoder(activity);
        List<Address> addresses = null;

        try {
            // Getting a maximum of 3 Address that matches the input text
            addresses = geocoder.getFromLocation(latitude, longitude, 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    @Override
    protected void onPostExecute(List<Address> addresses) {
        super.onPostExecute(addresses);
        onTaskCompletedInterface.onTaskCompleted(addresses);
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
        onTaskCancelledInterface.onTaskCancelled();
    }

    public interface OnTaskCompletedInterface {
        void onTaskCompleted(List<Address> addresses);
    }

    public interface OnTaskCancelledInterface {
        void onTaskCancelled();
    }
}
