package com.example.tuly.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tuly.R;
import com.example.tuly.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final Context context;
    private final List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        // Nome do usuário
        holder.txtNomeUsuario.setText(post.getNomeUsuario());

        // Comentário
        holder.txtComentario.setText(post.getComentario());

        // Data formatada
        String data = android.text.format.DateFormat
                .format("dd/MM/yyyy HH:mm", post.getTimestamp())
                .toString();
        holder.txtTimestamp.setText(data);

        // Foto do perfil
        if (post.getFotoPerfilUri() != null && !post.getFotoPerfilUri().isEmpty()) {
            Glide.with(context)
                    .load(post.getFotoPerfilUri())
                    .circleCrop()
                    .placeholder(R.drawable.ic_person)
                    .into(holder.imgFotoPerfil);
        } else {
            holder.imgFotoPerfil.setImageResource(R.drawable.ic_person);
        }

        // Foto do post (se existir)
        if (post.getFotoUri() != null && !post.getFotoUri().isEmpty()) {
            holder.imgPost.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(post.getFotoUri())
                    .fitCenter()
                    .into(holder.imgPost);
        } else {
            holder.imgPost.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView txtNomeUsuario, txtComentario, txtTimestamp;
        ImageView imgFotoPerfil, imgPost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNomeUsuario = itemView.findViewById(R.id.txtNomeUsuario);
            txtComentario = itemView.findViewById(R.id.txtComentario);
            txtTimestamp = itemView.findViewById(R.id.txtTimestamp);

            imgFotoPerfil = itemView.findViewById(R.id.imgFotoPerfil);
            imgPost = itemView.findViewById(R.id.imgPost); // AGORA FUNCIONA
        }
    }
}
