package pushpender.com.streetlightcomplaints;

/**
 * Created by Pushpender Bhandari on 4/25/2016.
 */
public class Logs {
    String FieldUpdated, Remarks, EnterBy, EntryTime;

    public Logs(String fieldUpdated, String remarks, String enterBy, String entryTime) {
        this.FieldUpdated = fieldUpdated;
        this.Remarks = remarks;
        this.EnterBy = enterBy;
        this.EntryTime = entryTime;
    }

    public Logs() {

        this.FieldUpdated = null;
        this.Remarks = null;
        this.EnterBy = null;
        this.EntryTime = null;
    }

    public String getFieldUpdated() {
        return FieldUpdated;
    }

    public void setFieldUpdated(String fieldUpdated) {
        FieldUpdated = fieldUpdated;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getEnterBy() {
        return EnterBy;
    }

    public void setEnterBy(String enterBy) {
        EnterBy = enterBy;
    }

    public String getEntryTime() {
        return EntryTime;
    }

    public void setEntryTime(String entryTime) {
        EntryTime = entryTime;
    }
}
