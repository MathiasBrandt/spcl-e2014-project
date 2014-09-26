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


public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private static final String PREFS_USER_ID_KEY = "USER_ID";
    private static final int PREFS_DEFAULT_USER_ID = -1;

    private EditText txtName;
    private EditText txtPhone;
    private EditText txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        String name;
        String phone;
        String email;

        @Override
        protected Integer doInBackground(Void... voids) {
            int userId = -1;

            try {
                Thread.sleep(3000);

                // fetch user id from server
                userId = 1;
            } catch(InterruptedException e) {
                Log.d(TAG, "Interrupted");
            }

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
    }
}
