package com.ekumfi.juice.fragment;

import static com.ekumfi.juice.activity.ConsumerAccountActivity.realmConsumer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ApplicationVersionSignature;
import com.ekumfi.juice.activity.MapsActivity;
import com.ekumfi.juice.activity.PictureActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.greysonparrelli.permiso.Permiso;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;
import com.noelchew.multipickerwrapper.library.MultiPickerWrapper;
import com.noelchew.multipickerwrapper.library.ui.MultiPickerWrapperSupportFragment;
import com.ekumfi.juice.R;
import com.ekumfi.juice.constants.Const;
import com.ekumfi.juice.util.PixelUtil;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 2CLearning on 12/13/2017.
 */

public class ConsumerAccountFragment1 extends MultiPickerWrapperSupportFragment {
    public static final String PICTURE_TYPE = "PICTURE_TYPE";
    public static final String TYPE_PROFILE_PIC = "TYPE_PROFILE_PIC";
    private static final String TAG = "AccountFragment1";

    public static double longitude = 0.0d;
    public static double latitude = 0.0d;

    public RoundedImageView profilePic;
    Context mContext;
    LinearLayout controls;
    RelativeLayout google_location_layout;
    public static File profile_pic_file = null;
    MultiPickerWrapper.PickerUtilListener multiPickerWrapperListener = new MultiPickerWrapper.PickerUtilListener() {
        @Override
        public void onPermissionDenied() {
            // do something here
        }

        @Override
        public void onImagesChosen(List<ChosenImage> list) {
            controls.setVisibility(View.GONE);
            String imagePath = list.get(0).getOriginalPath();
            profilePic.setImageBitmap(BitmapFactory.decodeFile(imagePath));

            profile_pic_file = new File(list.get(0).getOriginalPath());
        }

        @Override
        public void onVideosChosen(List<ChosenVideo> list) {
            Const.showToast(getContext(), mContext.getString(R.string.unsupported_file_format));
        }

        @Override
        public void onError(String s) {
            Toast.makeText(getContext(), getString(R.string.error_choosing_image), Toast.LENGTH_SHORT).show();
            Log.d(TAG, s);
        }
    };
    public static EditText name, primarycontact, auxiliarycontact;
    public static Spinner gender, employment_category;
    private FloatingActionButton addimage, gal, cam;
    public static TextView google_location;
    public static String street_address = "", digital_address = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();

        final View rootView = inflater.inflate(R.layout.fragment_consumer_account1, container, false);
        profilePic = rootView.findViewById(R.id.profile_imgview);
        name = rootView.findViewById(R.id.name);
        primarycontact = rootView.findViewById(R.id.primary_contact);
        auxiliarycontact = rootView.findViewById(R.id.auxiliary_contact);

        google_location_layout = rootView.findViewById(R.id.google_location_layout);

        google_location = rootView.findViewById(R.id.google_location);

        gender = rootView.findViewById(R.id.gender_spinner);
        employment_category = rootView.findViewById(R.id.employment_category);
        addimage = rootView.findViewById(R.id.addimage);
        controls = rootView.findViewById(R.id.add);
        gal = rootView.findViewById(R.id.gal);
        cam = rootView.findViewById(R.id.cam);


        addimage.setOnClickListener(v -> {
            if (controls.getVisibility() == View.VISIBLE) {
                controls.setVisibility(View.GONE);

            } else {
                controls.setVisibility(View.VISIBLE);
            }
        });
        gal.setOnClickListener(v -> multiPickerWrapper.getPermissionAndPickSingleImageAndCrop(imgOptions(), 1, 1));
        cam.setOnClickListener(v -> multiPickerWrapper.getPermissionAndTakePictureAndCrop(imgOptions(), 1, 1));

        profilePic.setOnClickListener(view -> {
            if (profilePic.getDrawable() == null) {
                //Toast.makeText(mContext, getString(R.string.image_not_set), Toast.LENGTH_SHORT).show();
            } else {
                PictureActivity.idPicBitmap = ((RoundedDrawable) profilePic.getDrawable()).getSourceBitmap();
                Intent intent = new Intent(getActivity(), PictureActivity.class);
                intent.putExtra(PICTURE_TYPE, TYPE_PROFILE_PIC);
                getActivity().startActivity(intent);
            }
        });

        google_location_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                                                             @Override
                                                             public void onPermissionResult(Permiso.ResultSet resultSet) {
                                                                 if (resultSet.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && resultSet.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {

                                                                     startActivityForResult(new Intent(getContext(), MapsActivity.class)
                                                                                     .putExtra("LONGITUDE", realmConsumer.getLongitude())
                                                                                     .putExtra("LATITUDE", realmConsumer.getLatitude())
                                                                                     .putExtra("BUTTON_TEXT", "CONFIRM LOCATION")
                                                                             , MapsActivity.RC_CONFIRM_LOCATION);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // txtData = (TextView)view.findViewById(R.id.txtData);
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

    @Override
    protected MultiPickerWrapper.PickerUtilListener getMultiPickerWrapperListener() {
        return multiPickerWrapperListener;
    }

    public void init() {
        if (realmConsumer != null) {
            String picture = realmConsumer.getProfile_image_url();
            boolean pictureExists = picture != null;
            if (pictureExists) {
                Glide.with(getContext())
                        .load(realmConsumer.getProfile_image_url())
                        .apply(new RequestOptions().centerCrop())
                        .apply(RequestOptions.signatureOf(ApplicationVersionSignature.obtain(getContext())))
                        .into(profilePic);
            } else {
                profilePic.setImageBitmap(null);
            }
            name.setText(realmConsumer.getName());
            primarycontact.setText(realmConsumer.getPrimary_contact());
            DecimalFormat formatter = new DecimalFormat("#0.00");
            if (realmConsumer.getLatitude() != 0.0d && realmConsumer.getLongitude() != 0.0d) {
                google_location.setText(formatter.format(realmConsumer.getLatitude()) + ", " + formatter.format(realmConsumer.getLongitude()));
            }
            longitude = realmConsumer.getLongitude();
            latitude = realmConsumer.getLatitude();
            street_address = realmConsumer.getStreet_address();
            digital_address = realmConsumer.getDigital_address();
        }
    }

    public boolean validate() {
        boolean validated = true;

        if (TextUtils.isEmpty(name.getText())) {
            name.setError(getString(R.string.error_field_required));
            validated = false;
        }
        /*if (gender.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) gender.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        }*/
        /*if (employment_category.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) employment_category.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            validated = false;
        }*/
        String phonenumber = primarycontact.getText().toString();
        if (!(phonenumber.length() == 10 && phonenumber.charAt(0) == '0')) {
            primarycontact.setError("Invalid number");
            validated = false;
        }

        /*String auxphonenumber = auxiliarycontact.getText().toString();
        if (!auxphonenumber.equals("") && !(auxphonenumber.length() == 10 && auxphonenumber.charAt(0) == '0')) {
            auxiliarycontact.setError("Invalid number");
            validated = false;
        }*/

        if (TextUtils.isEmpty(google_location.getText().toString())) {
            google_location.setError(getString(R.string.error_field_required));
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