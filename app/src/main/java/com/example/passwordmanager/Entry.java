package com.example.passwordmanager;

public class Entry {
    private String id;
    private String website;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String password;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Entry() {
        // Default constructor required for Firestore
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Entry(String website, String password, String userId) {
        this.website = website;
        this.password = password;
        this.userId = userId;
    }


}
