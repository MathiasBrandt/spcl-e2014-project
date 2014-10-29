package dk.itu.pervasive.common;

import com.google.gson.annotations.SerializedName;

/**
 * Created by brandt on 22/10/14.
 */

public class UserStatus {
    @SerializedName("user_id")
    private int userId;
    @SerializedName("status_id")
    private int statusId;

    public UserStatus(int userId, int statusId) {
        this.userId = userId;
        this.statusId = statusId;
    }

    public int getUserId() {
        return userId;
    }

    public int getStatusId() {
        return statusId;
    }
}
