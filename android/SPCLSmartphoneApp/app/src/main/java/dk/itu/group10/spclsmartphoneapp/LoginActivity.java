package dk.itu.group10.spclsmartphoneapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
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

        // TODO: DEBUGGING REMOVE THIS
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

    /***
     * Retrieves the user id stored in SharedPreferences.
     * @return the stored user id or -1 if no user is exists in SharedPreferences.
     */
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

    /***
     * Saves a user id to SharedPreferences.
     * @param userId the user id to save.
     */
    private void saveUserId(int userId) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

        Log.d(TAG, String.format("Saving new user id to preferences: %d", userId));

        preferences.edit().putInt(PREFS_USER_ID_KEY, userId).apply();
    }

    /***
     * Navigates the user interface to MainActivity.
     */
    private void navigateToMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }

    /***
     * Click handler for the "Create User" button.
     * @param v
     */
    public void btnCreateUserOnClick(View v) {
        new CreateUserAsyncTask().execute();
    }

    /***
     * This class represents an async task that can be used to create a user
     * on the server.
     */
    class CreateUserAsyncTask extends AsyncTask<Void, Void, Integer> {
        private static final String NAME_KEY = "Name";
        private static final String PHONE_KEY = "Phone";
        private static final String EMAIL_KEY = "Email";

        private ProgressDialog loadingDialog = new ProgressDialog(LoginActivity.this);
        private String name;
        private String phone;
        private String email;

        @Override
        protected Integer doInBackground(Void... voids) {
            // TODO: validate input fields

            return postUser();
        }

        /***
         * Tries to create a user on the server by sending a POST request.
         * @return the id of the newly created user or -1 on failure.
         */
        private int postUser() {
            HttpClient client = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(API_CREATE_USER_URL);
            postRequest.addHeader("content-type", "application/json");
            HttpResponse response = null;

            User user = new User(name, phone, email, "1");
            String userJson = serializeUser(user);

            StringEntity params = null;

            try {
                params = new StringEntity(userJson);
            } catch (UnsupportedEncodingException e) {
                Log.d(TAG, "UnsupportedEncodingException");
            }

            postRequest.setEntity(params);

            try {
                Log.d(TAG, String.format("Creating new user: %s", userJson));

                response = client.execute(postRequest);
            } catch (ClientProtocolException e) {
                Log.d(TAG, "ClientProtocolException");
            } catch (IOException e) {
                Log.d(TAG, "IOException");
            }

            Log.d(TAG, "Server returned status code: " + response.getStatusLine().getStatusCode());

            String responseString = "";

            if(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // read the response (the user id)
                try {
                    InputStream is = response.getEntity().getContent();
                    responseString = IOUtils.toString(is);

                    Log.d(TAG, "Full response: " + responseString);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                user = deserializeUser(responseString);
                return user.getId();
            }

            Log.d(TAG, "Could not create user");
            return PREFS_DEFAULT_USER_ID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // show loading dialog
            loadingDialog.setTitle(R.string.create_user_loading_title);
            loadingDialog.setMessage(getApplicationContext().getString(R.string.create_user_loading_message));
            loadingDialog.show();

            // fetch variables from text fields
            name = txtName.getText().toString().trim();
            phone = txtPhone.getText().toString().trim();
            email = txtEmail.getText().toString().trim().toLowerCase();
        }

        @Override
        protected void onPostExecute(Integer userId) {
            super.onPostExecute(userId);

            // save user id to preferences
            saveUserId(userId);

            // dismiss the loading dialog
            loadingDialog.dismiss();

            if(userId != PREFS_DEFAULT_USER_ID) {
                // show success message
                Toast.makeText(LoginActivity.this, getApplication().getString(R.string.create_user_success_message), Toast.LENGTH_SHORT).show();

                // navigate to main activity
                navigateToMainActivity();
            } else {
                // create an alert dialog showing an error message
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle(R.string.create_user_error_title)
                        .setMessage(getApplicationContext().getString(R.string.create_user_error_message))
                        .setPositiveButton("OK", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        /***
         * Serializes a user into JSON.
         * @param user the user object to serialize.
         * @return a JSON string representing the user.
         */
        private String serializeUser(User user) {
            Gson gson = new Gson();
            return gson.toJson(user);
        }

        /***
         * Deserializes a JSON string into a user object.
         * @param json the JSON string to deserialize.
         * @return a user object representing the JSON string.
         */
        private User deserializeUser(String json) {
            Gson gson = new Gson();
            return gson.fromJson(json, User.class);
        }
    }
}
