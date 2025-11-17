package com.example.tuly.view;

public interface ProfileView {

    void onPhotoUpdated();


    void showUserData(String nome, String email, int postCount, String fotoUri);

    void showError(String message);
}
