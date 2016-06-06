package com.hehelabs.wegoo1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import models.FeedbackInfo;
import objects.MyObject;
import parsers.FeedJsonParser;

public class PatientInfoDetails extends AppCompatActivity {

    private final static int REQUEST_CODE = 1;

    private static List<FeedbackInfo> feedlist = new ArrayList<>();

    private RecyclerView mFeedrecyclerView;
    private static FeedbackListAdapter mFeedAdapter;

    private TextView pName;
    private TextView pIdNumber;
    private TextView pGender;
    private TextView pLocation;

    private String patientName;
    private String patientID;
    String _id;
    private Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        pName = (TextView) findViewById(R.id.pr_name);
        pIdNumber = (TextView) findViewById(R.id.pr_id_number);


        b = getIntent().getExtras();
        if (b != null) {
            _id = b.getString("patientID");
            patientName = b.getString(StaticFields.PATIENT_NAME);
            patientID = b.getString(StaticFields.PATIENT_ID_NUMBER);

            patientScreenInfo(patientName, patientID);
        }

        final MyObject myobj = new MyObject();

        myobj.setName(patientName);
        myobj.setpId(patientID);


        Log.d("PatientInfoDetails", "patiebid:" + _id);
        if (_id != "11") {
            myobj.setId(_id);
            prepareFeedbackList(_id);
        }

        FloatingActionButton fab_signs = (FloatingActionButton) findViewById(R.id.fab_signs);
        fab_signs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInfoDetails.this, DeviceListActivity.class);
                intent.putExtra("patientInfo", myobj);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        mFeedrecyclerView = (RecyclerView) findViewById(R.id.feed);

        mFeedAdapter = new FeedbackListAdapter(getApplicationContext(),feedlist);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mFeedrecyclerView.setLayoutManager(mLayoutManager);
        mFeedrecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFeedrecyclerView.setAdapter(mFeedAdapter);

        mFeedAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode){
                Bundle b = data.getExtras();
                if(b != null){
                    MyObject myobject = (MyObject) b.getSerializable("object");
                    System.out.print("Name: "+myobject.getName() + "ID:" + myobject.getId());
                    Log.d("fromsensorActivity", myobject.getName());
                    patientScreenInfo(myobject.getName(),myobject.getpId());
                    prepareFeedbackList(myobject.getId());
                }else {
                    Log.d("fromsensorActivity", "Data was null from second activity");
                }
        }else {
            Log.d("fromsensorActivity", "Data was null from second activity else");
        }
    }

    private void patientScreenInfo(String patientName, String patientID) {
        pName.setText(patientName);
        pIdNumber.setText("ID: " + patientID);
    }

    private void prepareFeedbackList(String pId) {
        String feedUrl = "http://atago.kic.ac.jp:3000/getfeedback/"+pId;
        requestPatientFeedback(feedUrl);

    }

    private void requestPatientFeedback(String feedUrl) {
        MyTask task = new MyTask();
        task.execute(feedUrl);
    }

    @Override
    public void onResume() {
        super.onResume();
        feedlist.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        feedlist.clear();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                //action here
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    private class MyTask extends AsyncTask<String, Void, String> {
        String content;
        private ProgressDialog dialog = new ProgressDialog(PatientInfoDetails.this);
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            content = HTTPManager.RequestFeedbackList(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            Log.d("feebackdata", "data :"+result);
            feedlist.clear();
            feedlist.addAll(FeedJsonParser.ParseFeed(result));
            //updateDisplay(patientList);
            mFeedAdapter.notifyDataSetChanged();

        }
    }
}
