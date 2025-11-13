package com.example.tuly;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tuly.adapters.PostAdapter;
import com.example.tuly.databinding.ActivityFeedBinding;
import com.example.tuly.models.Post;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private ActivityFeedBinding binding;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postList = new ArrayList<>();
        // TODO: carregar posts do backend ou Firebase
        // Exemplo dummy:
        // postList.add(new Post(...));

        postAdapter = new PostAdapter(this, postList);
        binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewPosts.setAdapter(postAdapter);
        binding.recyclerViewPosts.setClipToPadding(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
