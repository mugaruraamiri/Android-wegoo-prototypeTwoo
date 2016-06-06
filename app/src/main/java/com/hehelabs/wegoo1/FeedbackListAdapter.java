package com.hehelabs.wegoo1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import models.FeedbackInfo;

/**
 * Created by amiri on 3/18/16.
 */
public class FeedbackListAdapter extends RecyclerView.Adapter<FeedbackListAdapter.MyViewHolder> {

    private List<FeedbackInfo> feedbackList;
    Context context;

    public FeedbackListAdapter(Context context, List<FeedbackInfo> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView userImage;
        private TextView userName;
        private TextView userIdNumber;
        private TextView userDate;
        private TextView userFeeback;

        public MyViewHolder(View view) {
            super(view);

            userImage = (ImageView) view.findViewById(R.id.user_image);
            userName = (TextView) view.findViewById(R.id.user_name);
            userDate = (TextView) view.findViewById(R.id.user_date);
            userFeeback = (TextView) view.findViewById(R.id.feed_text);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedback_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override

    public void onBindViewHolder(MyViewHolder holder, int position) {

        FeedbackInfo feedbackInfo = feedbackList.get(position);
        holder.userName.setText(feedbackInfo.getUserName());
        holder.userDate.setText(feedbackInfo.getPostedDate());
        holder.userFeeback.setText(feedbackInfo.getFeedback());

        Bitmap batmapBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.profile15);

        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),batmapBitmap);
        drawable.setCircular(true);

        holder.userImage.setImageDrawable(drawable);

    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }
}
