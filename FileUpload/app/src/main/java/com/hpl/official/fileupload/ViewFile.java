package com.hpl.official.fileupload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ViewFile extends Activity {
    WebView showFile;
    String Link;
    ConnectionDetector cd;
    AlertDialogManager alert;
    String[] linkType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_file);
        showFile = (WebView) findViewById(R.id.webView);
        cd = new ConnectionDetector(this);
        Intent intent = getIntent();
        Link = intent.getStringExtra("link");
        linkType = Link.split("\\.");
        int i = linkType.length;
        String exp = linkType[i - 1];
        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            alert.showAlertDialog(ViewFile.this, "Connection Failed", "It seems your internet connection is not working. Turn your wifi/data ON", false);
        } else if (exp.equals("pdf") || exp.equals("docx") || exp.equals("doc") || exp.equals("xls") || exp.equals("xlsx") || exp.equals("ppt") || exp.equals("pptx")) {
            startWebView("https://docs.google.com/gview?embedded=true&url=" + Link);
        } else if (exp.equals("jpg") || exp.equals("jpeg") || exp.equals("gif") || exp.equals("png") || exp.equals("bmp") || exp.equals("ico") || exp.equals("htm") || exp.equals("html") || exp.equals("txt")) {
            startWebView(Link);
        } else {
            Uri uri = Uri.parse(Link); //
            Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent1);
        }
    }

    private void startWebView(String url) {
        showFile.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog = new ProgressDialog(ViewFile.this);
                progressDialog.setMessage("Page is loading..");
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        });
        showFile.getSettings().setJavaScriptEnabled(true);
        showFile.getSettings().setBuiltInZoomControls(true);
        showFile.getSettings().setLoadWithOverviewMode(true);
        showFile.getSettings().setUseWideViewPort(true);
        showFile.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        showFile.setScrollbarFadingEnabled(false);
        showFile.loadUrl(url);
    }
}
