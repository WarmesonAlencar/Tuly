package com.example.tuly.model;

public class Post {

    private int id;
    private int userId;
    private String comentario;
    private String fotoUri;
    private long timestamp;

    // NOVOS CAMPOS (vindos do JOIN):
    private String nomeUsuario;
    private String fotoPerfilUri;

    public Post() {}

    public Post(int id, int userId, String comentario, String fotoUri, long timestamp,
                String nomeUsuario, String fotoPerfilUri) {
        this.id = id;
        this.userId = userId;
        this.comentario = comentario;
        this.fotoUri = fotoUri;
        this.timestamp = timestamp;
        this.nomeUsuario = nomeUsuario;
        this.fotoPerfilUri = fotoPerfilUri;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getComentario() { return comentario; }
    public String getFotoUri() { return fotoUri; }
    public long getTimestamp() { return timestamp; }
    public String getNomeUsuario() { return nomeUsuario; }
    public String getFotoPerfilUri() { return fotoPerfilUri; }

    // Setters
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public void setFotoPerfilUri(String fotoPerfilUri) { this.fotoPerfilUri = fotoPerfilUri; }
}
