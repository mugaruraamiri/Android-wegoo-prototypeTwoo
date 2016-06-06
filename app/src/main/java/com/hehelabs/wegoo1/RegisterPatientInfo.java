package com.hehelabs.wegoo1;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import parsers.JsonPerser;

public class RegisterPatientInfo extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static String TAG = "RegisterPatientInfo";

    private EditText Name, nationalId, location;
    private TextInputLayout inputNameLayout, inputNationalIdLayout, inputLocationLayout;
    private RadioButton genderMale, genderFemale;
    private Button mRegister;
    private Spinner mSpiner;

    private String latitude;
    private String Longitute;
    private GoogleApiClient.Builder mGoogleApiClient;
    private Location mLastLocation;

    private String spinnerSelectedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputNameLayout = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputNationalIdLayout = (TextInputLayout) findViewById(R.id.input_layout_id);
        inputLocationLayout = (TextInputLayout) findViewById(R.id.input_layout_location);

        Name = (EditText) findViewById(R.id.input_name);
        nationalId = (EditText) findViewById(R.id.input_ID);
        location = (EditText) findViewById(R.id.input_location);

        genderMale = (RadioButton) findViewById(R.id.male_radio_button);
        genderFemale = (RadioButton) findViewById(R.id.female_radio_button);

        mRegister = (Button) findViewById(R.id.btn_register);

        Name.addTextChangedListener(new MyTextWatcher(Name));
        nationalId.addTextChangedListener(new MyTextWatcher(nationalId));

        mSpiner = (Spinner) findViewById(R.id.gender_spinner);
        ArrayAdapter<CharSequence> spinerAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpiner.setAdapter(spinerAdapter);

        mSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedValue = mSpiner.getSelectedItem().toString();
                //Toast.makeText(getBaseContext(), spinnerSelectedValue + " is selected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        mRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                submitRegistrationForm();
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void submitRegistrationForm(){
        if(!validateName()){
            return;
        }
        if (!validateNationalId()){
            return;
        }
        if (!validateLocation()){
            return;
        }

        JSONObject JSONparams = new JSONObject();

        try {
            JSONparams.put("name",Name.getText().toString());
            JSONparams.put("IDnumber", nationalId.getText().toString());
            JSONparams.put("gender", genderMale.getText().toString());
            JSONparams.put("gender", spinnerSelectedValue);
            JSONparams.put("location", location.getText().toString());
            JSONparams.put("Device_uuid", AppUtils.getDeviceUuid(getApplicationContext()));
        } catch (JSONException e) {
            Log.e(TAG,"Setting app JSON parameter"+e.getMessage(),e);
            e.printStackTrace();
        }

        RegisterPatientToServer registerPatientToServer = new RegisterPatientToServer();
        registerPatientToServer.execute(JSONparams);


    }

    private boolean validateLocation() {
        if (location.getText().toString().trim().isEmpty()){
            inputLocationLayout.setError(getString(R.string.err_mess_id));
            location.requestFocus();
            return false;
        }else {
            inputNationalIdLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNationalId() {
        if (nationalId.getText().toString().trim().isEmpty() && nationalId.getText().length() != 12){
            inputNationalIdLayout.setError(getString(R.string.err_mess_id));
            nationalId.requestFocus();
            return false;
        }else {
            inputNationalIdLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateName() {
        if (Name.getText().toString().trim().isEmpty()){
            inputNameLayout.setError(getString(R.string.err_messg_name));
            Name.requestFocus();
            return false;
        }else {
            inputNameLayout.setErrorEnabled(false);
        }
        return true;
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;
        public MyTextWatcher(EditText view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()){
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_ID:
                    validateNationalId();
                    break;
                case R.id.input_location:
                    validateLocation();
                    break;
            }
        }
    }

    private class RegisterPatientToServer extends AsyncTask<JSONObject, Void, String> {
        @Override
        protected String doInBackground(JSONObject... params) {
            String result = HTTPManager.registerPatient("http://atago.kic.ac.jp:3000/data/api/registerPatient", params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(TAG, ""+s);
            if (s.equals("success")){
                Bundle b = new Bundle();
                Intent intent = new Intent(RegisterPatientInfo.this, PatientInfoDetails.class);
                b.putString(StaticFields.PATIENT_NAME, Name.getText().toString());
                b.putString(StaticFields.PATIENT_ID_NUMBER, nationalId.getText().toString());
                b.putString("patientID", "11");
                intent.putExtras(b);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(),"An Error ocured", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
