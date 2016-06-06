package com.hehelabs.wegoo1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Amiri on 2/17/16.
 */
public class WegooGcmHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 666345;

    public void onMessageReceived(String from, Bundle data) {
        //Log.e("wegoo GCM", "GCM in SafeGasGcmHandler onMessageReceived() with  msg from " + from);
        if (data == null) {
            //Log.e("SafeGas GCM", "GCM in SafeGasGcmHandler onMessageReceived() with  msg==null ..returning " + from);
            return;
        }
        //Json object should contain the name message
        String message = data.getString("message");
        String name = data.getString("name");
        String _id = data.getString("_id");
        createNotification(from, message, name, _id);
    }

    private void createNotification(String from, String message, String name, String _id) {
        try {
            Intent resultIntent = new Intent(this, PatientInfoDetails.class);
            resultIntent.setAction(Intent.ACTION_MAIN);
            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            resultIntent.putExtra("name", name);
            resultIntent.putExtra("_id", _id);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                            .setContentTitle("Wegoo title ")
                            .setContentText(message)
                            .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                            .setContentIntent(pendingIntent)
                            .addAction(0, "OPEN", pendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, builder.build());
            playSoundNotification();

        } catch (Exception e) {
            Log.e("wegoo GCM ", "exc with createNotification  " + e.getMessage());
        }
    }

    private void playSoundNotification() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (notification == null) {
                notification = RingtoneManager.getValidRingtoneUri(getApplicationContext());
            }
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            //SafeGasDroidApplication.getInstance().trackException(e);
        }
    }
}
