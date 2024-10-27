package com.example.educonnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private FloatingActionButton fabAddPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        recyclerView = view.findViewById(R.id.posts_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAddPost = view.findViewById(R.id.fab_add_post);
        fabAddPost.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddPostActivity.class);
            startActivity(intent);
        });

        // TODO: Replace with actual data fetching
        List<Post> posts = getDummyPosts();

        postAdapter = new PostAdapter(posts, getActivity() instanceof StudentDashboardActivity);
        recyclerView.setAdapter(postAdapter);

        return view;
    }

    private List<Post> getDummyPosts() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post("Math Tutor Needed", "Algebra", "Need help with quadratic equations", "2023-05-01", "14:00", 25.0, 5));
        posts.add(new Post("Physics Help", "Mechanics", "Struggling with Newton's laws", "2023-05-02", "16:30", 30.0, 7));
        // Add more dummy posts as needed
        return posts;
    }
}
