package com.hehelabs.wegoo1;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Amiri on 2/17/16.
 */
public class WegooGcmListenerService extends IntentService {

    private static final String TAG = "Wegoo GCM";
    private static final String[] TOPICS = {"global"};

    public WegooGcmListenerService() {
        super("WegooGcmListenerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "WegooGcmListenerService in onHandleIntent() with " + intent.toString());
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            sendRegistrationToServer(token);
            subscribeTopics(token);
            Log.e(TAG, "GCM Registration Token: " + token);
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit().putBoolean(WeegoPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    private void subscribeTopics(String token) throws IOException {

        try {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            for (String topic : TOPICS) {
                pubSub.subscribe(token, "/topics/" + topic, null);
            }
        } catch (Exception e) {
            //SafeGasDroidApplication.getInstance().trackException(e);
            Log.e("SafeGas GCM ", "exc with subscribeTopics  " + e.getMessage());
        }

    }

    private void sendRegistrationToServer(String token) {

        User.setUserGCMToken(token, getApplicationContext());
        if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()){
            sendRegistrationToServerAsync();
        }
    }

    private void sendRegistrationToServerAsync() {
        final AsyncTask<String, String, JSONObject> getTask = new AsyncTask<String, String, JSONObject>() {
            @Override
            protected JSONObject doInBackground(String... params) {
                try {
                    URL url = new URL("http://atago.kic.ac.jp:3000" + params[0]);
                    Log.e(TAG, "sending gcm user token " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(30000);
                    urlConnection.setReadTimeout(30000);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line = "", response = "";

                    while ((line = reader.readLine()) != null) {
                        response += line + "\n";
                    }
                    if (!urlConnection.getContentType().equalsIgnoreCase("application/json")) {
                        //Log.e(TAG, "Received bad content type while expecting json");
                        return null;
                    }
                    Log.e(TAG, "response " + response);
                    return new JSONObject(response);
                } catch (Exception e) {
                    //SafeGasDroidApplication.getInstance().trackException(e);
                    Log.e(TAG, "An exception in newVersion check " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(final JSONObject result) {
                try {
                    if (result == null) {
                        sendRegistrationToServerAsync();
                        return;
                    }
                    if ("success".equalsIgnoreCase(result.getString("status"))) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        sharedPreferences.edit().putBoolean(WeegoPreferences.SENT_TOKEN_TO_SERVER, true).apply();
//                        Intent registrationComplete = new Intent(WeegoPreferences.REGISTRATION_COMPLETE);
//                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
                    } else {
                        sendRegistrationToServerAsync();
                    }
                } catch (Exception e) {
                    //SafeGasDroidApplication.getInstance().trackException(e);
                    Log.e(TAG, "An exc in post of new version check " + e.getMessage());
                }
            }
        };

        if (!WeegoPreferences.getTokenSentToServer(getApplicationContext())) {
            getTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "/register/token/" + User.getUserGCMToken(getApplicationContext()) + "/" + AppUtils.getDeviceUuid(getApplicationContext()));
        }


    }
}
