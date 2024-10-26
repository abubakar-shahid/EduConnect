package com.example.educonnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
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
        TextView titleTextView, subjectTextView, descriptionTextView, dateTimeTextView, amountTextView;
        Button proposalButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.post_title);
            subjectTextView = itemView.findViewById(R.id.post_subject);
            descriptionTextView = itemView.findViewById(R.id.post_description);
            dateTimeTextView = itemView.findViewById(R.id.post_date_time);
            amountTextView = itemView.findViewById(R.id.post_amount);
            proposalButton = itemView.findViewById(R.id.submit_proposal_button);
        }

        public void bind(Post post) {
            titleTextView.setText(post.getTitle());
            subjectTextView.setText(post.getSubject());
            descriptionTextView.setText(post.getDescription());
            dateTimeTextView.setText(post.getDate() + " " + post.getTime());
            amountTextView.setText("$" + post.getAmount());

            proposalButton.setOnClickListener(v -> {
                // TODO: Implement proposal submission logic
                // This should open a floating window for proposal submission
            });
        }
    }
}
