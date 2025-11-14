package com.example.tuly.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.example.tuly.models.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "tuly.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_SENHA = "senha";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_BIO = "bio";
    public static final String COLUMN_FOTO_PERFIL = "fotoPerfil";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOME + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                COLUMN_SENHA + " TEXT NOT NULL, " +
                COLUMN_USERNAME + " TEXT DEFAULT '', " +
                COLUMN_BIO + " TEXT DEFAULT '', " +
                COLUMN_FOTO_PERFIL + " TEXT DEFAULT ''" +
                ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // --- Inserção de usuário ---
    public boolean inserirUsuario(String nome, String email, String senha) {
        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
            return false;
        }
        if (usuarioExiste(email)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        // Campos principais
        ContentValues valores = new ContentValues();
        valores.put(COLUMN_NOME, nome);
        valores.put(COLUMN_EMAIL, email);
        valores.put(COLUMN_SENHA, senha); // Em produção, usar hash!

        // Adiciona campos opcionais
        valores.putAll(criarCamposOpcionais(nome));

        long result = db.insert(TABLE_USERS, null, valores);
        db.close();
        return result != -1;
    }

    // --- Campos opcionais separados ---
    private ContentValues criarCamposOpcionais(String nome) {
        ContentValues valores = new ContentValues();
        valores.put(COLUMN_USERNAME, nome);  // username inicial
        valores.put(COLUMN_BIO, "");         // bio padrão vazia
        valores.put(COLUMN_FOTO_PERFIL, ""); // foto padrão vazia
        return valores;
    }

    // --- Verifica se usuário existe ---
    public boolean usuarioExiste(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String[] columns = {COLUMN_ID};
            String selection = COLUMN_EMAIL + " = ?";
            String[] selectionArgs = { email };
            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            return (cursor != null && cursor.moveToFirst());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    // --- Retorna usuário por email ---
    public User getUsuarioPorEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            String[] columns = {
                    COLUMN_ID, COLUMN_NOME, COLUMN_EMAIL,
                    COLUMN_USERNAME, COLUMN_BIO, COLUMN_FOTO_PERFIL
            };
            String selection = COLUMN_EMAIL + " = ?";
            String[] selectionArgs = {email};

            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                String bio = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIO));
                String fotoPerfil = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOTO_PERFIL));

                user = new User(id, nome, username, bio, fotoPerfil);
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Erro ao buscar colunas: Verifique o onCreate().", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return user;
    }

    // --- Retorna usuário por ID ---
    public User getUsuarioPorId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            String[] columns = {
                    COLUMN_ID, COLUMN_NOME, COLUMN_EMAIL,
                    COLUMN_USERNAME, COLUMN_BIO, COLUMN_FOTO_PERFIL
            };
            String selection = COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(userId) };

            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                String bio = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIO));
                String fotoPerfil = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOTO_PERFIL));

                user = new User(id, nome, username, bio, fotoPerfil);
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Erro ao buscar colunas por ID: Verifique o onCreate().", e);
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return user;
    }

    // --- Atualizar foto de perfil ---
    public boolean atualizarFotoPerfil(int usuarioId, String caminhoFoto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FOTO_PERFIL, caminhoFoto);

        int linhasAfetadas = db.update(TABLE_USERS, cv, COLUMN_ID + "=?", new String[]{String.valueOf(usuarioId)});
        db.close();
        return linhasAfetadas > 0;
    }

    // --- Verifica login ---
    public boolean verificarLogin(String email, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] columns = {COLUMN_ID};
            String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_SENHA + " = ?";
            String[] selectionArgs = { email, senha };
            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            return (cursor != null && cursor.moveToFirst());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    // --- Retorna ID do usuário por email ---
    public int getUsuarioIdPorEmail(String email) {
        int id = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                    COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return id;
    }

    // --- Retorna caminho da foto de perfil ---
    public String getFotoPerfil(int usuarioId) {
        String caminho = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_USERS,
                    new String[]{COLUMN_FOTO_PERFIL},
                    COLUMN_ID + "=?",
                    new String[]{String.valueOf(usuarioId)},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                caminho = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOTO_PERFIL));
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return caminho;
    }

    // --- Usuário logado (futuro) ---
    public User getUsuarioLogado() {
        return null; // Retornar usuário logado quando implementar sessão
    }
}
