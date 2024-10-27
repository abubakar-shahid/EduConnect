package com.example.educonnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.chats_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // TODO: Replace with actual data fetching
        List<Chat> chats = getDummyChats();

        setupRecyclerView(chats);

        return view;
    }

    private List<Chat> getDummyChats() {
        List<Chat> chats = new ArrayList<>();
        chats.add(new Chat("John Doe", "Hey, can you help me with math?", "10:30 AM"));
        chats.add(new Chat("Jane Smith", "Thanks for the physics lesson!", "Yesterday"));
        // Add more dummy chats as needed
        return chats;
    }

    private void setupRecyclerView(List<Chat> chats) {
        chatAdapter = new ChatAdapter(chats, new ChatAdapter.OnChatClickListener() {
            @Override
            public void onChatClick(Chat chat) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("chat_partner_name", chat.getName());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(chatAdapter);
    }
}
