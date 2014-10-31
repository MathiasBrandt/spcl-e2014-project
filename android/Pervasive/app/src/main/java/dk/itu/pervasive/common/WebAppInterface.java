package dk.itu.pervasive.common;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by brandt on 29/10/14.
 */

public class WebAppInterface {
    private Context mContext;

    public WebAppInterface(Context context) {
        this.mContext = context;
    }

    @JavascriptInterface
    public void messageReceived() {

    }

    @JavascriptInterface
    public void createNotification() {

    }

    @JavascriptInterface
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void nfcMessageAndId() {
        
    }

    @JavascriptInterface
    public void startMainService() {
        // if a user id was found

            Common.startMainService(mContext);

            // closes activity --> service keeps running
            //fragmentCallback.closeActivity();
    }
}
