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

    private EditText edtComentario;
    private Button btnPublicar;
    private RecyclerView recyclerPosts;
    private FeedPresenter presenter;
    private PostAdapter adapter;

    private String selectedImageUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        edtComentario = findViewById(R.id.edtComentario);
        btnPublicar = findViewById(R.id.btnPublicar);
        recyclerPosts = findViewById(R.id.recyclerPosts);
        ImageView btnPerfil = findViewById(R.id.btnPerfil);
        ImageView btnAdicionarFoto = findViewById(R.id.btnAdicionarFoto);

        recyclerPosts.setLayoutManager(new LinearLayoutManager(this));

        presenter = new FeedPresenter(this, this);
        presenter.loadPosts();

        if (btnAdicionarFoto != null) {
            btnAdicionarFoto.setOnClickListener(v -> showImageOptions());
        }

        btnPublicar.setOnClickListener(v -> {
            String comentario = edtComentario.getText().toString();

            presenter.publishPost(comentario, selectedImageUri);
            selectedImageUri = "";
        });

        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }


    private void showImageOptions() {
        String[] options = {"Camera", "Gallery"};

        new android.app.AlertDialog.Builder(this)
                .setTitle("Choose image source")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        ImageUtils.openCamera(this);
                    } else {
                        ImageUtils.openGallery(this);
                    }
                })
                .show();
    }




    // Receber resultado
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // -------- GALLERY RESULT --------
        Uri galleryUri = ImageUtils.handleGalleryResult(this, requestCode, resultCode, data);
        if (galleryUri != null) {
            selectedImageUri = galleryUri.toString();
            Toast.makeText(this, "Image selected.", Toast.LENGTH_SHORT).show();
            return;
        }

        // -------- CAMERA RESULT (FUTURE) --------
        Uri cameraUri = ImageUtils.handleCameraResult(requestCode, resultCode);

        if (cameraUri != null) {
            selectedImageUri = cameraUri.toString();
            Toast.makeText(this, "Image captured.", Toast.LENGTH_SHORT).show();
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
