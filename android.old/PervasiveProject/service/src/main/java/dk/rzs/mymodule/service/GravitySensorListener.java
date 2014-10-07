package dk.rzs.mymodule.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by rnoe on 24/09/14.
 */
public class GravitySensorListener implements SensorEventListener {
    private static final String TAG = "GravitySensorListener";
    private SensorService mSensorService;

    public GravitySensorListener(SensorService sensorService) {
        mSensorService = sensorService;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (z < -9.0) {
           Log.i(TAG, "DISPLAY WAS TURNED UPSIDE DOWN:)");


            // Notification setting below are activated when gravity sensor detects value -9
            // Notification should be moved to another separate class as we need it often, and to remove responsibility from this class.
            // create notification that sensor has started
            Resources r = mSensorService.getResources();

            //Pending intent not used yet
            //PendingIntent pi = PendingIntent
            //        .getActivity(this, 0, new Intent(this, PhotoGalleryActivity.class), 0);
            Notification notification = new Notification.Builder(mSensorService)
                    .setTicker(r.getString(R.string.gravity_notification))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(r.getString(R.string.gravity_notification_title))
                    .setContentText(r.getString(R.string.gravity_notification_text))
                            //.setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
            NotificationManager notificationManager = (NotificationManager)
                    mSensorService.getSystemService(mSensorService.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);

            // Implement communication with server to set situated display to busy state

            // Disable gravity sensor once it has enable busy state off situated display
            mSensorService.disableGravitySensor();

            // Implement start of accelerometer sensor to listen for shake event
        }
        Log.i(TAG, "x: " + x + "y: " + y + "z: " + z);
    }
}
