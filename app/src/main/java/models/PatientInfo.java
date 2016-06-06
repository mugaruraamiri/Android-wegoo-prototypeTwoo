package models;

/**
 * Created by Amiri on 1/23/16.
 */
public class PatientInfo {
    private String name, idCardNumber, date;
    private String feedbackCount;
    private String patientID;

//    public PatientInfo(String name, String idCardNumber, String date, String feedbackCount, String patientID){
//        this.name = name;
//        this.idCardNumber = idCardNumber;
//        this.date = date;
//        this.feedbackCount = feedbackCount;
//        this.patientID = patientID;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatientID(String patientID) {this.patientID = patientID;}

    public String getPatientID() { return  patientID; }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFeedbackCount() {
        return feedbackCount;
    }

    public void setFeedbackCount(String feedbackCount) {
        this.feedbackCount = feedbackCount;
    }

}
