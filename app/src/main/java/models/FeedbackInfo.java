package models;

import android.graphics.Bitmap;

/**
 * Created by amiri on 3/17/16.
 */
public class FeedbackInfo {
    private String userName;
    private String postedDate;
    private String feedback;
    private Bitmap profilePic;

//    public FeedbackInfo(String userName, String postedDate, String feedback) {
//        this.userName = userName;
//        this.postedDate = postedDate;
//        this.feedback = feedback;
//    }

    public String getFeedback() {
        return feedback;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Bitmap getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Bitmap profilePic) {
        this.profilePic = profilePic;
    }


}
