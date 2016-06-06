package com.hehelabs.wegoo1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import driver.UsbSerialPort;
import objects.MyObject;
import util.SerialInputOutputManager;

public class VitalSignsInfo extends AppCompatActivity {

    //    static final DataPoint[] DEMO_DATA = { 0.02, 0.02, 0.02, 0.02, 0.05, 0.07, 0.06, 0.09, 0.06,
//                -0.2, 0.9, 0.5, -0.1, 0.02, 0.02, 0.04, 0.1, 0.08, 0.03, 0.02, 0.02, 0.02, 0.02, 0.02};
    private static final String TAG = "VitalSignsInfo";

    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private LineGraphSeries<DataPoint> mSeries1;

    private static Bundle b;
    public String pname;
    public String pid;
    public String _id;


    private static final MyObject obj = new MyObject();

    private ProgressBar mProgress;
    private GraphView graph;

    private static UsbSerialPort sPort = null;
    private static String patientName;
    private static String patientIdNumber;
    private static String patientID;

    private SerialInputOutputManager mSerialIoManager;

    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {
                    VitalSignsInfo.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            VitalSignsInfo.this.updateReceivedData(data);
                        }
                    });
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_signs_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Keep the screen on for this activity
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mProgress = (ProgressBar) findViewById(R.id.circle_progress_bar);
        mProgress.setProgress(75);


        Intent intent = getIntent();

        graph = (GraphView) findViewById(R.id.graph);
        mSeries1 = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, 0.02),
                new DataPoint(1, 0.02),
                new DataPoint(2, 0.02),
                new DataPoint(3, 0.05),
                new DataPoint(4, 0.07),
                new DataPoint(5, 0.06),
                new DataPoint(6, 0.09),
                new DataPoint(7, 0.06),
                new DataPoint(8, -0.2),
                new DataPoint(9, 0.9),
                new DataPoint(10, 0.5),
                new DataPoint(11, -0.1),
                new DataPoint(12, 0.02),
                new DataPoint(13, 0.02),
                new DataPoint(14, 0.04),
                new DataPoint(15, 0.1),
                new DataPoint(16, 0.06)

        });

        mSeries1.setTitle("Random Curve 1");
        mSeries1.setColor(Color.GREEN);
        mSeries1.setDrawDataPoints(true);
        mSeries1.setDataPointsRadius(4);
        mSeries1.setThickness(6);

        graph.addSeries(mSeries1);
    }

    public void backButonPressed() {
        Intent intent = new Intent();
        intent.putExtra("patientInfo", obj);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            backButonPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sensor_data, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                //action here
                return true;
            case android.R.id.home:
                backButonPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateReceivedData(byte[] data) {
    }

    public static void show(Context context, UsbSerialPort port, String pname, String pid, String id) {

        sPort = port;
        patientName = pname;
        patientIdNumber = pid;
        patientID = id;

        obj.setName(patientName);
        obj.setpId(patientIdNumber);
        obj.setId(patientID);

        final Intent intent = new Intent(context, VitalSignsInfo.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);

    }
}
