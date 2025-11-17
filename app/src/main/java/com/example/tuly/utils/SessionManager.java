package com.example.tuly.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "TulySession";
    private static final String KEY_USER_ID = "userId";

    private final SharedPreferences sharedPreferences;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Salvar ID do usuário logado
    public void saveUserId(int id) {
        sharedPreferences.edit().putInt(KEY_USER_ID, id).apply();
    }

    // Pegar ID do usuário logado
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    // Limpar sessão (logout)
    public void clearSession() {
        sharedPreferences.edit().clear().apply();
    }
}

