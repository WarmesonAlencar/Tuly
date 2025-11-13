package com.example.tuly.models;

public class User {
    private int id;
    private String nome;
    private String username;
    private String bio;
    private String fotoPerfil;

    public User(int id, String nome, String username, String bio, String fotoPerfil) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.bio = bio;
        this.fotoPerfil = fotoPerfil;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getUsername() { return username; }
    public String getBio() { return bio; }
    public String getFotoPerfil() { return fotoPerfil; }
}
