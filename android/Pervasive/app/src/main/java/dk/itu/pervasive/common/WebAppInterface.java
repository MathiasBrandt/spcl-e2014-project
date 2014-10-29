package dk.itu.pervasive.common;

import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * Created by brandt on 29/10/14.
 */

public class WebAppInterface {
    private Context context;

    public WebAppInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void MessageReceived() {

    }

    public void CreateNotification() {

    }

    public void NogetMedNFC() {
        
    }
}
