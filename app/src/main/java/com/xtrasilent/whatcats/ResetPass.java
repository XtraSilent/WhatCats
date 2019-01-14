package com.xtrasilent.whatcats;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ResetPass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        WebView mywebview = findViewById(R.id.webView);

        mywebview.loadUrl("http://catmeow.tk/reset");
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.setWebViewClient(new WebViewClient());
        mywebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }

}
