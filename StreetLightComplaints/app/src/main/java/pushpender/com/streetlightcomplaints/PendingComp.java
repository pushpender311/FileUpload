package pushpender.com.streetlightcomplaints;

/**
 * Created by Pushpender Bhandari on 4/23/2016.
 */
public class PendingComp {
    int ComplaintID;
    String ComplaintNo;

    public PendingComp(int complaintID, String complaintNo) {
        this.ComplaintID = complaintID;
        this.ComplaintNo = complaintNo;
    }

    public PendingComp() {
        this.ComplaintID = 0;

        this.ComplaintNo = null;

    }

    public String getComplaintNo() {
        return ComplaintNo;
    }

    public void setComplaintNo(String complaintNo) {
        ComplaintNo = complaintNo;
    }

    public int getComplaintID() {
        return ComplaintID;
    }

    public void setComplaintID(int complaintID) {
        ComplaintID = complaintID;
    }
}
