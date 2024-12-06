package com.example.educonnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.QuerySnapshot;

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
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Please login again", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUserId = mAuth.getCurrentUser().getUid();
        
        // Check if user is student or tutor
        if (getActivity() instanceof StudentDashboardActivity) {
            loadStudentPosts(currentUserId);
        } else {
            loadTutorPosts(currentUserId);
        }
    }

    private void loadStudentPosts(String studentId) {
        try {
            db.collection("posts")
                    .whereEqualTo("studentId", studentId)
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        processPostsSnapshot(queryDocumentSnapshots);
                    })
                    .addOnFailureListener(e -> handleLoadFailure(e, studentId));
        } catch (Exception e) {
            handleException("Error in loadStudentPosts", e);
        }
    }

    private void loadTutorPosts(String tutorId) {
        // First get tutor's expertise
        db.collection("tutors").document(tutorId)
                .get()
                .addOnSuccessListener(tutorDoc -> {
                    if (tutorDoc.exists()) {
                        List<String> expertise = new ArrayList<>();
                        String expertise1 = tutorDoc.getString("expertise1");
                        String expertise2 = tutorDoc.getString("expertise2");
                        String expertise3 = tutorDoc.getString("expertise3");

                        if (expertise1 != null) expertise.add(expertise1.toLowerCase());
                        if (expertise2 != null) expertise.add(expertise2.toLowerCase());
                        if (expertise3 != null) expertise.add(expertise3.toLowerCase());

                        if (expertise.isEmpty()) {
                            Toast.makeText(getContext(), 
                                    "Please set your expertise in profile first", 
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        // Now get all posts and filter by expertise
                        db.collection("posts")
                                .orderBy("createdAt", Query.Direction.DESCENDING)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    List<Post> posts = new ArrayList<>();
                                    
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        String subject = document.getString("subject");
                                        if (subject != null && 
                                                expertise.contains(subject.toLowerCase())) {
                                            posts.add(createPostFromDocument(document));
                                        }
                                    }

                                    if (posts.isEmpty()) {
                                        Toast.makeText(getContext(), 
                                                "No posts found matching your expertise", 
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    postAdapter.updatePosts(posts);
                                })
                                .addOnFailureListener(e -> handleException("Error loading posts", e));
                    }
                })
                .addOnFailureListener(e -> handleException("Error loading tutor profile", e));
    }

    private Post createPostFromDocument(QueryDocumentSnapshot document) {
        String title = document.getString("title");
        String subject = document.getString("subject");
        String description = document.getString("description");
        Double amount = document.getDouble("amount");
        Long tokens = document.getLong("tokens");
        
        String date = "";
        String time = "";
        if (document.getDate("createdAt") != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String dateTime = sdf.format(document.getDate("createdAt"));
            String[] parts = dateTime.split(" ");
            if (parts.length == 2) {
                date = parts[0];
                time = parts[1];
            }
        }
        
        return new Post(
                document.getId(),
                title != null ? title : "",
                subject != null ? subject : "",
                description != null ? description : "",
                date,
                time,
                amount != null ? amount : 0.0,
                tokens != null ? tokens.intValue() : 0
        );
    }

    private void processPostsSnapshot(QuerySnapshot queryDocumentSnapshots) {
        try {
            List<Post> posts = new ArrayList<>();
            
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                posts.add(createPostFromDocument(document));
            }

            if (posts.isEmpty()) {
                Toast.makeText(getContext(), 
                        "No posts found. Create your first post!", 
                        Toast.LENGTH_SHORT).show();
            }

            postAdapter.updatePosts(posts);
        } catch (Exception e) {
            handleException("Error processing post data", e);
        }
    }

    private void handleLoadFailure(Exception e, String userId) {
        Log.e("PostsFragment", "Error loading posts: " + e.getMessage());
        
        if (e.getMessage() != null && e.getMessage().contains("FAILED_PRECONDITION")) {
            loadPostsWithoutOrdering(userId);
        } else {
            Toast.makeText(getContext(), 
                    "Error loading posts: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void handleException(String message, Exception e) {
        Log.e("PostsFragment", message + ": " + e.getMessage());
        Toast.makeText(getContext(), 
                "Error: " + e.getMessage(), 
                Toast.LENGTH_SHORT).show();
    }

    // Fallback method to load posts without ordering
    private void loadPostsWithoutOrdering(String currentUserId) {
        db.collection("posts")
                .whereEqualTo("studentId", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post> posts = new ArrayList<>();
                    
                    try {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String title = document.getString("title");
                            String subject = document.getString("subject");
                            String description = document.getString("description");
                            Double amount = document.getDouble("amount");
                            Long tokens = document.getLong("tokens");
                            
                            // Use current time if createdAt is missing
                            String date = "";
                            String time = "";
                            if (document.getDate("createdAt") != null) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", 
                                        Locale.getDefault());
                                String dateTime = sdf.format(document.getDate("createdAt"));
                                String[] parts = dateTime.split(" ");
                                if (parts.length == 2) {
                                    date = parts[0];
                                    time = parts[1];
                                }
                            }
                            
                            Post post = new Post(
                                    document.getId(),
                                    title != null ? title : "",
                                    subject != null ? subject : "",
                                    description != null ? description : "",
                                    date,
                                    time,
                                    amount != null ? amount : 0.0,
                                    tokens != null ? tokens.intValue() : 0
                            );
                            posts.add(post);
                        }

                        if (posts.isEmpty()) {
                            Toast.makeText(getContext(), 
                                    "No posts found. Create your first post!", 
                                    Toast.LENGTH_SHORT).show();
                        }

                        postAdapter.updatePosts(posts);
                    } catch (Exception e) {
                        Log.e("PostsFragment", "Error processing post data: " + e.getMessage());
                        Toast.makeText(getContext(), 
                                "Error processing posts", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("PostsFragment", "Error in fallback load: " + e.getMessage());
                    Toast.makeText(getContext(), 
                            "Error loading posts", Toast.LENGTH_SHORT).show();
                });
    }
}
