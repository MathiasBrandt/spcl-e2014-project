package dk.rzs.mymodule.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by rnoe on 24/09/14.
 */
public class SensorService extends IntentService {
    private static final String TAG = "SensorService";
    public SensorService() {
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


            //Notifications notifications = new Notifications(this, )
            long[] vibrations = new long[]{0, 1000, 3};
            int repeat = 3;


            Resources r = this.getResources();
            //Pending intent not used yet
            //PendingIntent pi = PendingIntent
            //        .getActivity(this, 0, new Intent(this, PhotoGalleryActivity.class), 0);
            Notification notification = new Notification.Builder(this)
                    .setTicker(r.getString(R.string.accelerometer_notification))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(r.getString(R.string.accelerometer_notification_title))
                    .setContentText(r.getString(R.string.accelerometer_notification_text))
                            //.setContentIntent(pi)
                    .setAutoCancel(true)
                    .setUsesChronometer(true)
                    .setVibrate(vibrations)
                    .build();
            NotificationManager notificationManager = (NotificationManager)
                    this.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);

            mSensorManager.unregisterListener(mAccelSensorListener);
        }
    }
}
