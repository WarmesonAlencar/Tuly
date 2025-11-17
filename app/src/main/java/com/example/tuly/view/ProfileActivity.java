package com.example.tuly.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tuly.presenter.ProfilePresenter;
import com.example.tuly.utils.ImageUtils;
import com.example.tuly.R;

public class ProfileActivity extends AppCompatActivity implements ProfileView {

    TextView txtNome, txtEmail, txtContador;
    ImageView imgPerfil;
    Button btnVoltar;
    ProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        txtContador = findViewById(R.id.txtContador);
        imgPerfil = findViewById(R.id.imgPerfil);
        btnVoltar = findViewById(R.id.btnVoltar);

        presenter = new ProfilePresenter(this, this);
        presenter.loadProfileData();

        imgPerfil.setOnClickListener(v -> showImageOptions());

        btnVoltar.setOnClickListener(v -> finish());
    }

    private void showImageOptions() {
        String[] options = {"Câmera", "Galeria"};
        new AlertDialog.Builder(this)
                .setTitle("Selecionar imagem")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        ImageUtils.openCamera(this);
                    } else {
                        ImageUtils.openGallery(this);
                    }
                })
                .show();
    }

    //  M&Eacute;TODO CORRETO EXIGIDO PELA INTERFACE
    @Override
    public void showUserData(String nome, String email, int postCount, String fotoUri) {
        txtNome.setText(nome);
        txtEmail.setText(email);
        txtContador.setText("Posts: " + postCount);

        if (fotoUri != null && !fotoUri.isEmpty()) {
            imgPerfil.setImageURI(Uri.parse(fotoUri));
        }
    }

    //  MÉTODO EXIGIDO PELA INTERFACE
    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //  MÉTODO EXIGIDO PELA INTERFACE
    @Override
    public void onPhotoUpdated() {
        Toast.makeText(this, "Foto atualizada!", Toast.LENGTH_SHORT).show();
        presenter.loadProfileData(); // recarrega dados
    }

    // Manipula retorno da câmera/galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri imageUri = ImageUtils.handleActivityResult(requestCode, resultCode, data);
        if (imageUri != null) {
            imgPerfil.setImageURI(imageUri);
            presenter.updateProfilePhoto(imageUri.toString());
        }
    }
}
