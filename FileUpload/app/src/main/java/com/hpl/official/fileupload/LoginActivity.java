package com.hpl.official.fileupload;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class LoginActivity extends Activity {

    EditText txtUsername, txtPassword;
    Button btnLogin;
    SessionManager session;
    ProgressDialog pDialog;
    String userAuth = null;
    ConnectionDetector cd;
    AlertDialogManager alert;
    AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Progress dialog
        pDialog = new ProgressDialog(this);
        builder = new AlertDialog.Builder(this);
        alert = new AlertDialogManager();
        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            builder = new AlertDialog.Builder(this);
            builder.setMessage("You are not connected to internet. Click OK to turn on internet")
                    .setTitle("Connetion Failed")
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
        }
        //Session Manager
        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        // Email, Password input text
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
//        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_SHORT).show();
        // Login button
        btnLogin = (Button) findViewById(R.id.btnLogin);
        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                cd = new ConnectionDetector(getApplicationContext());
                if (!cd.isConnectingToInternet()) {
                    alert.showAlertDialog(LoginActivity.this, "Connection Failed", "It seems your internet connection is not working. Turn your wifi/data ON", false);
                } else {
                    login();
                }
            }
        });
    }

    SpannableStringBuilder getErrorString(String s) {
        ForegroundColorSpan foregroundcolorspan = new ForegroundColorSpan(0xff000000);
        SpannableStringBuilder spannablestringbuilder = new SpannableStringBuilder(s);
        spannablestringbuilder.setSpan(foregroundcolorspan, 0, s.length(), 0);
        return spannablestringbuilder;
    }

    public void login() {
        String loginID = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        if (txtUsername.getText().length() == 0) {
            txtUsername.setError(getErrorString("Login ID can not be left blank"));
        }
        if (txtPassword.getText().length() == 0) {
            txtPassword.setError(getErrorString("Password can not be left blank"));
        }
        if (txtUsername.getText().length() != 0 && txtPassword.getText().length() != 0) {
            new AsyncLogin().execute(loginID, password);
            //session.createLoginSession(loginID, password);
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    @Override
    public void onBackPressed() {
        //Display alert message when back button has been pressed
        backButtonHandler();
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                LoginActivity.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.success);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        session.editor.clear();
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

    public void ScreenSupport() {

        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    class AsyncLogin extends AsyncTask<String, JSONObject, String> {
        String loginID = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        @Override
        protected String doInBackground(String... params) {
            RestAPI api = new RestAPI();
            try {
                JSONObject jsonObject = api.UserAuthentication(params[0], params[1]);
                JsonParser parser = new JsonParser();
                userAuth = parser.parseUserAuth(jsonObject);
            } catch (Exception e) {
                Log.d("AsyncLogin", e.getMessage());
            }
            return userAuth;
        }

        @Override
        protected void onPostExecute(String result) {
            String[] res = result.split(",");
            res[0].trim();
            res[1].trim();
            if (res[0].equals("0")) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                session.createLoginSession(res[1], password, loginID);
                session.editor.commit();
                pDialog.dismiss();
                finish();
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("name", res[1]);
                startActivity(i);
            } else {
                pDialog.dismiss();
                txtUsername.setText("");
                txtPassword.setText("");
                Toast.makeText(getApplicationContext(), "Username/Password is incorrect", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            pDialog.setTitle("Connecting to server");
            pDialog.setMessage("Connecting... Please wait..");
            showDialog();
            super.onPreExecute();
        }
    }
}
