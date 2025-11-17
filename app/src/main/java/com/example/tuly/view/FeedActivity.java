package com.example.tuly.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuly.presenter.FeedPresenter;
import com.example.tuly.model.Post;
import com.example.tuly.R;
import com.example.tuly.utils.ImageUtils;

import java.util.List;

public class FeedActivity extends AppCompatActivity implements FeedView {

    EditText edtComentario;
    Button btnPublicar;
    RecyclerView recyclerPosts;
    FeedPresenter presenter;
    PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        edtComentario = findViewById(R.id.edtComentario);
        btnPublicar = findViewById(R.id.btnPublicar);
        recyclerPosts = findViewById(R.id.recyclerPosts);
        ImageView btnPerfil = findViewById(R.id.btnPerfil);

        recyclerPosts.setLayoutManager(new LinearLayoutManager(this));

        presenter = new FeedPresenter(this, this);
        presenter.loadPosts();

        btnPublicar.setOnClickListener(v -> {
            String comentario = edtComentario.getText().toString();
            String fotoUri = ""; // implementar depois com câmera/galeria
            presenter.publishPost(comentario, fotoUri);
        });

        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }


    // Receber resultado
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri imageUri = ImageUtils.handleActivityResult(requestCode, resultCode, data);
        if (imageUri != null) {
            Toast.makeText(this, "Imagem selecionada: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
            // Aqui você pode salvar no banco ou mostrar na tela
        }
    }



    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPostPublished() {
        Toast.makeText(this, "Post publicado!", Toast.LENGTH_SHORT).show();
        edtComentario.setText("");
    }

    @Override
    public void showPosts(List<Post> posts) {
        adapter = new PostAdapter(this, posts);
        recyclerPosts.setAdapter(adapter);
    }
}
