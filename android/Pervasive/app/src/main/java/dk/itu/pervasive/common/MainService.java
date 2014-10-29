package dk.itu.pervasive.common;

/**
 * Created by rnoe on 30/09/14.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import dk.itu.pervasive.common.sensorListeners.AccelSensorListener;
import dk.itu.pervasive.common.sensorListeners.GravitySensorListener;
import dk.itu.pervasive.common.sensorListeners.StopListeners;


/**
 * Created by rnoe on 24/09/14.
 */
public class MainService extends IntentService {
    private static final String TAG = "MainService";
    public MainService() {
        super(TAG);
    }

    public SensorManager mSensorManager;
    private Sensor mGravitySensor;
    private Sensor mAccelerometerSensor;
    private AccelSensorListener mAccelSensorListener;
    private GravitySensorListener mGravitySensorListener;




    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);

        // Check extras
        if (intent.getExtras()!=null) {
            Bundle bundle = intent.getExtras();

            if (bundle.containsKey("STOP_SENSORS")) {
                Log.i(TAG, "Intent has Stop_service");
                closeNotification(0);

                // try getting running active listener from preferences.


            }
        } else {
            User user = Common.getUserFromPreferences();

            createNotification("Hi " + user.getName() + "\nInterrupt service is now running... ", "Pervasive Project", "Turn over phone to set busy state",null, false, R.drawable.available);


            // Start GRAVITY SENSOR
            if (mGravitySensorListener == null) {
                startGravitySensor();
            }

            // if bundle
            // create switch case for bundle id setting which action to start
        }
    }

    private void startGravitySensor() {
        mGravitySensorListener = new GravitySensorListener(this);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mGravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (mGravitySensor != null) {
            mSensorManager.registerListener(mGravitySensorListener, mGravitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            Log.i(TAG, "Registerered for Gravity Sensor");

        }
    }

    public void disableGravitySensor(){
        createNotification("You are now set as BUSY!", "Pervasive Project", "DO NOT DISTURB", null, false, R.drawable.do_not_disturb);
        mSensorManager.unregisterListener(mGravitySensorListener);
        mSensorManager = null;
        startAccelSensor();
    }

    private void startAccelSensor() {
        Log.i(TAG, "Registering for Accelerometer Sensor");
        // ShakeDetector initialization
        mAccelSensorListener = new AccelSensorListener(this);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mAccelerometerSensor != null) {
            mSensorManager.registerListener(mAccelSensorListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.e(TAG, "AccelerometerListener error");
        }
    }

    public void handleShakeEvent(int count) {
        Log.i(TAG, "Count: " + count);
        if (count >= 2) {

            createNotification("You are now set as available", "Pervasive Project", "AVAILABLE", null, false, R.drawable.available);

            mSensorManager.unregisterListener(mAccelSensorListener);
            mSensorManager = null;

            startGravitySensor();
        }
    }


    private void createNotification(String ticker, String title, String text, PendingIntent i, boolean timer, int icon) {
        long[] vibrations = new long[]{0, 1000};
        Resources r = this.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), icon);

        Intent stopSensorIntent = new Intent(this, MainService.class);
        stopSensorIntent.putExtra("STOP_SENSORS", true);
        //stopSensorIntent.putExtra("Listener", )
        PendingIntent closeServiceIntent = PendingIntent.getService(this, 0, stopSensorIntent, 0);

        //Intent viewMessagesIntent = new Intent(this, ViewMessagesActivity.class);
        //PendingIntent viewMessagesIntent = PendingIntent.getActivity(this, 0, viewMessagesIntent);


        Notification notification = new Notification.Builder(this)
                .setLargeIcon(largeIcon)
                .setSmallIcon(android.R.drawable.ic_notification_clear_all)
                .setTicker(ticker)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(i)
                .setAutoCancel(true)
                //.setUsesChronometer(timer)
                .setVibrate(vibrations)
                .addAction(android.R.drawable.ic_delete, "Close service", closeServiceIntent)
                //.addAction(android.R.drawable.ic_menu_more, "View messages", viewMessagesIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    private void createCustomNotification(){

    }

    private void closeNotification(int notificationId) {
        StopListeners.setSTOP_SENSOR(true);
        Log.i(TAG, "STOP SENSOR: " + StopListeners.getSTOP_SENSOR());
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(notificationId);


    }
}
