package pushpender.com.streetlightcomplaints;

/**
 * Created by Pushpender Bhandari on 4/23/2016.
 */
public class GetComplainDet {
    String ComplaintNo, Date, Town, Cluster, Zone, Pole, Address, Landmark, CustomerName, CustomerPhn, Remark;
    int CurrentStatus;

    public GetComplainDet(String complaintNo, String date, String town, String cluster, String zone, String pole, String address, String landmark,
                          String customerName, String customerPhn, String remark, int currentStatus) {
        this.ComplaintNo = complaintNo;
        this.Date = date;
        this.Town = town;
        this.Cluster = cluster;
        this.Zone = zone;
        this.Pole = pole;
        this.Address = address;
        this.Landmark = landmark;
        this.CustomerName = customerName;
        this.CustomerPhn = customerPhn;
        this.Remark = remark;
        this.CurrentStatus = currentStatus;
    }

    public GetComplainDet() {
        this.ComplaintNo = null;
        this.Date = null;
        this.Town = null;
        this.Cluster = null;
        this.Zone = null;
        this.Pole = null;
        this.Address = null;
        this.Landmark = null;
        this.CustomerName = null;
        this.CustomerPhn = null;
        this.Remark = null;
        this.CurrentStatus = 0;

    }

    public String getComplaintNo() {
        return ComplaintNo;
    }

    public String setComplaintNo(String complaintNo) {
        ComplaintNo = complaintNo;
        return complaintNo;
    }

    public String getDate() {
        return Date;
    }

    public String setDate(String date) {
        Date = date;
        return date;
    }

    public String getTown() {
        return Town;
    }

    public String setTown(String town) {
        Town = town;
        return town;
    }

    public String getCluster() {
        return Cluster;
    }

    public String setCluster(String cluster) {
        Cluster = cluster;
        return cluster;
    }

    public String getPole() {
        return Pole;
    }

    public String setPole(String pole) {
        Pole = pole;
        return pole;
    }

    public String getZone() {
        return Zone;
    }

    public String setZone(String zone) {
        Zone = zone;
        return zone;
    }

    public String getAddress() {
        return Address;
    }

    public String setAddress(String address) {
        Address = address;
        return address;
    }

    public String getLandmark() {
        return Landmark;
    }

    public String setLandmark(String landmark) {
        Landmark = landmark;
        return landmark;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String setCustomerName(String customerName) {
        CustomerName = customerName;
        return customerName;
    }

    public String getCustomerPhn() {
        return CustomerPhn;
    }

    public String setCustomerPhn(String customerPhn) {
        CustomerPhn = customerPhn;
        return customerPhn;
    }

    public String getRemark() {
        return Remark;
    }

    public String setRemark(String remark) {
        Remark = remark;
        return remark;
    }

    public int getCurrentStatus() {
        return CurrentStatus;
    }

    public int setCurrentStatus(int currentStatus) {
        CurrentStatus = currentStatus;
        return currentStatus;
    }


}
