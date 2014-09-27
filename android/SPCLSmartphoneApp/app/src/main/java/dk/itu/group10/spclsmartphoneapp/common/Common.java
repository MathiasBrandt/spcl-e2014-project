package dk.itu.group10.spclsmartphoneapp.common;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import dk.itu.group10.spclsmartphoneapp.models.User;

/**
 * Created by brandt on 27/09/14.
 */

public class Common {
    private static final String TAG = "Common";
    public static final String KEY_USER_ID = "USER_ID";
    public static final int DEFAULT_USER_ID = -1;
    public static final String API_CREATE_USER = "http://178.62.255.11/users";

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

    /***
     * Retrieves the user id stored in SharedPreferences.
     * @param a an activity used for context.
     * @return the stored user id or -1 if no user is exists in SharedPreferences.
     */
    public static int getUserIdFromPreferences(Activity a) {
        SharedPreferences preferences = a.getPreferences(a.MODE_PRIVATE);
        int userId = preferences.getInt(KEY_USER_ID, Common.DEFAULT_USER_ID);

        if(userId != Common.DEFAULT_USER_ID) {
            Log.d(TAG, String.format("User id found in preferences: %d", userId));
        } else {
            Log.d(TAG, String.format("No user id found in preferences (%d)", userId));
        }

        return userId;
    }

    /***
     * Saves a user id to SharedPreferences.
     * @param a an activity used for context.
     * @param userId the user id to save.
     */
    public static void saveUserIdToPreferences(Activity a, int userId) {
        SharedPreferences preferences = a.getPreferences(a.MODE_PRIVATE);

        Log.d(TAG, String.format("Saving new user id to preferences: %d", userId));

        preferences.edit().putInt(KEY_USER_ID, userId).apply();
    }

    /***
     * Send a post request to the specified url.
     * @param url the url to send the request to.
     * @param json the request parameters formatted as JSON.
     * @return the HttpResponse from the server.
     */
    public static HttpResponse sendHttpPost(String url, String json) {
        HttpClient client = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader("content-type", "application/json");
        StringEntity params = null;
        HttpResponse response = null;

        try {
            params = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "UnsupportedEncodingException: could not create StringEntity from json!" + e.getMessage());
        }

        if(params != null) {
            postRequest.setEntity(params);

            try {
                Log.d(TAG, String.format("Creating new user: %s", json));

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
}
