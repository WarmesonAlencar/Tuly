package com.example.tuly.model;

public class Post {

    private int id;
    private int userId;
    private String comentario;
    private String fotoUri;
    private long timestamp;

    // Novos campos
    private String nomeUsuario;
    private String fotoPerfilUri;

    public Post() {}

    public Post(int id, int userId, String comentario, String fotoUri, long timestamp) {
        this.id = id;
        this.userId = userId;
        this.comentario = comentario;
        this.fotoUri = fotoUri;
        this.timestamp = timestamp;
    }

    // GETTERS e SETTERS
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getComentario() { return comentario; }
    public String getFotoUri() { return fotoUri; }
    public long getTimestamp() { return timestamp; }
    public String getNomeUsuario() { return nomeUsuario; }
    public String getFotoPerfilUri() { return fotoPerfilUri; }

    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public void setFotoUri(String fotoUri) { this.fotoUri = fotoUri; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public void setFotoPerfilUri(String fotoPerfilUri) { this.fotoPerfilUri = fotoPerfilUri; }
}
