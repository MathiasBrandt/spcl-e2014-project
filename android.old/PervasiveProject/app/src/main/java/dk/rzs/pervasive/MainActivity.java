package dk.rzs.pervasive;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import static android.util.FloatMath.cos;
import static android.util.FloatMath.sin;
import static android.util.FloatMath.sqrt;
import static android.widget.Toast.*;


public class MainActivity extends Activity {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private static final String TAG = "TestRotation";
    private TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.TextView);
        textView.setMovementMethod(new ScrollingMovementMethod());

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (mSensor != null) {
            mSensorManager.registerListener(mySensorEventListener, mSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            Log.i(TAG, "Registerered for Gravity Sensor");

        } else {
            createToast("DEVICE WAS TURNED:)");
        }
    }

        private SensorEventListener mySensorEventListener = new SensorEventListener() {

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1    ];
                float z = event.values[2];
                if (z < -8.0) {
                    createToast("DISPLAY WAS TURNED UPSIDE DOWN:)");

                }
                Log.i(TAG, "x: " + x + "y: " + y + "z: " + z);
                textView.append("x: " + x + "y: " + y + "z: " + z + "\n");



            }
        };


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            if (mSensor != null) {
                mSensorManager.unregisterListener(mySensorEventListener);
            }
        }

    public void createToast(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();

    }
    }
