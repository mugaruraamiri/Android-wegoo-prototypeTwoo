package com.hehelabs.wegoo1;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Amiri on 1/25/16.
 */

public class HTTPManager {

    public static String RequestPatientList(String uri) {

        BufferedReader reader = null;
        try {
            Log.d("HTTPManage", "url:"+uri);
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    public static String RequestFeedbackList(String uri){
        BufferedReader reader = null;

        try {
            Log.d("HTTPManage", "url:"+uri);
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    public static String registerPatient(String uri, JSONObject JSONparams){

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String TAG = "RegisterPatientToServer";
        //final String status;

        OkHttpClient client = new OkHttpClient();
        RequestBody bodyData = RequestBody.create(JSON, String.valueOf(JSONparams));
        Request request = new Request.Builder()
                .url(uri)
                .post(bodyData)
                .build();

 //       client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//
//                    try {
//                        String responceData = response.body().string();
//                        JSONObject json = new JSONObject(responceData);
//                        final String status = json.getString("status");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });

        try {
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            try {
                JSONObject json = new JSONObject(jsonData);
                String status = json.getString("status");
                return status.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e(TAG,"getting responce:"+e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

}
