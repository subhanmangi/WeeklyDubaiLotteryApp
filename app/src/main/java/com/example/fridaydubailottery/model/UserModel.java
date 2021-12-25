package com.example.fridaydubailottery.model;

public class UserModel {
    private String id;
    private String name;
    private String accessToken;
    private String email;

    public UserModel(String id, String name, String accessToken, String email) {
        this.id = id;
        this.name = name;
        this.accessToken = accessToken;
        this.email = email;
    }

    public UserModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
