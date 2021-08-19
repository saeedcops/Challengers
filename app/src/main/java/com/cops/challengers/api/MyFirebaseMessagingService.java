package com.cops.challengers.api;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import android.util.Log;

import com.cops.challengers.R;
import com.cops.challengers.view.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String title,content;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        // Check if message contains a notification payload.

        if (remoteMessage.getData().size() > 0) {

            SharedPreferences sharedPreferences = getSharedPreferences(
                    "challengers", this.MODE_PRIVATE);

            boolean notification = sharedPreferences.getBoolean("notification", true);
            Intent intent = new Intent("MyData");
            if (remoteMessage.getNotification().getBody().equals("League")) {

               title=getString(R.string.league);
               content=getString(R.string.rank)+Integer.parseInt(remoteMessage.getData().get("rank"))+"\n"+
                       getString(R.string.prize)+Integer.parseInt(remoteMessage.getData().get("prize"));
                intent.putExtra("type", "MyData");
                intent.putExtra("coins", Integer.parseInt(remoteMessage.getData().get("coins")));
                intent.putExtra("image", remoteMessage.getNotification().getIcon());
                intent.putExtra("league", remoteMessage.getData().get("league"));
                intent.putExtra("next", remoteMessage.getData().get("next"));
                intent.putExtra("prize", Integer.parseInt(remoteMessage.getData().get("prize")));
                intent.putExtra("rank", Integer.parseInt(remoteMessage.getData().get("rank")));
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);


                if (notification) {
                    sendNotification(remoteMessage);
                }

            }else if (remoteMessage.getNotification().getBody().equals("Challenge")) {

                title=getString(R.string.challenge);
                content=remoteMessage.getData().get("name")+" "+getString(R.string.challenged_for)+
                        Integer.parseInt(remoteMessage.getData().get("coins"));

                intent.putExtra("type", "Challenge");
                intent.putExtra("coins", Integer.parseInt(remoteMessage.getData().get("coins")));
                intent.putExtra("image", remoteMessage.getNotification().getIcon());
                intent.putExtra("category", remoteMessage.getData().get("category"));
                intent.putExtra("name", remoteMessage.getData().get("name"));
                intent.putExtra("room", Integer.parseInt(remoteMessage.getData().get("room")));

                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                if (notification) {
                    sendNotification(remoteMessage);
                }

            }else if (remoteMessage.getNotification().getBody().equals("Daily")) {

                title=getString(R.string.daily_gift);
                content=getString(R.string.prize)+
                        Integer.parseInt(remoteMessage.getData().get("prize"));
                intent.putExtra("type", "Daily");
                intent.putExtra("prize", Integer.parseInt(remoteMessage.getData().get("prize")));

                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                if (notification) {
                    sendNotification(remoteMessage);
                }
            }



        }

    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);


    }

    private void sendNotification(RemoteMessage messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

//        String message="Your rank is "+messageBody.getSettingData().get("rank")+", Prize "+messageBody.getSettingData().get("prize");
        String channelId = getString(R.string.default_web_client_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                getResources(), R.drawable.logo))
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(title)
                        .setContentText(content)
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
