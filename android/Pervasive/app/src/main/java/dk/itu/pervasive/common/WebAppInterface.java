package dk.itu.pervasive.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

        // Start activity if not in foreground
        //PendingIntent webActivity = new PendingIntent(mContext, 0, WebActivity.class, 0);

        // Create notification
        Bitmap largeIcon = BitmapFactory.decodeResource(mContext.getResources(), android.R.drawable.alert_dark_frame);
        Notification notification = new Notification.Builder(mContext)
                .setLargeIcon(largeIcon)
                .setSmallIcon(android.R.drawable.ic_notification_clear_all)
                .setTicker("YOU'VE HAD A VISIT...")
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle("YOU'VE HAD A VISITOR")
                .setContentText("Click here for more info!")
                //.setContentIntent(i)
                .setAutoCancel(true)
                        //.setUsesChronometer(timer)
                //.setVibrate(vibrations)
                //.addAction(android.R.drawable.ic_delete, "Close service", closeServiceIntent)
                //.addAction(android.R.drawable.ic_menu_more, "View messages", viewMessagePendingsIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

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

    @JavascriptInterface
    public void stopMainService() {
        Common.stopMainService(mContext);
    }
}
