package com.hehelabs.wegoo1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Created by amiri on 3/26/16.
 */
public class AppUtils {

    public static String getDeviceUuid(Context context){
        String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }

}
