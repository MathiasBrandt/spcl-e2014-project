package dk.itu.pervasive.common.sensorListeners;

/**
 * Created by rnoe on 15/10/14.
 */
public class StopListeners {

    // This class is needed because MainService can't keep state when
    // called by pending intent. Basically just keeps a number to register if
    // sensor should be turned off

    private static boolean STOP_SENSOR;

    private StopListeners() {}

    public static boolean getSTOP_SENSOR() {
        return STOP_SENSOR;
    }

    public static void setSTOP_SENSOR(boolean STOP_SENSOR) {
        StopListeners.STOP_SENSOR = STOP_SENSOR;
    }
}
