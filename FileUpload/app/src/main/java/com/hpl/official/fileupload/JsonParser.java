package com.hpl.official.fileupload;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {

    public JsonParser() {
        super();
    }

    public String parseUserAuth(JSONObject object) {
        String userAtuh = null;
        try {
            userAtuh = object.getString("Value");

        } catch (JSONException e) {

            Log.d("parseUserAuth", e.getMessage());
        }
        return userAtuh;
    }

    public ArrayList<ListItems> parseFilePath(JSONObject object) {
        ArrayList<ListItems> arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = object.getJSONArray("Value");
            JSONObject jsonObj;
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObj = jsonArray.getJSONObject(i);
                String filepath = "";
                String type = "";
                String size = "";
                String date = "";

                try {
                    filepath = jsonObj.getString("FilePath");
                    type = jsonObj.getString("FileType");
                    size = jsonObj.getString("FileSize");
                    date = jsonObj.getString("CreationDate");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                arrayList.add(new ListItems(filepath, type, size, date));
            }

        } catch (JSONException e) {

            Log.d("parseFilePath", e.getMessage());
        }
        return arrayList;
    }
}
