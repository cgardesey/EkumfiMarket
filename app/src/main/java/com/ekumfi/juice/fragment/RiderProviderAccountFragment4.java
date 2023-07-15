package com.ekumfi.juice.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ekumfi.juice.activity.RiderProviderAccountActivity;
import com.ekumfi.juice.R;
import com.ekumfi.juice.util.PixelUtil;
import com.yalantis.ucrop.UCrop;

import java.io.File;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class RiderProviderAccountFragment4 extends Fragment {
    public static final String PICTURE_TYPE = "PICTURE_TYPE";
    public static final String TYPE_ID_PIC = "TYPE_ID_PIC";
    private static final String TAG = "PersonalProviderAccountFragment4";
    Context mContext;
    LinearLayout controls;
    TextView image_not_set;
    public static File identification_image_file = null;
    public static Spinner vehicle_type_spinner;
    public static EditText vehicle_registration_number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        final View rootView = inflater.inflate(R.layout.fragment_rider_provider_account4, container, false);
        vehicle_type_spinner = rootView.findViewById(R.id.vehicle_type_spinner);
        vehicle_registration_number = rootView.findViewById(R.id.vehicle_registration_number);
        controls = rootView.findViewById(R.id.add);
        image_not_set = rootView.findViewById(R.id.image_not_set);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // txtData = (TextView)view.findViewById(R.id.txtData);
    }

    public void init() {
        if (RiderProviderAccountActivity.realmProvider != null) {
            vehicle_type_spinner.setSelection(((ArrayAdapter) vehicle_type_spinner.getAdapter()).getPosition(RiderProviderAccountActivity.realmProvider.getVehicle_type()));
            vehicle_registration_number.setText(RiderProviderAccountActivity.realmProvider.getIdentification_number());
        }
    }

    public boolean validate() {
        boolean validated = true;
        if (TextUtils.isEmpty(vehicle_registration_number.getText())) {
            vehicle_registration_number.setError(getString(R.string.error_field_required));
            validated = false;
        }
        if (vehicle_type_spinner.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) vehicle_type_spinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        }
        return validated;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private UCrop.Options imgOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        options.setCropFrameColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        options.setCropFrameStrokeWidth(PixelUtil.dpToPx(getContext(), 4));
        options.setCropGridColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        options.setCropGridStrokeWidth(PixelUtil.dpToPx(getContext(), 2));
        options.setActiveWidgetColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        options.setToolbarTitle(getString(R.string.crop_image));

        // set rounded cropping guide
        options.setCircleDimmedLayer(true);
        return options;
    }
}