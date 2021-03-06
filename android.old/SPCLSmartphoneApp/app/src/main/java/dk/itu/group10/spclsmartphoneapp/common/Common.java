package dk.itu.group10.spclsmartphoneapp.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import dk.itu.group10.spclsmartphoneapp.IOnCompleteListener;
import dk.itu.group10.spclsmartphoneapp.MainActivity;
import dk.itu.group10.spclsmartphoneapp.R;
import dk.itu.group10.spclsmartphoneapp.models.User;

/**
 * Created by brandt on 27/09/14.
 */

public class Common {
    private static final String TAG = "Common";
    public static final String PREFERENCES_NAME = "SpclAppPreferences";
    public static final String PREFERENCES_KEY_USER = "PREFS_KEY_USER";
    public static final String PARENT_KEY = "Parent";
    public static final String API_CREATE_USER = "http://178.62.255.11/users";
    public static final String API_LIST_USERS = "http://178.62.255.11/users";

    private Common() {}

    /***
     * Serializes a user into JSON.
     * @param user the user object to serialize.
     * @return a JSON string representing the user.
     */
    public static String serializeUser(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    /***
     * Deserializes a JSON string into a user object.
     * @param json the JSON string to deserialize.
     * @return a user object representing the JSON string.
     */
    public static User deserializeUser(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }

    public static List<User> deserializeUsers(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<User>>(){}.getType();
        List<User> users = gson.fromJson(json, listType);

        return users;
    }

    /***
     * Retrieves the user id stored in SharedPreferences.
     * @param a an activity used for context.
     * @return the stored user or null if no user is exists in SharedPreferences.
     */
    public static User getUserFromPreferences(Activity a) {
        SharedPreferences preferences = a.getSharedPreferences(PREFERENCES_NAME, a.MODE_PRIVATE);
        String userJson = preferences.getString(PREFERENCES_KEY_USER, null);
        User user = Common.deserializeUser(userJson);

        if(user != null) {
            Log.d(TAG, String.format("User found in preferences: %s", userJson));
        } else {
            Log.d(TAG, String.format("No user found in preferences"));
        }

        return user;
    }

    /***
     * Saves a user id to SharedPreferences.
     * @param a an activity used for context.
     * @param user the user to save.
     */
    public static void saveUserToPreferences(Activity a, User user) {
        SharedPreferences preferences = a.getSharedPreferences(PREFERENCES_NAME, a.MODE_PRIVATE);
        String userJson = Common.serializeUser(user);

        Log.d(TAG, String.format("Saving new user to preferences: %s", userJson));

        preferences.edit().putString(PREFERENCES_KEY_USER, userJson).apply();
    }

    public static void removeUserInPreferences(Activity a) {
        saveUserToPreferences(a, null);
    }

    /***
     * Send a post request to the specified url.
     * @param url the url to send the request to.
     * @param json the request parameters formatted as JSON.
     * @return the HttpResponse from the server, null on error.
     */
    public static HttpResponse sendHttpPost(String url, String json) {
        HttpClient client = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader("content-type", "application/json");
        StringEntity params = null;
        HttpResponse response = null;

        try {
            params = new StringEntity(json, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "UnsupportedEncodingException: could not create StringEntity from json!" + e.getMessage());
        }

        if(params != null) {
            postRequest.setEntity(params);

            try {
                Log.d(TAG, String.format("Sending POST data: %s to URL: %s", json, url));
                response = client.execute(postRequest);
                Log.d(TAG, "Server returned status code: " + response.getStatusLine().getStatusCode());
            } catch (ClientProtocolException e) {
                Log.d(TAG, "ClientProtocolException");
            } catch (IOException e) {
                Log.d(TAG, "IOException");
            }
        }

        return response;
    }

    /***
     * Send a get request to the specified url.
     * @param url the url the url to send the request to.
     * @return the HttpResponse from the server, null on error.
     */
    public static HttpResponse sendHttpGet(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(url);
        HttpResponse response = null;

        try {
            Log.d(TAG, String.format("Sending GET request to URL: %s", url));
            response = client.execute(getRequest);
            Log.d(TAG, "Server returned status code: " + response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            Log.d(TAG, "IOException");
        }

        return response;
    }

    /***
     * Parses an HttpResponse.
     * @param response the HttpResponse to parse.
     * @return a string representing the content of the response.
     */
    public static String parseHttpResponse(HttpResponse response) {
        String responseString = null;

        try {
            InputStream is = response.getEntity().getContent();
            responseString = IOUtils.toString(is);

            Log.d(TAG, "Full response: " + responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseString;
    }

    public static void createUser(Activity context, String name, String phone, String email) {
        new CreateUserAsyncTask(context, name, phone, email).execute();
    }

    public static void getUsers(Activity context, IOnCompleteListener onCompleteListener) {
        new GetUsersAsyncTask(context, onCompleteListener).execute();
    }

    /***
     * Navigates the user interface to MainActivity.
     */
    public static void navigateToActivity(Activity from, Class<?> to, Boolean keepInHistory) {
        navigateToActivity(from, to, null, keepInHistory);
    }

    public static void navigateToActivity(Activity from, Class<?> to, Class<?> parent, Boolean keepInHistory) {
        Intent i = new Intent(from, to);

        if(keepInHistory) {
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        }

        if(parent != null) {
            i.putExtra(PARENT_KEY, parent);
        }

        from.startActivity(i);
    }

    /***
     * This class represents an async task that can be used to create a user
     * on the server.
     */
    static class CreateUserAsyncTask extends AsyncTask<Void, Void, User> {
        private static final String NAME_KEY = "Name";
        private static final String PHONE_KEY = "Phone";
        private static final String EMAIL_KEY = "Email";

        private Activity context;
        private ProgressDialog loadingDialog;
        private String name;
        private String phone;
        private String email;

        public CreateUserAsyncTask(Activity context, String name, String phone, String email) {
            this.context = context;
            this.name = name;
            this.phone = phone;
            this.email = email;
            loadingDialog = new ProgressDialog(context);
        }

        @Override
        protected User doInBackground(Void... voids) {
            // TODO: validate input fields

            return postUser();
        }

        /***
         * Tries to create a user on the server by sending a POST request.
         * @return the id of the newly created user or -1 on failure.
         */
        private User postUser() {
            User user = new User(name, phone, email, 1);
            String userJson = Common.serializeUser(user);

            HttpResponse response = Common.sendHttpPost(Common.API_CREATE_USER, userJson);

            if(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseString = Common.parseHttpResponse(response);
                user = Common.deserializeUser(responseString);
                return user;
            }

            Log.d(TAG, "Could not create user");
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // show loading dialog
            loadingDialog.setTitle(R.string.create_user_loading_title);
            loadingDialog.setMessage(context.getString(R.string.please_wait_message));
            loadingDialog.show();
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);

            // save user to preferences
            Common.saveUserToPreferences(context, user);

            // dismiss the loading dialog
            loadingDialog.dismiss();

            if(user != null) {
                // show success message
                Toast.makeText(context, context.getString(R.string.create_user_success_message), Toast.LENGTH_SHORT).show();

                // navigate to main activity
                navigateToActivity(context, MainActivity.class, false);
            } else {
                // create an alert dialog showing an error message
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.create_user_error_title)
                        .setMessage(context.getString(R.string.create_user_error_message))
                        .setPositiveButton(R.string.btnOk, null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    static class GetUsersAsyncTask extends AsyncTask<Void, Void, String> {
        private Activity context;
        private IOnCompleteListener onCompleteListener;
        private ProgressDialog loadingDialog;

        public GetUsersAsyncTask(Activity context, IOnCompleteListener onCompleteListener) {
            this.context = context;
            this.onCompleteListener = onCompleteListener;
            loadingDialog = new ProgressDialog(context);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return getUsers();
        }

        private String getUsers() {
            HttpResponse response = Common.sendHttpGet(API_LIST_USERS);
            String responseString = null;

            if(response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                responseString = Common.parseHttpResponse(response);
            }

            return responseString;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // show loading dialog
            loadingDialog.setTitle(R.string.loading_existing_users_title);
            loadingDialog.setMessage(context.getString(R.string.please_wait_message));
            loadingDialog.show();
        }

        @Override
        protected void onPostExecute(String users) {
            super.onPostExecute(users);

            if(onCompleteListener != null) {
                onCompleteListener.onComplete(users);
            }

            loadingDialog.dismiss();
        }
    }
}
