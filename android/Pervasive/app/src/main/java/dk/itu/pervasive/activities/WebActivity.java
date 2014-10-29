package dk.itu.pervasive.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import dk.itu.pervasive.R;
import dk.itu.pervasive.common.Common;
import dk.itu.pervasive.common.WebAppInterface;

public class WebActivity extends Activity {
    WebView webView;
    ProgressDialog webViewProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = (WebView) findViewById(R.id.webView);

        // enable javascript in web view
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        // set javascript interface for callbacks
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        webViewProgressDialog = new ProgressDialog(this);
        webViewProgressDialog.setTitle("Loading ...");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if(webViewProgressDialog != null && !webViewProgressDialog.isShowing()) {
                    webViewProgressDialog.show();
                }

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(webViewProgressDialog != null && webViewProgressDialog.isShowing()) {
                    webViewProgressDialog.dismiss();
                }

                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (Uri.parse(url).getHost().equals("http://178.62.255.11")) {
                    // This is my web site, so do not override; let my WebView load the page
                    return false;
                }
                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });

        webView.loadUrl(Common.URL_CLIENT_PHONE);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            Boolean result;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Really Quit?")
                    .setMessage("Are you sure you want to exit the application?")
                    .setPositiveButton(R.string.btnYes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            WebActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.btnNo, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

            builder.create().show();
        }
    }
}
