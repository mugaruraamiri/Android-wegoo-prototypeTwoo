package com.hehelabs.wegoo1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import models.PatientInfo;

/**
 * Created by Amiri on 1/23/16.
 */
public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.MyViewHolder> {


    private List<PatientInfo> patientList;

    public PatientListAdapter(List<PatientInfo> patientList) {
        this.patientList = patientList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mIdNumber, mDate, mNotificationCount;
        public MyViewHolder(View view) {
            super(view);

            mName = (TextView) view.findViewById(R.id.patient_name);
            mIdNumber = (TextView) view.findViewById(R.id.id_number);
            mDate = (TextView) view.findViewById(R.id.item_date);
            mNotificationCount = (TextView) view.findViewById(R.id.notif_count);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        PatientInfo patientInfo = patientList.get(position);
        holder.mName.setText(patientInfo.getName());
        holder.mIdNumber.setText(patientInfo.getIdCardNumber());
        holder.mDate.setText(patientInfo.getDate());
        if (patientInfo.getFeedbackCount() == "0"){
            holder.mNotificationCount.setVisibility(View.INVISIBLE);
        }else {
            holder.mNotificationCount.setText(patientInfo.getFeedbackCount());
        }


    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }
}
