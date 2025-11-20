package com.example.tuly.model;

public class User {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private String fotoUri;

    public User(int id, String nome, String email, String senha, String fotoUri) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.fotoUri = fotoUri;
    }


    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }

    public String getFotoUri() { return fotoUri; }
    public void setFotoUri(String uri) { this.fotoUri = uri; }
}





