package dk.itu.group10.spclsmartphoneapp.common;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

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
}
