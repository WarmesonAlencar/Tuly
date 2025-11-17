package com.example.tuly.view;

import com.example.tuly.model.Post;
import java.util.List;

public interface FeedView {
    void showPosts(List<Post> posts);
    void showMessage(String message);
    void onPostPublished();
}
