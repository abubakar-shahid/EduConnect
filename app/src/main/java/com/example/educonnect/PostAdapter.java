package com.example.educonnect;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;
    private boolean isStudent;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseDatabase realTimeDb;

    public PostAdapter(List<Post> posts, boolean isStudent) {
        this.posts = posts;
        this.isStudent = isStudent;
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.realTimeDb = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updatePosts(List<Post> newPosts) {
        posts.clear();
        posts.addAll(newPosts);
        notifyDataSetChanged();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, subjectTextView, descriptionTextView, dateTimeTextView, amountTextView, tokensTextView;
        Button actionButton, checkProposalsButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title);
            subjectTextView = itemView.findViewById(R.id.post_subject);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            dateTimeTextView = itemView.findViewById(R.id.post_date_time);
            amountTextView = itemView.findViewById(R.id.post_amount);
            tokensTextView = itemView.findViewById(R.id.post_tokens);
            actionButton = itemView.findViewById(R.id.post_action_button);
            checkProposalsButton = itemView.findViewById(R.id.check_proposals_button);
        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            subjectTextView.setText(post.getSubject());
            descriptionTextView.setText(post.getDescription());
            dateTimeTextView.setText(post.getDate() + " " + post.getTime());
            amountTextView.setText("$" + post.getAmount());
            tokensTextView.setText(post.getTokens() + " tokens required");

            String currentUserId = mAuth.getCurrentUser().getUid();

            if (isStudent) {
                actionButton.setText("Edit");
                actionButton.setOnClickListener(v -> {
                    Intent intent = new Intent(itemView.getContext(), EditPostActivity.class);
                    intent.putExtra("post_id", post.getPostId());
                    intent.putExtra("title", post.getTitle());
                    intent.putExtra("subject", post.getSubject());
                    intent.putExtra("description", post.getDescription());
                    intent.putExtra("amount", post.getAmount());
                    intent.putExtra("tokens", post.getTokens());
                    itemView.getContext().startActivity(intent);
                });
                
                if (currentUserId.equals(post.getStudentId())) {
                    checkProposalsButton.setVisibility(View.VISIBLE);
                    checkProposalsButton.setOnClickListener(v -> showProposalsDialog(post.getPostId()));
                } else {
                    checkProposalsButton.setVisibility(View.GONE);
                }
            } else {
                actionButton.setText("Submit Proposal");
                actionButton.setOnClickListener(v -> showProposalDialog(post.getPostId()));
                checkProposalsButton.setVisibility(View.GONE);
            }
        }

        private void showProposalDialog(String postId) {
            Dialog dialog = new Dialog(itemView.getContext());
            dialog.setContentView(R.layout.layout_proposal_dialog);

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);

            TextInputEditText proposalInput = dialog.findViewById(R.id.proposal_input);
            TextInputEditText amountInput = dialog.findViewById(R.id.amount_input);
            Button submitButton = dialog.findViewById(R.id.submit_proposal_dialog_button);

            submitButton.setOnClickListener(v -> {
                String proposalText = proposalInput.getText().toString().trim();
                String amountText = amountInput.getText().toString().trim();

                if (proposalText.isEmpty() || amountText.isEmpty()) {
                    Toast.makeText(itemView.getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get current user (tutor) details
                String tutorId = mAuth.getCurrentUser().getUid();
                
                // Create a new proposal object
                String proposalId = realTimeDb.getReference("proposals").push().getKey();
                
                Proposal proposal = new Proposal(
                    proposalId,
                    postId,
                    tutorId,
                    "", // tutorName will be set after fetching from Firestore
                    proposalText,
                    amountText,
                    System.currentTimeMillis()
                );

                // Get tutor's name from Firestore
                db.collection("tutors").document(tutorId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String tutorName = documentSnapshot.getString("name");
                            proposal.setTutorName(tutorName);

                            // Save proposal to Firebase Realtime Database
                            realTimeDb.getReference("proposals")
                                .child(proposalId)
                                .setValue(proposal)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(itemView.getContext(), 
                                        "Proposal submitted successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(itemView.getContext(), 
                                        "Failed to submit proposal", Toast.LENGTH_SHORT).show();
                                });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(itemView.getContext(), 
                            "Failed to get tutor information", Toast.LENGTH_SHORT).show();
                    });
            });

            dialog.show();
        }

        private void showProposalsDialog(String postId) {
            Dialog dialog = new Dialog(itemView.getContext());
            dialog.setContentView(R.layout.layout_proposals_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);

            RecyclerView recyclerView = dialog.findViewById(R.id.proposals_recycler_view);
            TextView noProposalsText = dialog.findViewById(R.id.no_proposals_text);
            
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            List<Proposal> proposalsList = new ArrayList<>();
            ProposalAdapter proposalAdapter = new ProposalAdapter(proposalsList);
            recyclerView.setAdapter(proposalAdapter);

            // Fetch proposals from Firebase Realtime Database
            realTimeDb.getReference("proposals")
                .orderByChild("postId")
                .equalTo(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        proposalsList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Proposal proposal = snapshot.getValue(Proposal.class);
                            if (proposal != null) {
                                proposalsList.add(proposal);
                            }
                        }
                        
                        if (proposalsList.isEmpty()) {
                            noProposalsText.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            noProposalsText.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        
                        proposalAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(itemView.getContext(), 
                            "Error loading proposals: " + databaseError.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                });

            dialog.show();
        }
    }
}
