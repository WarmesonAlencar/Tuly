package com.example.tuly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tuly.database.DatabaseHelper;
import com.example.tuly.databinding.ActivityProfileBinding;
import com.example.tuly.models.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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

        SharedPreferences prefs = getSharedPreferences("APP_PREFS", MODE_PRIVATE);
        int usuarioId = prefs.getInt("usuario_id", -1);

        if (usuarioId == -1) {
            // Nenhum usuário logado, redireciona para login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        binding.circleImageView.setOnClickListener(v -> openImageChooser());

        // Chamada AGORA é feita em background para evitar a tela preta
        carregarDadosUsuarioEmBackground();

        carregarFotoPerfil(usuarioId);
    }

    private void carregarFotoPerfil(int usuarioId) {
        // Obter o caminho da foto do banco
        String caminhoFoto = db.getFotoPerfil(usuarioId); // Crie este método no DatabaseHelper

        if (caminhoFoto != null && !caminhoFoto.isEmpty()) {
            File file = new File(caminhoFoto);
            if (file.exists()) {
                binding.circleImageView.setImageURI(Uri.fromFile(file));
            }
        }
    }


    private static final int PICK_IMAGE_REQUEST = 1;

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            binding.circleImageView.setImageURI(imageUri);

            try {
                InputStream is = getContentResolver().openInputStream(imageUri);
                File file = getProfileImageFile();
                FileOutputStream fos = new FileOutputStream(file);


                int usuarioId = 1; // substitua pelo ID do usuário logado
                db.atualizarFotoPerfil(usuarioId, file.getAbsolutePath());

                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }

                fos.close();
                is.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private File getProfileImageFile() {
        File dir = new File(getFilesDir(), "profile_images"); // pasta interna do app
        if (!dir.exists()) {
            dir.mkdirs(); // cria a pasta se não existir
        }
        return new File(dir, "perfil_usuario.jpg"); // nome fixo da imagem
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