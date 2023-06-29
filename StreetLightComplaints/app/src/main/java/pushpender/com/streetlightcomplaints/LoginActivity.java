package pushpender.com.streetlightcomplaints;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText etLoginID, etPassword;
    Button btnLogin;
    ProgressDialog pDialog;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(this);
        session = new SessionManager(LoginActivity.this);
        setContentView(R.layout.activity_login);
        etLoginID = (EditText) findViewById(R.id.etLoginID);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        if (session.isLoggedIn()) {
            finish();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncLogin().execute();
            }
        });
    }

    protected class AsyncLogin extends AsyncTask<String, JSONObject, UserData> {
        UserData userAuth = null;
        String loginID = etLoginID.getText().toString();
        String password = etPassword.getText().toString();

        @Override
        protected UserData doInBackground(String... params) {
            RestAPI api = new RestAPI();
            try {
                JSONObject jsonObject = api.GetDataSet("UserLogin '" + loginID + "','" + password + "'");
                JSONParser parser = new JSONParser();
                userAuth = parser.parseAuthentication(jsonObject);
            } catch (Exception e) {
                Log.d("AsyncLogin", e.getMessage());
            }
            return userAuth;
        }

        @Override
        protected void onPostExecute(UserData result) {
            if (result.getRespose() == 1) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                session.createLoginSession(result.getPersonName(), password, loginID);
                session.editor.commit();
                pDialog.dismiss();
                finish();
                Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                etPassword.setText("");
                pDialog.dismiss();
                Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            pDialog.setTitle("Authenticating");
            pDialog.setMessage("Connecting... Please wait..");
            pDialog.show();
            super.onPreExecute();
        }
    }
}
