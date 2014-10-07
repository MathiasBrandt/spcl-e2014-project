package dk.rzs.pervasive;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by rnoe on 22/09/14.
 */

public class SensorService extends IntentService {
    private static final String TAG = "SensorService";
    public SensorService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
    }
}
