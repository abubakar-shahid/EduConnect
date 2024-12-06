package com.example.educonnect;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProposalsAdapter extends RecyclerView.Adapter<ProposalsAdapter.ProposalViewHolder> {
    private List<Proposal> proposals = new ArrayList<>();

    @NonNull
    @Override
    public ProposalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_proposal, parent, false);
        return new ProposalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProposalViewHolder holder, int position) {
        holder.bind(proposals.get(position));
    }

    @Override
    public int getItemCount() {
        return proposals.size();
    }

    public void setProposals(List<Proposal> proposals) {
        this.proposals = proposals;
        notifyDataSetChanged();
    }

    class ProposalViewHolder extends RecyclerView.ViewHolder {
        private final TextView tutorNameText;
        private final TextView proposalText;
        private final TextView amountText;
        private final TextView timestampText;
        private final Button chatButton;

        public ProposalViewHolder(@NonNull View itemView) {
            super(itemView);
            tutorNameText = itemView.findViewById(R.id.tutor_name);
            proposalText = itemView.findViewById(R.id.proposal_text);
            amountText = itemView.findViewById(R.id.proposal_amount);
            timestampText = itemView.findViewById(R.id.proposal_timestamp);
            chatButton = itemView.findViewById(R.id.btn_chat);
        }

        public void bind(Proposal proposal) {
            tutorNameText.setText(proposal.getTutorName());
            proposalText.setText(proposal.getProposal());
            amountText.setText(String.format(Locale.getDefault(), "$%.2f", proposal.getAmount()));

            // Format timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
            String formattedTime = sdf.format(new Date(proposal.getTimestamp()));
            timestampText.setText(formattedTime);

            // Setup chat button
            chatButton.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), ChatActivity.class);
                intent.putExtra("otherUserId", proposal.getTutorId());
                intent.putExtra("otherUserName", proposal.getTutorName());
                itemView.getContext().startActivity(intent);
            });
        }
    }
} 