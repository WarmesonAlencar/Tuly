package com.example.tuly.models;

public class Post {
    private final String userName;
    private final String description;
    private final int imageResId;

    public Post(String userName, String description, int imageResId) {
        this.userName = userName;
        this.description = description;
        this.imageResId = imageResId;
    }

    public String getUserName() { return userName; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
}
