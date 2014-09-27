package dk.itu.group10.spclsmartphoneapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

import dk.itu.group10.spclsmartphoneapp.common.Common;
import dk.itu.group10.spclsmartphoneapp.models.User;


public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";

    private EditText txtName;
    private EditText txtPhone;
    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO: DEBUGGING REMOVE THIS
        Common.saveUserIdToPreferences(this, Common.DEFAULT_USER_ID);

        // check preferences for user id
        int userId = Common.getUserIdFromPreferences(this);

        // if a user id was found, navigate to main activity
        if(userId != Common.DEFAULT_USER_ID) {
            navigateToMainActivity();
        }

        txtName = (EditText) findViewById(R.id.txtName);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
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
            HttpPost postRequest = new HttpPost(Common.API_CREATE_USER);
            postRequest.addHeader("content-type", "application/json");
            HttpResponse response = null;

            User user = new User(name, phone, email, "1");
            String userJson = Common.serializeUser(user);

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

                user = Common.deserializeUser(responseString);
                return user.getId();
            }

            Log.d(TAG, "Could not create user");
            return Common.DEFAULT_USER_ID;
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
            Common.saveUserIdToPreferences(LoginActivity.this, userId);

            // dismiss the loading dialog
            loadingDialog.dismiss();

            if(userId != Common.DEFAULT_USER_ID) {
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
    }
}
