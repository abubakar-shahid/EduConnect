package com.example.educonnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProposalAdapter extends RecyclerView.Adapter<ProposalAdapter.ProposalViewHolder> {
    private List<Proposal> proposals;
    private OnProposalClickListener listener;

    public interface OnProposalClickListener {
        void onProposalClick(Proposal proposal);
    }

    public ProposalAdapter(List<Proposal> proposals, OnProposalClickListener listener) {
        this.proposals = proposals;
        this.listener = listener;
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

    class ProposalViewHolder extends RecyclerView.ViewHolder {
        TextView tutorNameText, proposalText, amountText, dateText;

        public ProposalViewHolder(@NonNull View itemView) {
            super(itemView);
            tutorNameText = itemView.findViewById(R.id.tutor_name);
            proposalText = itemView.findViewById(R.id.proposal_text);
            amountText = itemView.findViewById(R.id.proposal_amount);
            dateText = itemView.findViewById(R.id.proposal_date);
        }

        public void bind(Proposal proposal) {
            tutorNameText.setText(proposal.getTutorName());
            proposalText.setText(proposal.getMessage());
            amountText.setText("$" + proposal.getPrice());
            
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
            String date = sdf.format(new Date(proposal.getTimestamp()));
            dateText.setText(date);
            
            itemView.setOnClickListener(v -> {
                if (ProposalAdapter.this.listener != null) {
                    ProposalAdapter.this.listener.onProposalClick(proposal);
                }
            });
        }
    }
} 