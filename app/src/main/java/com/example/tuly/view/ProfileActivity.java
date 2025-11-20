package com.example.tuly.view;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;

import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import android.widget.TextView;

import android.widget.Button;

import android.app.AlertDialog;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tuly.presenter.ProfilePresenter;

import com.example.tuly.utils.ImageUtils;

import com.example.tuly.R;


public class ProfileActivity extends AppCompatActivity implements ProfileView {



    private TextView txtNome, txtEmail, txtContador;

    private ImageView imgPerfil;

    private Button btnVoltar;

    private ProfilePresenter presenter;

    private static final int REQUEST_CAMERA_PERMISSION = 5001;


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

                        // Verifica se a permissão da câmera já foi concedida
                        if (checkSelfPermission(android.Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                            // Solicita permissão
                            requestPermissions(
                                    new String[]{android.Manifest.permission.CAMERA},
                                    REQUEST_CAMERA_PERMISSION
                            );

                        } else {
                            // Já tem permissão → Abre a câmera
                            ImageUtils.openCamera(this);
                        }

                    } else {

                        // Gallery flow

                        ImageUtils.openGallery(this);

                    }

                })

                .show();

    }
    // ======================================

    //            VIEW IMPLEMENTATION

    // ======================================

    @Override

    public void showUserData(String nome, String email, int postCount, String fotoUri) {

        txtNome.setText(nome);

        txtEmail.setText(email);

        txtContador.setText("Posts: " + postCount);



        if (fotoUri != null && !fotoUri.isEmpty()) {
            Glide.with(this)
                    .load(fotoUri)

                    .into(imgPerfil);
        }

    }



    @Override
    public void showPhoto(String uri) {
        Glide.with(this)
                .load(uri)
                .into(imgPerfil);
    }




    @Override

    public void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }



    @Override

    public void showError(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }



    @Override

    public void onPhotoUpdated() {

        presenter.loadProfileData(); // reloads everything

    }



    // ======================================

    //           ACTIVITY RESULT

    // ======================================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // ======= GALLERY RESULT =======
        Uri galleryUri = ImageUtils.handleGalleryResult(this, requestCode, resultCode, data);

        if (galleryUri != null) {
            presenter.onGalleryPhotoSelected(galleryUri.toString());
            return;
        }

        // ======= CAMERA RESULT =======
        Uri cameraUri = ImageUtils.handleCameraResult(requestCode, resultCode);

        if (cameraUri != null) {
            presenter.onCameraPhotoTaken(cameraUri.toString());
            return;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {

            // Permissão concedida
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                ImageUtils.openCamera(this);

            } else {
                // Permissão negada
                Toast.makeText(this, "Permissão da câmera negada.", Toast.LENGTH_SHORT).show();
            }
        }
    }


}

