package com.example.tuly.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tuly.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    private final SQLiteDatabase db;

    public PostDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        this.db = helper.getWritableDatabase();
    }

    public boolean insertPost(int userId, String comentario, String fotoUri) {
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("comentario", comentario);
        values.put("fotoUri", fotoUri);
        values.put("timestamp", System.currentTimeMillis());

        long result = db.insert("posts", null, values);
        return result != -1;
    }

    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT p.id, p.userId, p.comentario, p.fotoUri, p.timestamp, " +
                        "u.nome AS nomeUsuario, u.fotoPerfil AS fotoPerfilUri " +
                        "FROM posts p " +
                        "JOIN usuarios u ON u.id = p.userId " +
                        "ORDER BY p.id DESC",
                null
        );


        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                String comentario = cursor.getString(cursor.getColumnIndexOrThrow("comentario"));
                String fotoUri = cursor.getString(cursor.getColumnIndexOrThrow("fotoUri"));
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow("timestamp"));

                String nomeUsuario = cursor.getString(cursor.getColumnIndexOrThrow("nomeUsuario"));
                String fotoPerfilUri = cursor.getString(cursor.getColumnIndexOrThrow("fotoPerfilUri"));

                Post post = new Post(id, userId, comentario, fotoUri, timestamp);
                post.setNomeUsuario(nomeUsuario);
                post.setFotoPerfilUri(fotoPerfilUri);

                posts.add(post);

            } while (cursor.moveToNext());
        }


        cursor.close();
        return posts;
    }

    public int getPostCountByUser(int userId) {
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM posts WHERE userId=?",
                new String[]{String.valueOf(userId)}
        );

        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        return count;
    }
}
