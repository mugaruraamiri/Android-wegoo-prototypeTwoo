package com.hehelabs.wegoo1;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Amiri on 2/17/16.
 */
public class WeegoPreferences {
    public final static String SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER";
    private final static String TAG = "WeegoPreferences";

    public static boolean setTokenSentToServer(final boolean tokenSent,final Context request){
        SharedPreferences preferences = request.getSharedPreferences(SENT_TOKEN_TO_SERVER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        try{
            editor.putBoolean(SENT_TOKEN_TO_SERVER, tokenSent);
            editor.apply();
            return true;
        } catch (Exception e){
            Log.e(TAG,"Exec setTokenSentToServer:"+e.getMessage());
        }

        return false;

    }

    public static boolean getTokenSentToServer(final Context request){
        try {
            SharedPreferences preferences = request.getSharedPreferences(SENT_TOKEN_TO_SERVER, Context.MODE_PRIVATE);
            return preferences.getBoolean(SENT_TOKEN_TO_SERVER, false);
        } catch (Exception e){
            Log.e(TAG, "exc setUserGCMToken " + e.getMessage());
        }
        return false;

    }
}
