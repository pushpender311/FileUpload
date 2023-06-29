package pushpender.com.streetlightcomplaints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pushpender Bhandari on 4/22/2016.
 */
public class JSONParser {
    public JSONParser() {
        super();
    }

    public UserData parseAuthentication(JSONObject object) {
        UserData details = new UserData();
        try {
            JSONObject Value = object.getJSONObject("Value");
            JSONObject jsonObj = Value.getJSONArray("Table").getJSONObject(0);
            int res = 0;
            String msg = null;
            String name = null;
            try {

                res = details.setRespose(jsonObj.getInt("Column1"));
                msg = details.setMessage(jsonObj.getString("Column2"));
                name = details.setPersonName(jsonObj.getString("PersonName"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            new UserData(res, msg, name);
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return details;
    }

    public Update parseUpdate(JSONObject object) {
        Update details = new Update();
        try {
            JSONObject Value = object.getJSONObject("Value");
            JSONObject jsonObj = Value.getJSONArray("Table").getJSONObject(0);
            String column1 = null;
            String column2 = null;
            try {

                column1 = details.setColumn1(jsonObj.getString("Column1"));
                column2 = details.setColumn2(jsonObj.getString("Column2"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Update(column1, column2);
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return details;
    }

    public GetComplainDet parseGetComplaintDet(JSONObject object) {
        GetComplainDet details = new GetComplainDet();
        String ComplaintNo = "";
        String EntryDate = "";
        String Cluster = "";
        String Town = "";
        String Zone = "";
        String PoleNo = "";
        String Address = "";
        String LandMark = "";
        String CustomerName = "";
        String CustomerNo = "";
        String Remark = "";
        int StatusID = 0;

        try {
            JSONObject jsonObject = object.getJSONObject("Value");
            JSONObject jsonArray = jsonObject.getJSONArray("Table").getJSONObject(0);
            ComplaintNo = details.setComplaintNo(jsonArray.getString("ComplaintNo"));
            EntryDate = details.setDate(jsonArray.getString("Dated"));
            Cluster = details.setCluster(jsonArray.getString("Cluster"));
            Town = details.setTown(jsonArray.getString("Town"));
            Zone = details.setZone(jsonArray.getString("Zone"));
            PoleNo = details.setPole(jsonArray.getString("PoleNo"));
            Address = details.setAddress(jsonArray.getString("Address"));
            LandMark = details.setLandmark(jsonArray.getString("LandMark"));
            CustomerName = details.setCustomerName(jsonArray.getString("CustomerName"));
            CustomerNo = details.setCustomerPhn(jsonArray.getString("CustomerNo"));
            StatusID = details.setCurrentStatus(jsonArray.getInt("StatusID"));
            Remark = details.setRemark(jsonArray.getString("Remarks"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new GetComplainDet(ComplaintNo, EntryDate, Town, Cluster, Zone, PoleNo, Address, LandMark, CustomerName, CustomerNo, Remark, StatusID);
        return details;
    }

    public ArrayList<GetStatus> parseGetStatus(JSONObject object) {
        ArrayList<GetStatus> details = new ArrayList<>();

        try {
            JSONObject jsonObject = object.getJSONObject("Value");
            JSONArray jsonArray = jsonObject.getJSONArray("Table");
            JSONObject object1;
            for (int i = 0; i < jsonArray.length(); i++) {
                object1 = jsonArray.getJSONObject(i);
                String desc = "";
                int fig = 0;
                try {
                    desc = object1.getString("Description");
                    fig = object1.getInt("Figure");
                } catch (JSONException ignored) {
                }
                details.add(new GetStatus(desc, fig));
            }

        } catch (NullPointerException | JSONException e) {
            e.printStackTrace();
        }
        return details;
    }

    public ArrayList<PendingComp> parseItems(JSONObject object) {
        ArrayList<PendingComp> arrayList = new ArrayList<>();
        try {
            JSONObject Value = object.getJSONObject("Value");
            JSONArray jsonArray = Value.getJSONArray("Table");
            JSONObject jsonObj;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObj = jsonArray.getJSONObject(i);
                int compID = 0;
                String complaintNO = "";

                try {
                    compID = jsonObj.getInt("ComplaintID");
                    complaintNO = jsonObj.getString("ComplaintNo");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                arrayList.add(new PendingComp(compID, complaintNO));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return arrayList;
    }

    public ArrayList<Logs> parseLogs(JSONObject object) {
        ArrayList<Logs> arrayList = new ArrayList<>();
        try {
            JSONObject Value = object.getJSONObject("Value");
            JSONArray jsonArray = Value.getJSONArray("Table1");
            JSONObject jsonObj;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObj = jsonArray.getJSONObject(i);
                String fieldUpdated = "";
                String Remark = "";
                String EnterBy = "";
                String EntryTime = "";

                try {
                    fieldUpdated = jsonObj.getString("FieldUpdated");
                    Remark = jsonObj.getString("Remarks");
                    EnterBy = jsonObj.getString("EnterBy");
                    EntryTime = jsonObj.getString("EntryTime");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                arrayList.add(new Logs(fieldUpdated, Remark, EnterBy, EntryTime));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return arrayList;
    }

}
