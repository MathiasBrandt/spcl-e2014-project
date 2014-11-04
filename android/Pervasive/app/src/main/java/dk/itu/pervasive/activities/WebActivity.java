package dk.itu.pervasive.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.ndeftools.Record;
import org.ndeftools.wellknown.TextRecord;

import java.util.List;

import dk.itu.pervasive.R;
import dk.itu.pervasive.common.Common;
import dk.itu.pervasive.common.WebAppInterface;

public class WebActivity extends Activity {
    WebView webView;
    ProgressDialog webViewProgressDialog;

    private static final String TAG = "WebActivity";
    private String tabletId;
    private boolean messageView = false;
    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;


    public void enableForegroundMode() {
        Log.d(TAG, "enableForegroundMode");

        // foreground mode gives the current active application priority for reading scanned tags
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED); // filter for tags
        IntentFilter[] writeTagFilters = new IntentFilter[] {tagDetected};
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);
    }

    public void disableForegroundMode() {
        Log.d(TAG, "disableForegroundMode");

        nfcAdapter.disableForegroundDispatch(this);
    }

   @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

       if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
           try {

               Parcelable[] messageFromTag = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
               if (messageFromTag != null) {
                   Log.d(TAG, "Found " + messageFromTag.length + " NDEF messages");

                   // parse to records
                   for (int i = 0; i < messageFromTag.length; i++) {
                       try {
                           List<Record> records = new org.ndeftools.Message((NdefMessage) messageFromTag[i]);

                           Log.d(TAG, "Found " + records.size() + " records in message " + i);

                           for (int k = 0; k < records.size(); k++) {
                               Log.d(TAG, " Record #" + k + " is of class " + records.get(k).getClass().getSimpleName());

                               Record record = records.get(k);

                               if (record instanceof TextRecord) {
                                   TextRecord tRec = (TextRecord) record;
                                   tabletId = tRec.getText().trim();
                                   Log.i(TAG, "Text is " + tRec.getText());
                                   createToast(tabletId);
                               }
                           }
                       } catch (FormatException e) {
                           e.printStackTrace();
                       }
                   }
               }
           } catch (Exception e) {
               Log.e(TAG, "Some problem");
           }

           webView.loadUrl("javascript:nfcReceived("+ tabletId + ")");
       }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        // disable action bar
        ActionBar ab = getActionBar();
        ab.hide();

        // initialize NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);



        webView = (WebView) findViewById(R.id.webView);

        // enable javascript in web view
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        // set javascript interface for callbacks
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        webViewProgressDialog = new ProgressDialog(this);
        webViewProgressDialog.setTitle("Loading ...");
        webViewProgressDialog.setCancelable(false);

        webView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (webViewProgressDialog != null && !webViewProgressDialog.isShowing()) {
                    webViewProgressDialog.show();
                }

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (webViewProgressDialog != null && webViewProgressDialog.isShowing()) {
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
        if (webView.canGoBack()) {
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


    public void createToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");

        super.onResume();

        enableForegroundMode();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");

        super.onPause();

        disableForegroundMode();
    }
}
