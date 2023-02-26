package com.example.socialmediamobileqev;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Users {

    public String name;
    public String email;
    String Url_profile;
    public String userID;

    public Users() {
    }

    public Users(String name, String email,String img_profile, String userID) {
        this.name = name;
        this.email = email;
        this.Url_profile = img_profile;
        this.userID = userID;
;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl_profile() {
        return Url_profile;
    }

    public void setUrl_profile(String url_profile) {
        Url_profile = url_profile;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("Url_profile", Url_profile);
        result.put("userID", userID);
        // result.put("lastName", lastName);
        return result;



    }
}
