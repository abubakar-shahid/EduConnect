package com.example.educonnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatInbox> chatList;
    private TextView noChatsText;
    private FirebaseDatabase database;
    private String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        database = FirebaseDatabase.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = view.findViewById(R.id.chats_recycler_view);
        noChatsText = view.findViewById(R.id.no_chats_text);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatList = new ArrayList<>();
        
        chatAdapter = new ChatAdapter(chatList, chat -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("other_user_id", chat.getParticipantId());
            intent.putExtra("other_user_name", chat.getParticipantName());
            startActivity(intent);
        });
        
        recyclerView.setAdapter(chatAdapter);
        loadChats();

        return view;
    }

    private void loadChats() {
        database.getReference("chat_inbox").child(currentUserId)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    chatList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatInbox chat = snapshot.getValue(ChatInbox.class);
                        if (chat != null) {
                            chatList.add(chat);
                        }
                    }
                    
                    if (chatList.isEmpty()) {
                        noChatsText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        noChatsText.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    
                    chatAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Handle error
                }
            });
    }
}
