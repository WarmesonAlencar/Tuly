package com.example.tuly.presenter;

import android.content.Context;

import com.example.tuly.database.PostDAO;
import com.example.tuly.database.UserDAO;
import com.example.tuly.model.User;
import com.example.tuly.utils.SessionManager;
import com.example.tuly.view.ProfileView;

public class ProfilePresenter {

    private final ProfileView view;
    private final UserDAO userDAO;
    private final PostDAO postDAO;
    private final SessionManager session;

    public ProfilePresenter(ProfileView view, Context context) {
        this.view = view;
        this.userDAO = new UserDAO(context);
        this.postDAO = new PostDAO(context);
        this.session = new SessionManager(context);
    }

    // Load initial user data
    public void loadProfileData() {
        int userId = session.getUserId();

        User user = userDAO.getUserById(userId);
        if (user == null) {
            view.showError("Error loading user data.");
            return;
        }

        int postCount = postDAO.getPostCountByUser(userId);
        String photoUri = user.getFotoUri();

        view.showUserData(user.getNome(), user.getEmail(), postCount, photoUri);
    }

    // ===========================
    // GALLERY FLOW (NEW)
    // ===========================
    public void onGalleryPhotoSelected(String uri) {
        int userId = session.getUserId();

        boolean success = userDAO.updateProfilePhoto(userId, uri);

        if (success) {
            // Show updated photo immediately
            view.showPhoto(uri);

            // Feedback
            view.showMessage("Foto Atualizada!");

            // Keep your original method call
            view.onPhotoUpdated();

        } else {
            view.showError("Erro ao atualizar a Foto");
        }
    }

    // ===========================
    // CAMERA FLOW (FUTURE)
    // ===========================
    public void onCameraPhotoTaken(String uri) {
        int userId = session.getUserId();

        boolean success = userDAO.updateProfilePhoto(userId, uri);

        if (success) {
            view.showPhoto(uri);
            view.showMessage("Foto tirada e salva!");
            view.onPhotoUpdated();
        } else {
            view.showError("Erro ao salvar foto da câmera");
        }
    }

}
