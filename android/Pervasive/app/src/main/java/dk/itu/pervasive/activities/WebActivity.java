package dk.itu.pervasive.activities;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import dk.itu.pervasive.R;
import dk.itu.pervasive.common.Common;

public class WebActivity extends Activity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = (WebView) findViewById(R.id.webView);

        // enable javascript
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        // open links in the webview instead of browser
        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl(Common.URL_CLIENT_PHONE);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
