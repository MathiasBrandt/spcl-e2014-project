package dk.rzs.servicesensor.pervasive;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by rnoe on 23/09/14.
 */
public class SensorsService extends IntentService {
    private static final String TAG = "SensorService";

    public SensorsService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
    }
}
