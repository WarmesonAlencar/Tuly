package com.example.tuly.utils;

public class ValidationUtils {
    public static boolean isValid(String nome, String email, String senha) {
        return !nome.isEmpty() && !email.isEmpty() && !senha.isEmpty();
    }
}
