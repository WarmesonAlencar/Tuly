package com.example.tuly.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tuly.model.User;

public class UserDAO {
    private final SQLiteDatabase db;

    public UserDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public boolean insertUser(String nome, String email, String senha) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NOME, nome);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_SENHA, senha);
        values.put("fotoUri", "");

        long result = db.insert("usuarios", null, values);
        return result != -1;
    }


    public User getUserById(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE id=?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String senha = cursor.getString(cursor.getColumnIndexOrThrow("senha"));
            String fotoUri = cursor.getString(cursor.getColumnIndexOrThrow("fotoUri"));
            cursor.close();
            return new User(id, nome, email, senha, fotoUri);
        }
        cursor.close();
        return null;
    }


    public boolean validateUser(String email, String senha) {
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE email=? AND senha=?", new String[]{email, senha});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public int getUserIdByEmail(String email) {
        Cursor cursor = db.rawQuery(
                "SELECT id FROM usuarios WHERE email=?",
                new String[]{email}
        );

        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();

        return userId;
    }

    public boolean updateProfilePhoto(int userId, String fotoUri) {
        ContentValues values = new ContentValues();
        values.put("fotoUri", fotoUri);

        int rows = db.update(
                "usuarios",
                values,
                "id=?",
                new String[]{String.valueOf(userId)}
        );

        return rows > 0;
    }



}
