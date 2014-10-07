package dk.itu.pervasive.sensorListeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;
import android.util.Log;

import dk.itu.pervasive.services.MainService;

/**
 * Created by rnoe on 30/09/14.
 */
public class AccelSensorListener implements SensorEventListener {

    /*
     * The gForce that is necessary to register as shake.
     * Must be greater than 1G (one earth gravity unit).
     * You can install "G-Force", by Blake La Pierre
     * from the Google Play Store and run it to see how
     *  many G's it takes to register a shake
     */
    private static final String TAG = "AccelSensorListener";
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.2F;
    private static final int SHAKE_SLOP_TIME_MS = 300;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 1000;

    private long mShakeTimestamp;
    private int mShakeCount;
    private MainService mMainService;

    public AccelSensorListener(MainService mainService) {
        mMainService = mainService;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Log.i(TAG, "In onSensorChanged()");
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        // gForce will be close to 1 when there is no movement.
        float gForce = FloatMath.sqrt(gX * gX + gY * gY + gZ * gZ);

        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            final long now = System.currentTimeMillis();
            // ignore shake events too close to each other (500ms)
            if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                return;
            }

            // reset the shake count after 3 seconds of no shakes
            if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                mShakeCount = 0;
            }

            mShakeTimestamp = now;
            mShakeCount++;

            mMainService.handleShakeEvent(mShakeCount);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}