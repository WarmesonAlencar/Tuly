package com.example.tuly;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuly.database.DatabaseHelper;
import com.example.tuly.databinding.ActivityProfileBinding;
import com.example.tuly.models.User;

import java.util.concurrent.Executors; // NOVO IMPORT

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Garante que o layout seja exibido

        db = new DatabaseHelper(this);

        // Chamada AGORA é feita em background para evitar a tela preta
        carregarDadosUsuarioEmBackground();
    }

    private void carregarDadosUsuarioEmBackground() {
        // Usa um Executor para mover a operação de DB para o background
        Executors.newSingleThreadExecutor().execute(() -> {

            // 1. CONSULTA DE DB (Executada na Thread de Background)
            User usuario = db.getUsuarioLogado();

            // 2. ATUALIZAÇÃO DA UI (Voltando para a Thread Principal)
            runOnUiThread(() -> {
                if (usuario != null) {
                    atualizarInterface(usuario);
                } else {
                    // Caso não haja usuário logado (mostra dados de convidado)
                    mostrarDadosConvidado();
                }
            });
        });
    }

    // Novo método para aplicar os dados do usuário à UI (Correção de sobrescrita inclusa)
    private void atualizarInterface(User usuario) {

        // CORREÇÃO: Define o @username (assumindo que tvUsername é para o nome de usuário/apelido)
        String username = usuario.getUsername() != null ? usuario.getUsername() : "usuário";
        binding.tvUsername.setText("@" + username); // Define o apelido (ex: @usuario)

        // Define bio ou mensagem padrão
        String bio = usuario.getBio() != null && !usuario.getBio().isEmpty()
                ? usuario.getBio()
                : "Ainda não adicionou uma bio.";
        binding.tvBio.setText(bio);

        // Define foto de perfil (usa uma imagem padrão se não existir)
        if (usuario.getFotoPerfil() != null && !usuario.getFotoPerfil().isEmpty()) {
            int resId = getResources().getIdentifier(
                    usuario.getFotoPerfil(),
                    "drawable",
                    getPackageName()
            );
            if (resId != 0) {
                binding.circleImageView.setImageResource(resId);
            } else {
                binding.circleImageView.setImageResource(R.drawable.ic_profile_placeholder);
            }
        } else {
            binding.circleImageView.setImageResource(R.drawable.ic_profile_placeholder);
        }
    }

    private void mostrarDadosConvidado() {
        Toast.makeText(this, "Nenhum usuário encontrado.", Toast.LENGTH_SHORT).show();
        binding.tvUsername.setText("@guest");
        binding.tvBio.setText("Faça login para ver seu perfil completo.");
        binding.circleImageView.setImageResource(R.drawable.ic_profile_placeholder);
    }
}