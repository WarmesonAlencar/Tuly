package com.example.tuly;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tuly.database.DatabaseHelper;
import com.example.tuly.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DatabaseHelper(this);

        binding.btnEntrar.setOnClickListener(v -> {
            String email = binding.edtUsuario.getText().toString().trim();
            String senha = binding.edtSenha.getText().toString();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha usuário e senha", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean ok = db.verificarLogin(email, senha);
            if (ok) {
                startActivity(new Intent(this, FeedActivity.class));
                finish();
            } else {
                Toast.makeText(this, "E-mail ou senha inválidos!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnCadastrar.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
