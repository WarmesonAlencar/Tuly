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

    // Carregar os dados do perfil do usuário
    public void loadProfileData() {
        int userId = session.getUserId();

        User user = userDAO.getUserById(userId);
        if (user == null) {
            view.showError("Erro ao carregar dados do usuário.");
            return;
        }

        int postCount = postDAO.getPostCountByUser(userId);

        // FOTO DO USUÁRIO (pode estar null)
        String fotoUri = user.getFotoUri();

        // Enviar dados completos
        view.showUserData(user.getNome(), user.getEmail(), postCount, fotoUri);
    }

    // Atualizar foto do perfil
    public void updateProfilePhoto(String fotoUri) {
        int userId = session.getUserId();

        boolean sucesso = userDAO.updateProfilePhoto(userId, fotoUri);

        if (sucesso) {
            view.onPhotoUpdated();
        } else {
            view.showError("Erro ao atualizar foto.");
        }
    }
}
