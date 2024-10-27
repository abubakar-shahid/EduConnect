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

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;
    private boolean isStudent;

    public PostAdapter(List<Post> posts, boolean isStudent) {
        this.posts = posts;
        this.isStudent = isStudent;
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

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, subjectTextView, descriptionTextView, dateTimeTextView, amountTextView, tokensTextView;
        Button actionButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title);
            subjectTextView = itemView.findViewById(R.id.post_subject);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            dateTimeTextView = itemView.findViewById(R.id.post_date_time);
            amountTextView = itemView.findViewById(R.id.post_amount);
            tokensTextView = itemView.findViewById(R.id.post_tokens);
            actionButton = itemView.findViewById(R.id.post_action_button);
        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            subjectTextView.setText(post.getSubject());
            descriptionTextView.setText(post.getDescription());
            dateTimeTextView.setText(post.getDate() + " " + post.getTime());
            amountTextView.setText("$" + post.getAmount());
            tokensTextView.setText(post.getTokens() + " tokens required");

            if (isStudent) {
                actionButton.setText("More...");
                actionButton.setOnClickListener(v -> {
                    Intent intent = new Intent(itemView.getContext(), PostDetailsActivity.class);
                    intent.putExtra("title", post.getTitle());
                    intent.putExtra("subject", post.getSubject());
                    intent.putExtra("description", post.getDescription());
                    intent.putExtra("dateTime", post.getDate() + " " + post.getTime());
                    intent.putExtra("amount", post.getAmount());
                    intent.putExtra("tokens", post.getTokens());
                    itemView.getContext().startActivity(intent);
                });
            } else {
                actionButton.setText("Submit Proposal");
                actionButton.setOnClickListener(v -> showProposalDialog());
            }
            
        }

        private void showProposalDialog() {
            Dialog dialog = new Dialog(itemView.getContext());
            dialog.setContentView(R.layout.layout_proposal_dialog);

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.CENTER);

            TextInputEditText proposalInput = dialog.findViewById(R.id.proposal_input);
            TextInputEditText amountInput = dialog.findViewById(R.id.amount_input);
            Button submitButton = dialog.findViewById(R.id.submit_proposal_dialog_button);

            submitButton.setOnClickListener(v -> {
                String proposal = proposalInput.getText().toString();
                String amount = amountInput.getText().toString();

                if (proposal.isEmpty() || amount.isEmpty()) {
                    Toast.makeText(itemView.getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: Implement proposal submission logic
                    Toast.makeText(itemView.getContext(), "Proposal submitted", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }
}
