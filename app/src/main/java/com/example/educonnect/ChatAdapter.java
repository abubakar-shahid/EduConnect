package com.example.educonnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Chat> chats;
    private OnChatClickListener listener;

    public ChatAdapter(List<Chat> chats, OnChatClickListener listener) {
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
        Chat chat = chats.get(position);
        holder.bind(chat);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, lastMessageTextView, timeTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.chat_name);
            lastMessageTextView = itemView.findViewById(R.id.chat_last_message);
            timeTextView = itemView.findViewById(R.id.chat_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && ChatAdapter.this.listener != null) {
                        ChatAdapter.this.listener.onChatClick(ChatAdapter.this.chats.get(position));
                    }
                }
            });
        }

        public void bind(Chat chat) {
            nameTextView.setText(chat.getName());
            lastMessageTextView.setText(chat.getLastMessage());
            timeTextView.setText(chat.getTime());
        }
    }
}
