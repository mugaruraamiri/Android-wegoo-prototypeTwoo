package parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import models.PatientInfo;

/**
 * Created by amiri on 3/16/16.
 */
public class PatientJsonParser {
    public static List<PatientInfo> parseFeed(String content){


        try {
            JSONArray ar = null;

            ar = new JSONArray(content);
            List<PatientInfo> patientList = new ArrayList<>();

            for (int i=0; i < ar.length(); i++){

                JSONObject obj = ar.getJSONObject(i);
                PatientInfo patient = new PatientInfo();

                patient.setName(obj.getString("name"));
                patient.setIdCardNumber(obj.getString("IDnumber"));
                patient.setPatientID(obj.getString("_id"));
                patient.setDate(obj.getString("last_updated"));
                patient.setFeedbackCount("0");

                patientList.add(patient);

            }

            return patientList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}
