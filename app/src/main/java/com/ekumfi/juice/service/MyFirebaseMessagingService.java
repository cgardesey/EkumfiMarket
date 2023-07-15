package com.ekumfi.juice.service;

import static com.ekumfi.juice.activity.GetAuthActivity.MYUSERID;
import static com.ekumfi.juice.activity.SellerHomeActivity.homeactivity;
import static com.ekumfi.juice.activity.SellerHomeActivity.retriev_current_registration_token;
import static com.ekumfi.juice.fragment.ChatIndexFragment.chatIndexFragmentContext;
import static com.ekumfi.juice.fragment.ChatIndexFragment.populateChatIndex;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.preference.PreferenceManager;
import android.util.Log;

import com.ekumfi.juice.activity.MessageActivity;
import com.ekumfi.juice.activity.SellerHomeActivity;
import com.ekumfi.juice.realm.RealmChat;
import com.ekumfi.juice.realm.RealmEnrolment;
import com.ekumfi.juice.realm.RealmInstitution;
import com.ekumfi.juice.realm.RealmInstructor;
import com.ekumfi.juice.realm.RealmInstructorCourse;
import com.ekumfi.juice.realm.RealmStudent;
import com.ekumfi.juice.realm.RealmUser;
import com.ekumfi.juice.receiver.AlarmReceiver;
import com.ekumfi.juice.receiver.NetworkReceiver;
import com.ekumfi.juice.util.MyWorker;
import com.ekumfi.juice.util.RealmUtility;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ekumfi.juice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import io.realm.Realm;

/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 * <p>
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 * <p>
 * <intent-filter>
 * <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "engineer_From: " + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("engineer", "Message data payload: " + remoteMessage.getData());

            Map<String, String> data = remoteMessage.getData();
            String type = data.get("type");

            if (type != null && type.equals("chat")) {
                JSONObject jsonResponse = new JSONObject(data);

                Realm.init(getApplicationContext());
                JSONObject finalJsonResponse = jsonResponse;
                Realm.getInstance(RealmUtility.getDefaultConfig(getApplicationContext())).executeTransaction(realm -> {
                    try {
                        JSONObject chatresponse = new JSONObject(finalJsonResponse.getString("chatresponse"));
                        realm.createOrUpdateObjectFromJson(RealmChat.class, new JSONObject(chatresponse.getString("chat")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

                boolean chatInForeground = NetworkReceiver.activeActivity instanceof MessageActivity;
                if (!chatInForeground) {

                    if (chatIndexFragmentContext != null) {
                        chatIndexFragmentContext.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                populateChatIndex(chatIndexFragmentContext);
                            }
                        });
                    }

                    Intent intent = null;
                    try {
                        intent = new Intent(getApplicationContext(), AlarmReceiver.class)
                                .putExtra("TYPE", "chat")
                                .putExtra("TITLE", finalJsonResponse.getString("title"))
                                .putExtra("BODY", finalJsonResponse.getString("body"))
                                .putExtra("CONSUMER_ID", data.get("CONSUMER_ID"))
                                .putExtra("SELLER_ID", data.get("SELLER_ID"))
                                .putExtra("NAME", data.get("NAME"))
                                .putExtra("PROFILE_IMAGE_URL", data.get("PROFILE_IMAGE_URL"))
                                .putExtra("AVAILABILITY", data.get("AVAILABILITY"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 23424243, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        retriev_current_registration_token(getApplicationContext(), token);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, SellerHomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo_square)
                        .setContentTitle("Message Title")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
