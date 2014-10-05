package dk.itu.pervasive.services;

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
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import dk.itu.pervasive.models.User;
import dk.itu.pervasive.sensorListeners.AccelSensorListener;
import dk.itu.pervasive.sensorListeners.GravitySensorListener;
import dk.itu.pervasive.various.Common;

/**
 * Created by rnoe on 24/09/14.
 */
public class MainService extends IntentService {
    private static final String TAG = "MainService";
    public MainService() {
        super(TAG);
    }

    private SensorManager mSensorManager;
    private SensorManager mAccelSensorManager;
    private Sensor mGravitySensor;
    private Sensor mAccelerometerSensor;
    private AccelSensorListener mAccelSensorListener;
    private GravitySensorListener mGravitySensorListener;




    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
        User user = Common.getUserFromPreferences();
        createNotification("Hi " + user.getName() + "\nInterrupt service is now running... ", "Pervasive Project", "Turn over phone to set busy state", null, false );


        // Start GRAVITY SENSOR
        if (mGravitySensorListener == null) {
            startGravitySensor();
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
        createNotification("You are now set as BUSY!", "Pervasive Project", "DO NOT DISTURB", null, true);
        mSensorManager.unregisterListener(mGravitySensorListener);
        startAccelSensor();
    }

    private void startAccelSensor() {
        Log.i(TAG, "Registering for Accelerometer Sensor");
        // ShakeDetector initialization
        mAccelSensorListener = new AccelSensorListener(this);
        mAccelSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = mAccelSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (mAccelerometerSensor != null) {
            mAccelSensorManager.registerListener(mAccelSensorListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.e(TAG, "AccelerometerListener error");
        }
    }

    public void handleShakeEvent(int count) {
        Log.i(TAG, "Count: " + count);
        if (count >= 2) {
            createNotification("You are now set as available", "Pervasive Project", "AVAILABLE", null, false);

            mSensorManager.unregisterListener(mAccelSensorListener);
        }
    }

    private void createNotification(String ticker, String title, String text, PendingIntent i, boolean timer) {
        long[] vibrations = new long[]{0, 1000};
        Resources r = this.getResources();

        Notification notification = new Notification.Builder(this)
                .setTicker(ticker)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(i)
                .setAutoCancel(true)
                .setUsesChronometer(timer)
                .setVibrate(vibrations)
                //.setOngoing(false)
                .build();
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify("pervasive", 0, notification);
    }
}
