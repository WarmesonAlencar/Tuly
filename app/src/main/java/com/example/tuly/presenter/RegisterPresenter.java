package com.example.tuly.presenter;

import android.content.Context;

import com.example.tuly.database.UserDAO;
import com.example.tuly.utils.ValidationUtils;
import com.example.tuly.view.RegisterView;

public class RegisterPresenter {

    private final RegisterView view;
    private final UserDAO userDAO;

    public RegisterPresenter(RegisterView view, Context context) {
        this.view = view;
        this.userDAO = new UserDAO(context);
    }

    public void registerUser(String nome, String email, String senha) {

        if (!ValidationUtils.isValid(nome, email, senha)) {
            view.showError("Preencha todos os campos.");
            return;
        }

        boolean inserted = userDAO.insertUser(nome, email, senha);

        if (inserted) {
            view.onRegisterSuccess();
        } else {
            view.showError("Erro ao cadastrar usuário. Tente outro e-mail.");
        }
    }
}
