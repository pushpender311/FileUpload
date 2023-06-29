/*
 * Design and Developed by PUSHPENDER BHANDARI
 *
 */
package com.hpl.official.fileupload;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MainActivity extends Activity {

    private static final int SELECT_FILE = 2;
    SessionManager session;
    Button lblName, btnCapturePicture, btnUpload, btnLoadRecent, gotoWebsite, btnLogout;
    TextView txtPercentage, txtFilePath;
    ProgressDialog pDialog;
    Bitmap thumbnail;
    ProgressBar progressBar;
    ImageView imageView;
    String selectedImagePath;
    ConnectionDetector cd;
    long totalSize = 0;
    HashMap<String, String> user;
    AlertDialogManager alert;
    AlertDialog.Builder builder;
    String loginID, name, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pDialog = new ProgressDialog(this);
        lblName = (Button) findViewById(R.id.UserName);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        btnLoadRecent = (Button) findViewById(R.id.loadRecent);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        txtFilePath = (TextView) findViewById(R.id.txtFilePath);
        gotoWebsite = (Button) findViewById(R.id.gotoWebsite);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imageView);
        session = new SessionManager(getApplicationContext());
        txtPercentage.setVisibility(View.GONE);
        cd = new ConnectionDetector(this);
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
        }
        new AsyncValidation().execute();
        session.checkLogin();
        user = session.getUserDetails();
        name = user.get(SessionManager.KEY_NAME);
        loginID = user.get(SessionManager.LOGIN_ID);
        password = user.get(SessionManager.KEY_PASSWORD);
        lblName.setText(name);
        try {
            if (lblName.getText().toString().equals("")) {
                finish();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(null);
                txtFilePath.setText("");
                txtPercentage.setText("0");
                selectImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cd.isConnectingToInternet()) {
                    alert.showAlertDialog(MainActivity.this, "Connection Failed", "It seems your internet connection is not working. Turn your wifi/data ON", false);
                } else if (!txtFilePath.getText().toString().equals("")) {
                    txtPercentage.setVisibility(View.VISIBLE);
                    btnUpload.setClickable(false);
                    new UploadFileToServer().execute();
                } else {
                    Toast.makeText(MainActivity.this, "Select file to upload first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gotoWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://medvault.scsworld.in/LoginMobile.aspx?mpUserID=" + loginID + "&mpPWD=" + password + "");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                // new callWeb().execute();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure to logout? ").setTitle("Hello " + lblName.getText().toString())
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                session.logoutUser();
                                finish();
                                session.editor.clear();
                                session.editor.commit();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        btnLoadRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoadRecent.class);
                intent.putExtra("loginid", loginID);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE) {
            try {
                selectedImagePath = null;
                Uri selectedImageUri = data.getData();
                ;
                if (selectedImageUri.getScheme().equals("content")) {
                    Cursor cursor = getContentResolver().query(selectedImageUri, null, null, null, null);
                    try {
                        if (cursor != null && cursor.moveToFirst()) {
                            // selectedImagePath = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                            selectedImagePath = cursor.getString(column_index);
                        }
                    } finally {
                        cursor.close();
                    }
                }
                if (selectedImagePath == null) {
                    selectedImagePath = selectedImageUri.getPath();
                }

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                thumbnail = BitmapFactory.decodeFile(selectedImagePath, options);
                imageView.setImageBitmap(thumbnail);
                txtFilePath.setText(selectedImagePath);
            } catch (IllegalArgumentException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                try {
                    if (items[item].equals("Choose from Library")) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    showAlert(e.getMessage());
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        imageView.setImageBitmap(null);
                        txtFilePath.setText("");
                        txtPercentage.setText("0");
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected class AsyncValidation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String val = "1";
            Validation validation = new Validation();
            try {
                JSONObject jsonObject = validation.getValidation("medvault.scsworld.in");
                val = jsonObject.getString("Value");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return val;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("1")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Error");
                alertDialog.setMessage(result);
                alertDialog.setCancelable(false);
                alertDialog.setIcon(R.drawable.success);
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        session.logoutUser();
                        finish();
                    }
                });
                alertDialog.show();
            }
        }
    }

    /**
     * Uploading the file to server
     */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(progress[0]);
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")

        private String uploadFile() {
            String responseString;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        publishProgress((int) ((num / (float) totalSize) * 100));
                    }
                });
                File sourceFile = new File(selectedImagePath);
                entity.addPart("image", new FileBody(sourceFile));
                entity.addPart("loginID", new StringBody(loginID));
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(MainActivity.class.getSimpleName(), "Response from server: " + result);
            // showing the server response in an alert dialog
            showAlert(result);
            txtPercentage.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            progressBar.setProgress(0);
            btnUpload.setClickable(true);
            super.onPostExecute(result);
        }
    }
}
