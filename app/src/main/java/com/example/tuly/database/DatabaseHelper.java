package com.example.tuly.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tuly.db";
    private static final int DATABASE_VERSION = 6;

    // Nome da tabela e colunas
    public static final String TABLE_USERS = "usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_SENHA = "senha";

    public static final String COLUMN_FOTO = "fotoUri";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // TABELA DE USUÁRIOS
        String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOME + " TEXT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_SENHA + " TEXT, "
                + COLUMN_FOTO + " TEXT)";
        db.execSQL(CREATE_TABLE_USERS);

        // TABELA DE POSTS
        String CREATE_POSTS = "CREATE TABLE posts ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "userId INTEGER, "
                + "comentario TEXT, "
                + "fotoUri TEXT, "
                + "timestamp INTEGER, "
                + "FOREIGN KEY(userId) REFERENCES usuarios(id))";
        db.execSQL(CREATE_POSTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS posts");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Inserir novo usuário
    public boolean inserirUsuario(String nome, String email, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, nome);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_SENHA, senha);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // Verificar login
    public boolean verificarLogin(String email, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_SENHA + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email, senha});
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return existe;
    }




    public String getFotoPerfil(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_FOTO + " FROM " + TABLE_USERS + " WHERE id=?",
                new String[]{String.valueOf(userId)});

        String foto = null;
        if (cursor.moveToFirst()) {
            foto = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOTO));
        }

        cursor.close();
        return foto;
    }


}
