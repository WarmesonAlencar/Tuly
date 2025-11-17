package com.example.tuly.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        holder.txtNomeUsuario.setText(post.getNomeUsuario());
        holder.txtComentario.setText(post.getComentario());

        // Formatando data
        String data = android.text.format.DateFormat
                .format("dd/MM/yyyy HH:mm", post.getTimestamp())
                .toString();
        holder.txtTimestamp.setText(data);

        // Foto do usuário (caso tenha)
        if (post.getFotoPerfilUri() != null) {
            Glide.with(context).load(post.getFotoPerfilUri()).into(holder.imgFotoPerfil);
        } else {
            holder.imgFotoPerfil.setImageResource(R.drawable.ic_person);
        }
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView txtComentario, txtTimestamp;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtComentario = itemView.findViewById(R.id.txtComentario);
            txtTimestamp = itemView.findViewById(R.id.txtTimestamp);
        }
    }
}

