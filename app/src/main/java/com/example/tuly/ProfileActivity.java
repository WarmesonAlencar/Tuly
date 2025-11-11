package com.example.tuly;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Referências aos elementos da tela
        TextView txtNome = findViewById(R.id.txtNome);
        TextView txtEmail = findViewById(R.id.txtEmail);
        Button btnVoltar = findViewById(R.id.btnVoltar);

        // (Exemplo de dados fixos — depois você pode puxar de um banco ou login)
        txtNome.setText("Warmeson de Freitas Alencar");
        txtEmail.setText("warmeson@email.com");

        // Botão para voltar à tela Home
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FeedActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
