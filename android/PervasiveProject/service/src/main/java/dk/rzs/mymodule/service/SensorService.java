package dk.rzs.mymodule.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
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
    private Sensor mSensor;
    private GravitySensorListener gravitySensorListener;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);

        // Start GRAVITY SENSOR
        if (gravitySensorListener == null) {
            startGravitySensor();

        }
    }

    private void startGravitySensor() {
        gravitySensorListener = new GravitySensorListener(this);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (mSensor != null) {
            mSensorManager.registerListener(gravitySensorListener, mSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            Log.i(TAG, "Registerered for Gravity Sensor");

        }
    }

    public void disableGravitySensor(){
        mSensorManager.unregisterListener(gravitySensorListener);
    }
}
