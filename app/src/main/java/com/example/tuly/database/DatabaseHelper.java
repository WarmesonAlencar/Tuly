package com.example.tuly.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log; // Adicionado para logs de erro

import com.example.tuly.models.User; // Sua classe de modelo completa

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "tuly.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_SENHA = "senha";
    // Colunas adicionais necessárias para o seu modelo User
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_BIO = "bio";
    public static final String COLUMN_FOTO_PERFIL = "fotoPerfil";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela atualizada para incluir todas as colunas do seu modelo User
        String CREATE_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOME + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                COLUMN_SENHA + " TEXT NOT NULL," +
                COLUMN_USERNAME + " TEXT DEFAULT ''," +
                COLUMN_BIO + " TEXT DEFAULT ''," +
                COLUMN_FOTO_PERFIL + " TEXT DEFAULT '')";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // --- Métodos de CRUD (Inserir, Existe, Login) ---

    public boolean inserirUsuario(String nome, String email, String senha) {
        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
            return false;
        }
        if (usuarioExiste(email)) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, nome);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_SENHA, senha); // Em produção, hashear!
        // Campos adicionais com valores padrão
        values.put(COLUMN_USERNAME, nome); // Exemplo: usando nome como username inicial
        values.put(COLUMN_BIO, "");
        values.put(COLUMN_FOTO_PERFIL, "");

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean usuarioExiste(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            String[] columns = {COLUMN_ID};
            String selection = COLUMN_EMAIL + " = ?";
            String[] selectionArgs = { email };
            // Consulta para verificar se existe uma linha com o email
            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            return (cursor != null && cursor.moveToFirst()); // Retorna true se houver resultado
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    // ... (usuarioExiste e verificarLogin permanecem os mesmos)

    // 🔹 Método corrigido: busca dados completos de um usuário pelo e-mail
    // **Retorna com.example.tuly.models.User**
    public User getUsuarioPorEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        User user = null; // Tipo alterado para User

        try {
            // Colunas completas
            String[] columns = {
                    COLUMN_ID, COLUMN_NOME, COLUMN_EMAIL, COLUMN_USERNAME, COLUMN_BIO, COLUMN_FOTO_PERFIL
            };
            String selection = COLUMN_EMAIL + " = ?";
            String[] selectionArgs = {email};

            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME));
                String emailUser = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));

                // Extraindo os novos campos
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME));
                String bio = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIO));
                String fotoPerfil = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOTO_PERFIL));

                // Cria o objeto User (seu modelo completo)
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

    // 🔹 NOVO MÉTODO: Adicionado para buscar por ID (útil para sessão)
    // 🔹 NOVO MÉTODO: Adicionado para buscar por ID (útil para sessão)
    public User getUsuarioPorId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        User user = null;

        try {
            String[] columns = {
                    COLUMN_ID, COLUMN_NOME, COLUMN_EMAIL, COLUMN_USERNAME, COLUMN_BIO, COLUMN_FOTO_PERFIL
            };
            String selection = COLUMN_ID + " = ?";
            String[] selectionArgs = { String.valueOf(userId) }; // Busca por ID

            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Monta o objeto User
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME));
                String emailUser = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL));
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


    // 🔹 Implementação do método que estava faltando o return (linha 120 aprox.)
    public User getUsuarioLogado() {


        return null; // Retorna null se não houver lógica de sessão ou usuário logado.
    }


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
}