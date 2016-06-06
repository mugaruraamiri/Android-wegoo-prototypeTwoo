package com.hehelabs.wegoo1;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Amiri on 1/23/16.
 */
public class StaticFields {
    public static String PATIENT_NAME="com.hehelabs.wegoo1.StaticFields.pName";
    public static String PATIENT_ID_NUMBER="com.hehelabs.wegoo1.StaticFields.pIdNumber";
    public static String PATIENT_AGE="com.hehelabs.wegoo1.StaticFields.pAge";
    public static String PATIENT_LOCATION="com.hehelabs.wegoo1.StaticFields.pLocation";
    public static String NOTIFICATION_DATA = "NOTIFICATION_DATA";

    public static final int NOTIFICATION_ID = 1;

    // give your server registration url here
    static final String SERVER_URL = "http://atago.kic.ac.jp:3000/api/v1/appregister";

    // Google project id
    static final String SENDER_ID = "790141553774";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "AndroidHive GCM";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.hehelabs.wegoo1.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    public static final int MESSAGE_REFRESH = 101;
    public static final long REFRESH_TIMEOUT_MILLIS = 5000;

    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
