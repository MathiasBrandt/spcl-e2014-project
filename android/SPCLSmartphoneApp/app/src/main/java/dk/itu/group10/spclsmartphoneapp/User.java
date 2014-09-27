package dk.itu.group10.spclsmartphoneapp;

/**
 * Created by brandt on 27/09/14.
 */

public class User {
    String name;
    String phone;
    String email;
    String statusId;

    public User(String name, String phone, String email, String statusId) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.statusId = statusId;
    }

    public String getJson() {
        String s = "";
        s += "{";
        s += "\"name\":\"" + name + "\",";
        s += "\"phone\":\"" + phone + "\",";
        s += "\"email\":\"" + email + "\",";
        s += "\"status_id\":\"" + statusId + "\"";
        s += "}";

        return s;
    }
}
