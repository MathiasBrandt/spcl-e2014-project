package dk.itu.group10.spclsmartphoneapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private static final String PREFS_USER_ID_KEY = "USER_ID";
    private static final int PREFS_DEFAULT_USER_ID = -1;
    private static final String API_CREATE_USER_URL = "http://178.62.255.11/users";

    private EditText txtName;
    private EditText txtPhone;
    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // DEBUGGING
        saveUserId(PREFS_DEFAULT_USER_ID);

        // check preferences for user id
        int userId = getStoredUserId();

        // if a user id was found, navigate to main activity
        if(userId != PREFS_DEFAULT_USER_ID) {
            navigateToMainActivity();
        }

        txtName = (EditText) findViewById(R.id.txtName);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
    }

    private int getStoredUserId() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        int userId = preferences.getInt(PREFS_USER_ID_KEY, PREFS_DEFAULT_USER_ID);

        if(userId != PREFS_DEFAULT_USER_ID) {
            Log.d(TAG, String.format("User id found in preferences: %d", userId));
        } else {
            Log.d(TAG, String.format("No user id found in preferences (%d)", userId));
        }

        return userId;
    }

    private void saveUserId(int userId) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        Log.d(TAG, String.format("Saving new user id to preferences: %d", userId));

        preferences.edit().putInt(PREFS_USER_ID_KEY, userId).commit();
    }

    private void navigateToMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void btnCreateUserOnClick(View v) {
        new CreateUserAsyncTask().execute();
    }

    class CreateUserAsyncTask extends AsyncTask<Void, Void, Integer> {
        private static final String NAME_KEY = "Name";
        private static final String PHONE_KEY = "Phone";
        private static final String EMAIL_KEY = "Email";

        private ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        private String name;
        private String phone;
        private String email;

        @Override
        protected Integer doInBackground(Void... voids) {
            // TODO: validate input fields

            int userId = -1;

            // create a user and get the user id
            userId = createUser();

            return userId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // show loading dialog
            dialog.setTitle("Creating User");
            dialog.setMessage("Please wait ...");
            dialog.show();

            // fetch variables from text fields
            name = txtName.getText().toString().trim();
            phone = txtPhone.getText().toString().trim();
            email = txtEmail.getText().toString().trim();

            Log.d(TAG, String.format("Creating new user: %s, %s, %s", name, phone, email));
        }

        @Override
        protected void onPostExecute(Integer userId) {
            super.onPostExecute(userId);

            // save user id to preferences
            saveUserId(userId);

            // dismiss the loading dialog
            dialog.dismiss();

            // navigate to main activity
            navigateToMainActivity();
        }

        private int createUser() {
            HttpClient client = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(API_CREATE_USER_URL);
            HttpResponse response = null;

            User user = new User(name, phone, email, "1");
            StringEntity params = null;
            try {
                params = new StringEntity(user.getJson());
            } catch (UnsupportedEncodingException e) {
                Log.d(TAG, "UnsupportedEncodingException");
            }
            postRequest.setEntity(params);
            postRequest.addHeader("content-type", "application/json");

            try {
                response = client.execute(postRequest);
            } catch (ClientProtocolException e) {
                Log.d(TAG, "ClientProtocolException");
            } catch (IOException e) {
                Log.d(TAG, "IOException");
            }

            Log.d(TAG, "Status code: " + response.getStatusLine().getStatusCode());

            if(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // read the response (the user id)
                try {
                    InputStreamReader isr = new InputStreamReader(response.getEntity().getContent());
                    char[] buffer = new char[512];
                    isr.read(buffer);
                    Log.d(TAG, new String(buffer));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            Log.d(TAG, "Could not create user");
            return -1;
        }
    }
}
