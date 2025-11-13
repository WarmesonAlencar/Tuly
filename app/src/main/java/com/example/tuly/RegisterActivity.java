package com.example.tuly;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tuly.database.DatabaseHelper;
import com.example.tuly.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DatabaseHelper(this);

        binding.btnCadastrar.setOnClickListener(v -> {
            String nome = binding.edtNome.getText().toString().trim();
            String email = binding.edtEmail.getText().toString().trim();
            String senha = binding.edtSenha.getText().toString();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.usuarioExiste(email)) {
                Toast.makeText(this, "E-mail já cadastrado.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserido = db.inserirUsuario(nome, email, senha);
            if (inserido) {
                Toast.makeText(this, "Usuário cadastrado!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao cadastrar!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
