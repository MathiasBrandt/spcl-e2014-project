package mathiasbrandt.com.socketiotest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


public class MainActivity extends Activity {
    static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new socketAsyncTask().execute();
    }

    class socketAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d(TAG, "executing socket.io stuff");

                String url = "http://178.62.255.11:3000";
                final Socket socket = IO.socket(url);

                socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Log.d(TAG, "Connected - emitting");

                        JSONObject jsonObj = new JSONObject();

                        try {
                            jsonObj.put("user_id", 1);
                            jsonObj.put("status_id", 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        socket.emit("setStatus", jsonObj.toString());
                    }
                }).on("statusChanged", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Log.d(TAG, "Status Changed!");
                        for (Object o : args) {
                            Log.d(TAG, "arg: " + o);
                        }
                    }
                });

                socket.connect();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
