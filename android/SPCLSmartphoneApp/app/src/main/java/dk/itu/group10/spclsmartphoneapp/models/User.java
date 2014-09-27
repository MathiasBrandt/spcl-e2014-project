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
    private String statusId;

    public User() {

    }

    public User(String name, String phone, String email, String statusId) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.statusId = statusId;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
