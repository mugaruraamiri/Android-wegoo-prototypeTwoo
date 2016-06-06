package com.hehelabs.wegoo1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import driver.UsbSerialDriver;
import driver.UsbSerialPort;
import driver.UsbSerialProber;
import models.PatientInfo;
import parsers.PatientJsonParser;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private List<PatientInfo> patientList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PatientListAdapter mAdapter;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StaticFields.MESSAGE_REFRESH:
                    refleshDeviceList();
                    mHandler.sendEmptyMessageDelayed(StaticFields.MESSAGE_REFRESH, StaticFields.REFRESH_TIMEOUT_MILLIS);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    private UsbManager mUsbManager;
    private UsbSerialPort mPort;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mUsbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);

        recyclerView = (RecyclerView) view.findViewById(R.id.patient_list);
        mAdapter = new PatientListAdapter(patientList);

        setHasOptionsMenu(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PatientInfo patient = patientList.get(position);
                //Toast.makeText(getContext(), patient.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), PatientInfoDetails.class);
                Bundle b = new Bundle();
                b.putString(StaticFields.PATIENT_NAME, patient.getName());
                b.putString(StaticFields.PATIENT_ID_NUMBER, patient.getIdCardNumber());
                b.putString("patientID", patient.getPatientID());
                intent.putExtras(b);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        PreparePatientList();
        return view;

    }

    private void PreparePatientList() {

        requestPatientData("http://atago.kic.ac.jp:3000/data");

        //mAdapter.notifyDataSetChanged();
    }

    private void requestPatientData(String uri) {
        if (new ConnectionDetector(getContext()).isConnectingToInternet()) {
            MyTask task = new MyTask();
            task.execute(uri);
        } else {
            Toast.makeText(getContext(), "Please check your connections", Toast.LENGTH_SHORT).show();
        }
    }


    //handling Recycle view clicl Item

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivityFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivityFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    private class MyTask extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Loading...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String content = HTTPManager.RequestPatientList(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Log.d("MainActivity", "data :" + result);
            patientList.addAll(PatientJsonParser.parseFeed(result));
            //updateDisplay(patientList);
            mAdapter.notifyDataSetChanged();

        }
    }

    private void refleshDeviceList() {
        new AsyncTask<Void, Void, List<UsbSerialPort>>() {
            @Override
            protected List<UsbSerialPort> doInBackground(Void... params) {
                //Log.d(TAG, "Refreshing device list ...");
                SystemClock.sleep(1000);

                final List<UsbSerialDriver> drivers =
                        UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);

                final List<UsbSerialPort> result = new ArrayList<UsbSerialPort>();
                for (final UsbSerialDriver driver : drivers) {
                    final List<UsbSerialPort> ports = driver.getPorts();
                    //Log.d(TAG, String.format("+ %s: %s port%s",
                    //driver, Integer.valueOf(ports.size()), ports.size() == 1 ? "" : "s"));
                    result.addAll(ports);
                }

                return result;
            }

            @Override
            protected void onPostExecute(List<UsbSerialPort> result) {
//                mEntries.clear();
//                mEntries.addAll(result);
//                mAdapter.notifyDataSetChanged();
//                mProgressBarTitle.setText(
//                        String.format("%s device(s) found", Integer.valueOf(mEntries.size())));
//                hideProgressBar();
//                Log.d(TAG, "Done refreshing, " + mEntries.size() + " entries found.");
                mPort = result.get(0);
                String deviceFound = String.format("%s device(s) found", Integer.valueOf(result.size()));
                Toast.makeText(getActivity().getApplicationContext(), deviceFound, Toast.LENGTH_SHORT).show();
            }

        }.execute((Void) null);
    }
}


