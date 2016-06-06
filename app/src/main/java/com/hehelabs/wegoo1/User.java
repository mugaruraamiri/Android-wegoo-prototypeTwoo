package com.hehelabs.wegoo1;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Amiri on 2/17/16.
 */
public class User {
    private final static String PREF_NAME_GCM = "safevault.gcm";

    private final static String TAG = "Wegoo User";

    public static String getUserGCMToken(final Context request) {
        try {
            SharedPreferences preferences = request.getSharedPreferences(PREF_NAME_GCM, Context.MODE_PRIVATE);
            return preferences.getString(PREF_NAME_GCM, " ");
        } catch (Exception e) {
            //SafeGasDroidApplication.getInstance().trackException(e);
            //Log.e(TAG, "exc getUserGCMToken " + e.getMessage());
        }
        return " ";
    }

    public static boolean setUserGCMToken(final String token,final Context request) {
        SharedPreferences preferences = request.getSharedPreferences(PREF_NAME_GCM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        try {
            editor.putString(PREF_NAME_GCM, token);
            editor.apply();
            return true;
        } catch (Exception e) {
            //SafeGasDroidApplication.getInstance().trackException(e);
            Log.e(TAG, "exc setUserGCMToken " + e.getMessage());

        }
        return false;
    }
}
