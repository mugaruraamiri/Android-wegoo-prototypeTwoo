package parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by amiri on 4/11/16.
 */
public class JsonPerser {
    public static String ResponceParser(String content){
        String result="";
        try {
            JSONObject object = new JSONObject(content);
            JSONArray ar = new JSONArray(object);
            for (int i=0; i < ar.length(); i++){
                JSONObject obj = ar.getJSONObject(i);
                result = obj.getString("status");
            }
            return result;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
