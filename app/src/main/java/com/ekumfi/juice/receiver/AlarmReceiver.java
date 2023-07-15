package com.ekumfi.juice.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.ekumfi.juice.R;
import com.ekumfi.juice.activity.MessageActivity;
import com.ekumfi.juice.other.InitApplication;
import com.ekumfi.juice.realm.RealmChat;
import com.ekumfi.juice.realm.RealmConsumer;
import com.ekumfi.juice.realm.RealmEkumfiInfo;
import com.ekumfi.juice.realm.RealmSeller;
import com.ekumfi.juice.util.RealmUtility;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.ekumfi.juice.activity.GetAuthActivity.APITOKEN;
import static com.ekumfi.juice.constants.keyConst.API_URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andy on 11/8/2019.
 */


public class AlarmReceiver extends BroadcastReceiver {

    private static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    private static final String CHANNEL_NAME = "NOTIFICATION_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Remember in the SetAlarm file we made an intent to this, this is way this work, otherwise you would have to put an action
        /*Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);*/


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // Importance applicable to all the notifications in this Channel
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        // Notification channel should only be created for devices running Android 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
            //Boolean value to set if lights are enabled for Notifications from this Channel
            notificationChannel.enableLights(true);
            //Boolean value to set if vibration are enabled for Notifications from this Channel
            notificationChannel.enableVibration(true);
            //Sets the color of Notification Light
            notificationChannel.setLightColor(Color.GREEN);
            //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
            notificationChannel.setVibrationPattern(new long[]{500, 500, 500, 500, 500});
            notificationManager.createNotificationChannel(notificationChannel);
            //Sets whether notifications from these Channel should be visible on Lockscreen or not
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_splash);
        String type = intent.getStringExtra("TYPE");
        String title = "";
        String body = "";

        switch (type) {
            case "chat":
                title = intent.getStringExtra("NAME");
                body = intent.getStringExtra("BODY");
                break;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_splash)
                .setContentTitle(title)
                .setContentText(body)
                .setLargeIcon(icon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);

        final Intent[] notificationIntent = {null};

        switch (type) {
            case "chat":
                String role = PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + "ROLE", "");
                if (role.equals("SELLER")) {
                    if (intent.getStringExtra("CONSUMER_ID").equals("")) {
                        final String[] ekumfi_name = new String[1];
                        final String[] profile_image_url = new String[1];
                        String consumer_id = intent.getStringExtra("CONSUMER_ID");

                        final RealmEkumfiInfo[] ekumfiInfos = {Realm.getInstance(RealmUtility.getDefaultConfig(context)).where(RealmEkumfiInfo.class).findFirst()};
                        ekumfi_name[0] = intent.getStringExtra("NAME");
                        profile_image_url[0] = intent.getStringExtra("PROFILE_IMAGE_URL");

                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                API_URL + "ekumfi-chat-data",
                                response -> {
                                    if (response != null) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Realm.init(context);
                                            Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
                                                try {
                                                    ekumfiInfos[0] = realm.createOrUpdateObjectFromJson(RealmEkumfiInfo.class, jsonObject.getJSONObject("ekumfi_info"));
                                                    realm.createOrUpdateAllFromJson(RealmChat.class, jsonObject.getJSONArray("chats"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                ekumfi_name[0] = ekumfiInfos[0].getName();
                                                profile_image_url[0] = ekumfiInfos[0].getProfile_image_url();
                                            });

                                            notificationIntent[0] = new  Intent(context, MessageActivity.class)
                                                    .putExtra("CONSUMER_ID", consumer_id)
                                                    .putExtra("EKUMFI_NAME", ekumfi_name[0])
                                                    .putExtra("PROFILE_IMAGE_URL", profile_image_url[0]);

                                            notificationIntent[0].addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            PendingIntent contentIntent = PendingIntent.getActivity(context, 1000, notificationIntent[0],
                                                    PendingIntent.FLAG_UPDATE_CURRENT);
                                            builder.setContentIntent(contentIntent);
                                            // Add as notification
                                            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                            manager.notify(1000, builder.build());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                error -> {
                                    error.printStackTrace();
                                    notificationIntent[0] = new  Intent(context, MessageActivity.class)
                                            .putExtra("CONSUMER_ID", consumer_id)
                                            .putExtra("CONSUMER_NAME", ekumfi_name[0])
                                            .putExtra("PROFILE_IMAGE_URL", profile_image_url[0]);

                                    notificationIntent[0].addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    PendingIntent contentIntent = PendingIntent.getActivity(context, 1000, notificationIntent[0],
                                            PendingIntent.FLAG_UPDATE_CURRENT);
                                    builder.setContentIntent(contentIntent);
                                    // Add as notification
                                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                    manager.notify(1000, builder.build());
                                    Log.d("Cyrilll", error.toString());
                                }
                        ) {
                            @Override
                            public Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("consumer_id", consumer_id);
                                params.put("seller_id", PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + "SELLER_ID", ""));
                                Realm.init(context);
                                Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
                                    RealmResults<RealmChat> results = realm.where(RealmChat.class)
                                            .sort("id", Sort.DESCENDING)
                                            .equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + "SELLER_ID", ""))
                                            .equalTo("consumer_id", consumer_id)
                                            .findAll();
                                    ArrayList<RealmChat> myArrayList = new ArrayList<>();
                                    for (RealmChat realmChat : results) {
                                        if (!(realmChat.getChat_id().startsWith("z"))) {
                                            myArrayList.add(realmChat);
                                        }
                                    }
                                    if (results.size() < 3) {
                                        params.put("id", "0");
                                    }
                                    else{
                                        params.put("id", String.valueOf(myArrayList.get(0).getId()));
                                    }
                                });
                                return params;
                            }
                            /** Passing some request headers* */
                            @Override
                            public Map getHeaders() throws AuthFailureError {
                                HashMap headers = new HashMap();
                                headers.put("accept", "application/json");
                                headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + APITOKEN, ""));
                                return headers;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                0,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        InitApplication.getInstance().addToRequestQueue(stringRequest);
                    } else {
                        final String[] consumer_name = new String[1];
                        final String[] profile_image_url = new String[1];
                        String consumer_id = intent.getStringExtra("CONSUMER_ID");

                        Realm.init(context);
                        final RealmConsumer[] realmConsumer = {Realm.getInstance(RealmUtility.getDefaultConfig(context)).where(RealmConsumer.class).equalTo("consumer_id", consumer_id).findFirst()};
                        consumer_name[0] = realmConsumer[0].getName();
                        profile_image_url[0] = realmConsumer[0].getProfile_image_url();

                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                API_URL + "seller-chat-data",
                                response -> {
                                    if (response != null) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Realm.init(context);
                                            Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
                                                try {
                                                    realmConsumer[0] = realm.createOrUpdateObjectFromJson(RealmConsumer.class, jsonObject.getJSONObject("consumer"));
                                                    realm.createOrUpdateAllFromJson(RealmChat.class, jsonObject.getJSONArray("chats"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                consumer_name[0] = realmConsumer[0].getName();
                                                profile_image_url[0] = realmConsumer[0].getProfile_image_url();
                                            });

                                            notificationIntent[0] = new  Intent(context, MessageActivity.class)
                                                    .putExtra("CONSUMER_ID", consumer_id)
                                                    .putExtra("CONSUMER_NAME", consumer_name[0])
                                                    .putExtra("PROFILE_IMAGE_URL", profile_image_url[0]);

                                            notificationIntent[0].addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                            PendingIntent contentIntent = PendingIntent.getActivity(context, 1000, notificationIntent[0],
                                                    PendingIntent.FLAG_UPDATE_CURRENT);
                                            builder.setContentIntent(contentIntent);
                                            // Add as notification
                                            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                            manager.notify(1000, builder.build());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                error -> {
                                    error.printStackTrace();
                                    notificationIntent[0] = new  Intent(context, MessageActivity.class)
                                            .putExtra("CONSUMER_ID", consumer_id)
                                            .putExtra("CONSUMER_NAME", consumer_name[0])
                                            .putExtra("PROFILE_IMAGE_URL", profile_image_url[0]);
                                    notificationIntent[0].addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    PendingIntent contentIntent = PendingIntent.getActivity(context, 1000, notificationIntent[0],
                                            PendingIntent.FLAG_UPDATE_CURRENT);
                                    builder.setContentIntent(contentIntent);
                                    // Add as notification
                                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                    manager.notify(1000, builder.build());
                                    Log.d("Cyrilll", error.toString());
                                }
                        ) {
                            @Override
                            public Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("consumer_id", consumer_id);
                                params.put("seller_id", PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + "SELLER_ID", ""));
                                Realm.init(context);
                                Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
                                    RealmResults<RealmChat> results = realm.where(RealmChat.class)
                                            .sort("id", Sort.DESCENDING)
                                            .equalTo("seller_id", PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + "SELLER_ID", ""))
                                            .equalTo("consumer_id", consumer_id)
                                            .findAll();
                                    ArrayList<RealmChat> myArrayList = new ArrayList<>();
                                    for (RealmChat realmChat : results) {
                                        if (!(realmChat.getChat_id().startsWith("z"))) {
                                            myArrayList.add(realmChat);
                                        }
                                    }
                                    if (results.size() < 3) {
                                        params.put("id", "0");
                                    }
                                    else{
                                        params.put("id", String.valueOf(myArrayList.get(0).getId()));
                                    }
                                });
                                return params;
                            }
                            /** Passing some request headers* */
                            @Override
                            public Map getHeaders() throws AuthFailureError {
                                HashMap headers = new HashMap();
                                headers.put("accept", "application/json");
                                headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + APITOKEN, ""));
                                return headers;
                            }
                        };
                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                0,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        InitApplication.getInstance().addToRequestQueue(stringRequest);
                    }
                } else {
                    final String[] seller_name = new String[1];
                    final String[] profile_image_url = new String[1];
                    final String[] availability = new String[1];
                    String seller_id = intent.getStringExtra("SELLER_ID");

                    Realm.init(context);
                    final RealmSeller[] realmSeller = {Realm.getInstance(RealmUtility.getDefaultConfig(context)).where(RealmSeller.class).equalTo("seller_id", seller_id).findFirst()};
                    seller_name[0] = realmSeller[0].getShop_name();
                    profile_image_url[0] = realmSeller[0].getShop_image_url();
                    availability[0] = realmSeller[0].getAvailability();

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            API_URL + "consumer-chat-data",
                            response -> {
                                if (response != null) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Realm.init(context);
                                        Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
                                            try {
                                                realmSeller[0] = realm.createOrUpdateObjectFromJson(RealmSeller.class, jsonObject.getJSONObject("seller"));
                                                realm.createOrUpdateAllFromJson(RealmChat.class, jsonObject.getJSONArray("chats"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            seller_name[0] = realmSeller[0].getShop_name();
                                            profile_image_url[0] = realmSeller[0].getShop_image_url();
                                            availability[0] = realmSeller[0].getAvailability();
                                        });

                                        notificationIntent[0] = new  Intent(context, MessageActivity.class)
                                                .putExtra("SELLER_ID", seller_id)
                                                .putExtra("SELLER_NAME", seller_name[0])
                                                .putExtra("PROFILE_IMAGE_URL", profile_image_url[0])
                                                .putExtra("AVAILABILITY", availability[0]);

                                        notificationIntent[0].addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                        PendingIntent contentIntent = PendingIntent.getActivity(context, 1000, notificationIntent[0],
                                                PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(contentIntent);
                                        // Add as notification
                                        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                        manager.notify(1000, builder.build());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            error -> {
                                error.printStackTrace();
                                notificationIntent[0] = new Intent(context, MessageActivity.class)
                                        .putExtra("SELLER_ID", seller_id)
                                        .putExtra("SELLER_NAME", seller_name[0])
                                        .putExtra("PROFILE_IMAGE_URL", profile_image_url[0])
                                        .putExtra("AVAILABILITY", availability[0]);

                                notificationIntent[0].addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                PendingIntent contentIntent = PendingIntent.getActivity(context, 1000, notificationIntent[0],
                                        PendingIntent.FLAG_UPDATE_CURRENT);
                                builder.setContentIntent(contentIntent);
                                // Add as notification
                                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                manager.notify(1000, builder.build());
                                Log.d("Cyrilll", error.toString());
                            }
                    ) {
                        @Override
                        public Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("seller_id", seller_id);
                            params.put("consumer_id", PreferenceManager.getDefaultSharedPreferences(context).getString("CONSUMER_ID", ""));
                            Realm.init(context);
                            Realm.getInstance(RealmUtility.getDefaultConfig(context)).executeTransaction(realm -> {
                                RealmResults<RealmChat> results = realm.where(RealmChat.class)
                                        .sort("id", Sort.DESCENDING)
                                        .equalTo("seller_id", seller_id)
                                        .equalTo("consumer_id", PreferenceManager.getDefaultSharedPreferences(context).getString("CONSUMER_ID", ""))
                                        .findAll();
                                ArrayList<RealmChat> myArrayList = new ArrayList<>();
                                for (RealmChat realmChat : results) {
                                    if (!(realmChat.getChat_id().startsWith("z"))) {
                                        myArrayList.add(realmChat);
                                    }
                                }
                                if (results.size() < 3) {
                                    params.put("id", "0");
                                }
                                else{
                                    params.put("id", String.valueOf(myArrayList.get(0).getId()));
                                }
                            });
                            return params;
                        }
                        /** Passing some request headers* */
                        @Override
                        public Map getHeaders() throws AuthFailureError {
                            HashMap headers = new HashMap();
                            headers.put("accept", "application/json");
                            headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(context).getString("com.ekumfi.juice" + APITOKEN, ""));
                            return headers;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    InitApplication.getInstance().addToRequestQueue(stringRequest);
                }

                break;
        }
    }
}

