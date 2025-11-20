package com.example.tuly.view;

public interface ProfileView {

    // Already existed — keep it
    void onPhotoUpdated();

    // Already existed — keep it
    void showUserData(String name, String email, int postCount, String photoUri);

    // Already existed — keep it
    void showError(String message);

    // NEW — show the new photo selected from gallery
    void showPhoto(String uri);

    // NEW — generic success message (non-error)
    void showMessage(String message);
}
