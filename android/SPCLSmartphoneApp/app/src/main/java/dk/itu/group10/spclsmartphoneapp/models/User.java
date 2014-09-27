package dk.itu.group10.spclsmartphoneapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by brandt on 27/09/14.
 */

public class User {
    private int id;
    private String name;
    private String phone;
    private String email;
    @SerializedName("status_id")
    private int statusId;

    public User() {

    }

    public User(String name, String phone, String email, int statusId) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.statusId = statusId;
    }

    public int getId() {
        return id;
    }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public int getStatusId() { return statusId; }

    @Override
    public String toString() {
        return name;
    }
}
