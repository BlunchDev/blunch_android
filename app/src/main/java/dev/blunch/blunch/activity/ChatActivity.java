package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

import dev.blunch.blunch.R;
import dev.blunch.blunch.domain.ChatMessage;

public class ChatActivity extends AppCompatActivity {

    private FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.chat_list);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        Firebase.setAndroidContext(getApplicationContext());

        Firebase mRef = new Firebase("https://blunch.firebaseio.com/chats/-1");


        mAdapter = new FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder>(ChatMessage.class, R.layout.message, ChatMessageViewHolder.class, mRef) {
            @Override
            public void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, ChatMessage chatMessage, int position) {
                chatMessageViewHolder.contentText.setText(chatMessage.getContent());
            }
        };
        recycler.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    public static class ChatMessageViewHolder extends RecyclerView.ViewHolder {
        TextView contentText;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            contentText = (TextView)itemView.findViewById(R.id.content);
        }
    }
}
