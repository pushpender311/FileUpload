package pushpender.com.streetlightcomplaints;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView listPending, listDisplay, listStatus;
    TextView etName, txtLogout;
    EditText etComplaint, etDate, etTown, etZone, etPole, etAddress, etLandmark, etCustomerName, etCustomerPhn, etRemark, etcompletion;
    ImageView imgCal;
    Spinner spCluster, spComplaintStatus;
    TableRow tabCompletion;
    SessionManager session;
    ArrayList<String> data, data1, data2, data3, data4, data5, data6, data7, listCluster, listCurrentStatus;
    String name, LoginID;
    String ComplaintID;
    ComplaintAdap complaintAdap;
    DisplayLogAdap displayLogAdap;
    ArrayAdapter<String> adap, adapCluster, adapStatus;
    Button btnModify, btnDisplayLogs;
    String status[] = {"Closed", "Open", "Working", "Under Observation"};
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new
            DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }
            };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = findViewById(R.id.etName);
        txtLogout = findViewById(R.id.txtLogout);
        listPending = findViewById(R.id.listPending);
        listDisplay = findViewById(R.id.listDisplay);
        listStatus = findViewById(R.id.listStatus);
        etComplaint = findViewById(R.id.etComplaint);
        etDate = findViewById(R.id.etDate);
        etTown = findViewById(R.id.etTown);
        etZone = findViewById(R.id.etZone);
        etPole = findViewById(R.id.etPole);
        etAddress = findViewById(R.id.etAddress);
        etLandmark = findViewById(R.id.etLandmark);
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerPhn = findViewById(R.id.etCustomerPhn);
        etRemark = findViewById(R.id.etRemark);
        etcompletion = findViewById(R.id.etCompletion);
        imgCal = findViewById(R.id.imgCal);
        spCluster = findViewById(R.id.spCluster);
        spComplaintStatus = findViewById(R.id.spCurrentStatus);
        btnDisplayLogs = findViewById(R.id.btnDisplayLog);
        btnModify = findViewById(R.id.btnModify);
        tabCompletion = findViewById(R.id.tabComlpletion);
        session = new SessionManager(MainActivity.this);
        session.checkLogin();
        HashMap<String, String> hashMap = session.getUserDetails();
        name = hashMap.get(SessionManager.KEY_NAME);
        LoginID = hashMap.get(SessionManager.LOGIN_ID);
        etName.setText(name);
        data = new ArrayList<>();
        data1 = new ArrayList<>();
        data2 = new ArrayList<>();
        data3 = new ArrayList<>();
        data4 = new ArrayList<>();
        data5 = new ArrayList<>();
        data6 = new ArrayList<>();
        data7 = new ArrayList<>();
        listCluster = new ArrayList<>();
        listCurrentStatus = new ArrayList<>();
        etComplaint.setEnabled(false);
        etDate.setEnabled(false);
        etTown.setEnabled(false);
        spCluster.setEnabled(false);
        etZone.setEnabled(false);
        etPole.setEnabled(false);
        etAddress.setEnabled(false);
        etLandmark.setEnabled(false);
        etCustomerName.setEnabled(false);
        etCustomerPhn.setEnabled(false);
        etRemark.setEnabled(false);
        spComplaintStatus.setEnabled(false);
        etcompletion.setEnabled(false);
        imgCal.setEnabled(false);
        tabCompletion.setVisibility(View.GONE);
        new AsyncPending().execute();
        new AsyncGetStatus().execute();
        complaintAdap = new ComplaintAdap(MainActivity.this, data, data1);
        listPending.setAdapter(complaintAdap);
        adapStatus = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, status);
        spComplaintStatus.setAdapter(adapStatus);

        if (etName.getText().toString().equals("")) {
            finish();
        }
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Logout..!!");
                alertDialog.setMessage("Are you sure you want to Logout?");
                alertDialog.setIcon(R.mipmap.ic_launcher);
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                session.logoutUser();
                                finish();
                            }
                        });
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });
        listPending.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listDisplay.setVisibility(View.GONE);
                listStatus.setVisibility(View.VISIBLE);
                btnDisplayLogs.setText("Display Log");
                ComplaintID = listPending.getItemAtPosition(position).toString();
                new AsyncGetComplainDet().execute(ComplaintID);
            }
        });
        btnDisplayLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ComplaintID == null) {
                        Toast.makeText(MainActivity.this, "Please Select Complaint then try ", Toast.LENGTH_SHORT).show();
                    } else {
                        DisplayLog();
                    }
                } catch (NullPointerException ignored) {
                }
            }
        });
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ComplaintID == null) {
                    Toast.makeText(MainActivity.this, "Please Select Complaint then try ", Toast.LENGTH_SHORT).show();
                } else UpdateModify();
            }
        });
        spComplaintStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String status = spComplaintStatus.getItemAtPosition(position).toString();
                if (status.equals("Closed")) {
                    tabCompletion.setVisibility(View.VISIBLE);
                } else {
                    tabCompletion.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imgCal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(MainActivity.this, date, myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });
    }

    private void updateLabel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
        etcompletion.setText(dateFormat.format(myCalendar.getTime()));
    }

    public void UpgradeFunction() {
        try {
            String ComplitionDate = "'" + etcompletion.getText().toString() + "'";
            if (!spComplaintStatus.getSelectedItem().toString().toLowerCase().equals("Closed".toLowerCase()))
                ComplitionDate = "NULL";
            String action = "Exec UpdateComplaint @ComplaintID =" + ComplaintID + ",@Zone='" +
                    etZone.getText().toString() + "'," + "@PoleNo='" + etPole.getText().toString()
                    + "',@Address='" + etAddress.getText().toString() + "'," +
                    "@LandMark='" + etLandmark.getText().toString() + "',@CustomerName='" +
                    etCustomerName.getText().toString() + "'," +
                    "@CustomerNo='" + etCustomerPhn.getText().toString() +
                    "',@Remarks='" + etRemark.getText().toString() + "'," +
                    "@EnterBy='" + LoginID + "',@StatusID=" +
                    spComplaintStatus.getSelectedItemPosition() +
                    ",@CompletionDate=" + ComplitionDate;
            new AsyncUpdate().execute(action);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void UpdateModify() {
        if (btnModify.getText().equals("Modify")) {
            etComplaint.setEnabled(true);
            etDate.setEnabled(true);
            etTown.setEnabled(false);
            spCluster.setEnabled(false);
            etZone.setEnabled(true);
            etPole.setEnabled(true);
            etAddress.setEnabled(true);
            etLandmark.setEnabled(true);
            etCustomerName.setEnabled(true);
            etCustomerPhn.setEnabled(true);
            etRemark.setEnabled(true);
            spComplaintStatus.setEnabled(true);
            etcompletion.setEnabled(true);
            imgCal.setEnabled(true);
            etRemark.setText("");
            btnModify.setText("Update");
            listPending.setEnabled(false);
            btnDisplayLogs.setText("Cancel");

        } else if (btnModify.getText().equals("Update")) {
            String msg = "";
            if (etCustomerName.getText().length() == 0)
                msg = msg + "Customer Name can not left blank" + "\n";
            if (etCustomerPhn.getText().length() == 0)
                msg = msg + "Customer Phone can not left blank" + "\n";
            if (etRemark.getText().length() == 0)
                msg = msg + "Remarks can not left blank" + "\n";
            if (spComplaintStatus.getSelectedItem().toString().toLowerCase().equals("closed")) {
                if (etcompletion.getText().length() == 0) {
                    msg = msg + "Completion date can not left blank";
                }
            }
            if (msg.length() != 0) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Sure to update?");
            alertDialog.setMessage("Are you sure you want to update the complaint?");
            alertDialog.setIcon(R.drawable.cal);
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            btnModify.setText("Modify");
                            btnDisplayLogs.setText("Display Log");
                            etComplaint.setEnabled(false);
                            etDate.setEnabled(false);
                            etTown.setEnabled(false);
                            spCluster.setEnabled(false);
                            etZone.setEnabled(false);
                            etPole.setEnabled(false);
                            etAddress.setEnabled(false);
                            etLandmark.setEnabled(false);
                            etCustomerName.setEnabled(false);
                            etCustomerPhn.setEnabled(false);
                            etRemark.setEnabled(false);
                            spComplaintStatus.setEnabled(false);
                            listPending.setEnabled(true);
                            etcompletion.setEnabled(false);
                            imgCal.setEnabled(false);
                            UpgradeFunction();
                            complaintAdap.clear();
                            new AsyncPending().execute();
                            adap.clear();
                            new AsyncGetStatus().execute();
                            new AsyncGetComplainDet().execute(ComplaintID);
                        }
                    });
            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
    }

    public void DisplayLog() {
        if (btnDisplayLogs.getText().equals("Display Log")) {
            data3.clear();
            data4.clear();
            data5.clear();
            data6.clear();
            listStatus.setVisibility(View.GONE);
            listDisplay.setVisibility(View.VISIBLE);
            btnDisplayLogs.setText("Hide Log");
            new AsyncLogs().execute(ComplaintID);
            if (displayLogAdap != null) {
                displayLogAdap.clear();
                displayLogAdap.notifyDataSetChanged();
            }
        } else if (btnDisplayLogs.getText().equals("Hide Log")) {
            btnDisplayLogs.setText("Display Log");
            listDisplay.setVisibility(View.GONE);
            listStatus.setVisibility(View.VISIBLE);
        } else if (btnDisplayLogs.getText().equals("Cancel")) {
            etComplaint.setEnabled(false);
            etDate.setEnabled(false);
            etTown.setEnabled(false);
            spCluster.setEnabled(false);
            etZone.setEnabled(false);
            etPole.setEnabled(false);
            etAddress.setEnabled(false);
            etLandmark.setEnabled(false);
            etCustomerName.setEnabled(false);
            etCustomerPhn.setEnabled(false);
            etRemark.setEnabled(false);
            spComplaintStatus.setEnabled(false);
            listPending.setEnabled(true);
            etcompletion.setEnabled(false);
            imgCal.setEnabled(false);
            tabCompletion.setVisibility(View.GONE);
            btnDisplayLogs.setText("Display Log");
            btnModify.setText("Modify");
            new AsyncGetComplainDet().execute(ComplaintID);
        }
    }

    class AsyncPending extends AsyncTask<String, JSONObject, ArrayList<PendingComp>> {
        ArrayList<PendingComp> list = null;

        @Override
        protected ArrayList<PendingComp> doInBackground(String... params) {
            RestAPI api = new RestAPI();
            JSONObject jsonObject = null;
            try {
                jsonObject = api.GetDataSet("GetPending '" + LoginID + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONParser parser = new JSONParser();
            list = parser.parseItems(jsonObject);
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<PendingComp> result) {
            for (int i = 0; i < result.size(); i++) {
                data.add(result.get(i).getComplaintNo());
                data1.add(String.valueOf(result.get(i).getComplaintID()));
            }
            complaintAdap.notifyDataSetChanged();
            super.onPostExecute(result);
        }
    }

    class AsyncGetComplainDet extends AsyncTask<String, JSONObject, GetComplainDet> {

        @Override
        protected GetComplainDet doInBackground(String... params) {
            GetComplainDet det;

            RestAPI api = new RestAPI();
            JSONObject jsonObject = null;
            try {
                jsonObject = api.GetDataSet("GetComplainDet " + params[0] + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONParser parser = new JSONParser();
            det = parser.parseGetComplaintDet(jsonObject);
            return det;
        }

        @Override
        protected void onPostExecute(GetComplainDet result) {
            etComplaint.setText(result.getComplaintNo());
            etDate.setText(result.getDate());
            etTown.setText(result.getTown());

            listCluster.clear();
            listCluster.add(result.getCluster());
            adapCluster = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, listCluster);
            spCluster.setAdapter(adapCluster);
            adapCluster.notifyDataSetChanged();
            etZone.setText(result.getZone());
            etPole.setText(result.getPole());
            etAddress.setText(result.getAddress());
            etLandmark.setText(result.getLandmark());
            etCustomerName.setText(result.getCustomerName());
            etCustomerPhn.setText(result.getCustomerPhn());
            etRemark.setText(result.getRemark());
            spComplaintStatus.setSelection(result.getCurrentStatus());
            adapStatus.notifyDataSetChanged();
            super.onPostExecute(result);
        }
    }

    class AsyncGetStatus extends AsyncTask<String, JSONObject, ArrayList<GetStatus>> {
        @Override
        protected ArrayList<GetStatus> doInBackground(String... params) {
            ArrayList<GetStatus> status;
            RestAPI api = new RestAPI();
            JSONObject jsonObject = null;
            try {
                jsonObject = api.GetDataSet("GetStatus '" + LoginID + "'");
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONParser parser = new JSONParser();
            status = parser.parseGetStatus(jsonObject);
            return status;
        }

        @Override
        protected void onPostExecute(ArrayList<GetStatus> result) {
            try {
                for (int i = 0; i < result.size(); i++) {
                    data7.add(result.get(i).getDescription() + " : " + result.get(i).getFigure());
                    adap = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, data7);
                    listStatus.setAdapter(adap);
                }
                adap.notifyDataSetChanged();
            } catch (NullPointerException ignored) {
            }

            super.onPostExecute(result);
        }
    }

    class AsyncLogs extends AsyncTask<String, JSONObject, ArrayList<Logs>> {
        ArrayList<Logs> list = null;

        @Override
        protected ArrayList<Logs> doInBackground(String... params) {
            RestAPI api = new RestAPI();
            JSONObject jsonObject = null;
            try {
                jsonObject = api.GetDataSet("GetComplainDet " + params[0] + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONParser parser = new JSONParser();
            list = parser.parseLogs(jsonObject);
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Logs> result) {
            try {
                for (int i = 0; i < result.size(); i++) {
                    data3.add(result.get(i).getFieldUpdated());
                    data4.add(result.get(i).getRemarks());
                    data5.add(result.get(i).getEnterBy());
                    data6.add(result.get(i).getEntryTime());
                    displayLogAdap = new DisplayLogAdap(MainActivity.this, data3, data4, data5, data6);
                    displayLogAdap.notifyDataSetChanged();
                    listDisplay.setAdapter(displayLogAdap);
                }
            } catch (NullPointerException ignored) {
            }
            super.onPostExecute(result);
        }
    }

    class AsyncUpdate extends AsyncTask<String, JSONObject, Update> {

        @Override
        protected Update doInBackground(String... params) {
            Update det;

            RestAPI api = new RestAPI();
            JSONObject jsonObject = null;
            try {
                jsonObject = api.GetDataSet(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONParser parser = new JSONParser();
            det = parser.parseUpdate(jsonObject);
            return det;
        }

        @Override
        protected void onPostExecute(Update result) {
            if (result.getColumn1().equals("0")) {
                Toast.makeText(MainActivity.this, result.getColumn2(), Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(result);
        }
    }
}

