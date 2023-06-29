package com.hpl.official.fileupload;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

/************Developed By:
 Pushpender Bhandari
 ********************/

public class LoadRecent extends Activity {
    ListView filePathList;
    ArrayList<String> data;
    ArrayList<String> data1;
    ArrayList<String> data2;
    ArrayList<String> data3;
    ArrayAdapter<String> adapter;
    ProgressDialog pDialog;
    String loginID;
    AlertDialogManager alert;
    ConnectionDetector cd;
    AlertDialog.Builder builder;
    Handler handler;

    CustomAdapter cAdap;
    Runnable runnable;
    boolean loading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_recent);
        filePathList = (ListView) findViewById(R.id.listView);
        pDialog = new ProgressDialog(this);
        cd = new ConnectionDetector(this);
        Intent intent = getIntent();
        loginID = intent.getStringExtra("loginid");
        new AsyncRecentImages().execute(loginID);
        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            builder = new AlertDialog.Builder(this);
            builder.setMessage("You are not connected to internet. Click OK to turn on internet")
                    .setTitle("Connection Failed")
                    .setIcon(R.drawable.success)
                    .setPositiveButton(Html.fromHtml(" OK "), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            handler = new Handler();
            handler.postDelayed(runnable = new Runnable() {
                @Override
                public void run() {
                    if (loading) {
                        pDialog.dismiss();
                        builder = new AlertDialog.Builder(LoadRecent.this);
                        builder.setMessage("Alert..!! Not able to connect to server. Please try again later..")
                                .setTitle("Connetion Failed")
                                .setIcon(R.drawable.fail)
                                .setPositiveButton(Html.fromHtml(" OK "), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        loading = false;
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }, 60 * 1000);
        }
        data = new ArrayList<>();
        data1 = new ArrayList<>();
        data2 = new ArrayList<>();
        data3 = new ArrayList<>();
        cAdap = new CustomAdapter(this, data, data1, data2, data3);
        //adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        filePathList.setAdapter(cAdap);
        filePathList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cd = new ConnectionDetector(getApplicationContext());
                if (!cd.isConnectingToInternet()) {
                    alert.showAlertDialog(LoadRecent.this, "Connection Failed", "It seems your internet connection is not working. Turn your wifi/data ON", false);
                } else {
                    String file = filePathList.getItemAtPosition(position).toString();
                    String Link = Config.FILE_URL + file;
                    Intent i = new Intent(getApplicationContext(), ViewFile.class);
                    i.putExtra("link", Link);
                    startActivity(i);
                }
            }
        });
    }

    private class AsyncRecentImages extends AsyncTask<String, Void, ArrayList<ListItems>> {
        ArrayList<ListItems> list = null;

        @Override
        protected ArrayList<ListItems> doInBackground(String... params) {
            loading = true;
            RestAPI api = new RestAPI();
            try {
                JSONObject jsonObj = api.GetFileList(params[0]);
                JsonParser parser = new JsonParser();
                list = parser.parseFilePath(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<ListItems> retailers) {
            try {
                for (int i = 0; i < retailers.size(); i++) {
                    data.add(retailers.get(i).getName());
                    data1.add(retailers.get(i).getSize());
                    data2.add(retailers.get(i).getType());
                    data3.add(retailers.get(i).getDate());
                    loading = false;
                    pDialog.dismiss();
                }
                cAdap.notifyDataSetChanged();
            } catch (NullPointerException | IllegalArgumentException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            pDialog.show();
            pDialog.setMessage("Loading files list please wait...");
        }
    }
}
