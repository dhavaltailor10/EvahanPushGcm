package com.evahanpushgcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dreamworld Solutions on 22-7-2016.
 */
public class GCMNotificationIntentService extends IntentService {

    public static String LSTATUS = "latest_status";
    public static String LTIME = "latest_time";

    public int NOTIFICATION_ID = 1;
    public static String TAG = "GCMNotificationIntentService";
    NotificationCompat.Builder builder;
    Bitmap Images;
    String awb, status;
    private NotificationManager mNotificationManager;
    ArrayList<String> alist = new ArrayList<>();

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(7000);
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onCreate();
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                sendNotification((String) extras.get("Shiptrack"));

            }
        }
        GcmReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {

        try {

            JSONObject jsonObject = new JSONObject(msg);
            awb = jsonObject.getString("awb_no");
            status = jsonObject.getString("status");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent in = new Intent(this, DisplayActivity.class);
        in.putExtra("Notif", awb.toString());
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Random random = new Random();
        int no = random.nextInt(9999 - 1000) + 1000;
        //in.putExtra("url", surl);
        PendingIntent contentIntent = PendingIntent.getActivity(this, no, in, PendingIntent.FLAG_UPDATE_CURRENT);

        int numMessages = 0;
        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                this).setSmallIcon(R.mipmap.ic_launcher_app)

                .setContentTitle(getString(R.string.app_name))
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(awb + "\n" + status))
                .setAutoCancel(true)
                .setContentText(status)
                .setDefaults(Notification.DEFAULT_ALL)
                .setNumber(++numMessages);
        mBuilder.setContentIntent(contentIntent);
        int n = random.nextInt(9999 - 1000) + 1000;
        mNotificationManager.notify(n, mBuilder.build());
        mBuilder.setAutoCancel(true);
        NOTIFICATION_ID = NOTIFICATION_ID + 1;
/*
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
*/

    }
}