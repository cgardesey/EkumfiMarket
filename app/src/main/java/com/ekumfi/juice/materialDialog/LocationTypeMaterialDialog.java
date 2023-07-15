package com.ekumfi.juice.materialDialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.ekumfi.juice.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nana on 10/22/2017.
 */

public class LocationTypeMaterialDialog extends DialogFragment {

    Button current_location, live_location;
    ImageView cancel;

    LocationTypeMaterialDialog locationTypeMaterialDialog;


    private static final String TAG = "LocationTypeMaterialDialog";


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.list_item_location_type, null);

        locationTypeMaterialDialog = LocationTypeMaterialDialog.this;
        cancel = view.findViewById(R.id.cancel);
        current_location = view.findViewById(R.id.current_location);
        live_location = view.findViewById(R.id.live_location);
        cancel = view.findViewById(R.id.cancel);

        current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationTypeMDInterface activity = (LocationTypeMDInterface) getActivity();
                activity.onCurrentLocationClick();
            }
        });

        live_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationTypeMDInterface activity = (LocationTypeMDInterface) getActivity();
                activity.onLiveLocationClick();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        //  builder.setCancelable(false);
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // If you want to modify a view in your Activity
                getActivity().runOnUiThread(() -> getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)));
            }
        }, 5);
        return builder.create();
    }

    public interface LocationTypeMDInterface {
        public void onCurrentLocationClick();
        public void onLiveLocationClick();
    }
}