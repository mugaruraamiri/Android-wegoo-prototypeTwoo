package parsers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.hehelabs.wegoo1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import models.FeedbackInfo;
import models.PatientInfo;

/**
 * Created by amiri on 3/19/16.
 */
public class FeedJsonParser {
    public static List<FeedbackInfo> ParseFeed(String content){
        try {
            JSONArray ar = null;

            ar = new JSONArray(content);
            List<FeedbackInfo> feedlist = new ArrayList<>();

            for (int i=0; i < ar.length(); i++){

                JSONObject obj = ar.getJSONObject(i);
                FeedbackInfo fedlogs = new FeedbackInfo();
                fedlogs.setUserName(obj.getString("name"));
                fedlogs.setPostedDate(obj.getString("time"));
                fedlogs.setFeedback(obj.getString("feedback"));

                feedlist.add(fedlogs);

            }

            return feedlist;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
