package com.example.educonnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PostsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private FloatingActionButton fabAddPost;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.posts_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fabAddPost = view.findViewById(R.id.fab_add_post);
        fabAddPost.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddPostActivity.class);
            startActivity(intent);
        });

        // Initialize adapter with empty list
        postAdapter = new PostAdapter(new ArrayList<>(), 
                getActivity() instanceof StudentDashboardActivity);
        recyclerView.setAdapter(postAdapter);

        // Load posts
        loadPosts();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload posts when returning to fragment
        loadPosts();
    }

    private void loadPosts() {
        db.collection("posts")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post> posts = new ArrayList<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", 
                            Locale.getDefault());

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String title = document.getString("title");
                        String subject = document.getString("subject");
                        String description = document.getString("description");
                        Double amount = document.getDouble("amount");
                        Long tokens = document.getLong("tokens");
                        String date = sdf.format(document.getDate("createdAt"));

                        // Split date and time
                        String[] dateTime = date.split(" ");
                        
                        Post post = new Post(
                                title,
                                subject,
                                description,
                                dateTime[0],
                                dateTime[1],
                                amount,
                                tokens != null ? tokens.intValue() : 0
                        );
                        posts.add(post);
                    }

                    postAdapter.updatePosts(posts);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), 
                            "Error loading posts: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
    }
}
