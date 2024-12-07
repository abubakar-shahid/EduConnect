package com.example.educonnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatInbox> chats;
    private OnChatClickListener listener;

    public ChatAdapter(List<ChatInbox> chats, OnChatClickListener listener) {
        this.chats = chats;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatInbox chat = chats.get(position);
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public interface OnChatClickListener {
        void onChatClick(ChatInbox chat);
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, lastMessageTextView, timeTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.chat_name);
            lastMessageTextView = itemView.findViewById(R.id.chat_last_message);
            timeTextView = itemView.findViewById(R.id.chat_time);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onChatClick(chats.get(position));
                }
            });
        }

        public void bind(ChatInbox chat) {
            nameTextView.setText(chat.getParticipantName());
            lastMessageTextView.setText(chat.getLastMessage());
            
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String time = sdf.format(new Date(chat.getLastMessageTime()));
            timeTextView.setText(time);
        }
    }
}
