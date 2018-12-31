package com.example.retrofitrough.api.model;

public class User {

    private String id;
    private String name;
    private String job;
    private String createdAt;

    public User(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
