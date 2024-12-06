package com.example.educonnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public void setProposals(List<Proposal> proposals) {
        this.proposals = proposals;
        notifyDataSetChanged();
    }

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

    static class ProposalViewHolder extends RecyclerView.ViewHolder {
        TextView tutorNameText, proposalText, amountText, timestampText;

        public ProposalViewHolder(@NonNull View itemView) {
            super(itemView);
            tutorNameText = itemView.findViewById(R.id.tutor_name);
            proposalText = itemView.findViewById(R.id.proposal_text);
            amountText = itemView.findViewById(R.id.proposal_amount);
            timestampText = itemView.findViewById(R.id.proposal_timestamp);
        }

        public void bind(Proposal proposal) {
            tutorNameText.setText(proposal.getTutorName());
            proposalText.setText(proposal.getProposal());
            amountText.setText(String.format("$%.2f", proposal.getAmount()));
            
            // Format timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String dateTime = sdf.format(new Date(proposal.getTimestamp()));
            timestampText.setText(dateTime);
        }
    }
} 