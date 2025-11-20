package com.example.tuly.presenter;

import android.content.Context;

import com.example.tuly.database.PostDAO;
import com.example.tuly.model.Post;
import com.example.tuly.utils.SessionManager;
import com.example.tuly.view.FeedView;

import java.util.List;

public class FeedPresenter {

    private final FeedView view;
    private final PostDAO postDAO;
    private final SessionManager session;

    public FeedPresenter(FeedView view, Context context) {
        this.view = view;
        this.postDAO = new PostDAO(context);
        this.session = new SessionManager(context);
    }

    // carregar posts
    public void loadPosts() {
        List<Post> posts = postDAO.getAllPosts();
        view.showPosts(posts);
    }

    // publicar post
    public void publishPost(String comentario, String fotoUri) {


        if ((comentario == null || comentario.trim().isEmpty()) && (fotoUri == null || fotoUri.trim().isEmpty())) {
            view.showMessage("Digite um comentário ou selecione uma foto!");
            return;
        }


        int userId = session.getUserId();

        boolean ok = postDAO.insertPost(userId, comentario, fotoUri);

        if (ok) {
            view.onPostPublished();
            loadPosts();
        } else {
            view.showMessage("Erro ao publicar post.");
        }
    }
}
