package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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

        final RecyclerView recycler = (RecyclerView) findViewById(R.id.chat_list);
        assert recycler!=null;
        recycler.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recycler.setLayoutManager(layout);
        Firebase.setAndroidContext(getApplicationContext());

        final Firebase mRef = new Firebase("https://blunch.firebaseio.com/chats/-1");


        mAdapter = new FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder>(ChatMessage.class, R.layout.message, ChatMessageViewHolder.class, mRef) {
            @Override
            public void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, ChatMessage chatMessage, int position) {
                chatMessageViewHolder.contentText.setText(chatMessage.getContent());
                chatMessageViewHolder.authorText.setText(chatMessage.getAuthor());
                DateFormat sdf = SimpleDateFormat.getDateTimeInstance();
                chatMessageViewHolder.dateText.setText(sdf.format(chatMessage.createdAt()));
            }
        };
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recycler.smoothScrollToPosition(mAdapter.getItemCount()-1);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        recycler.setAdapter(mAdapter);


        Button sendButton = (Button) findViewById(R.id.send);
        final EditText messageText = (EditText) findViewById(R.id.new_message);
        assert sendButton != null;
        assert messageText != null;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMessage message = new ChatMessage("TEST",messageText.getText().toString());
                mRef.push().setValue(message);
                messageText.setText("");
            }
        });
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
        TextView dateText;
        TextView authorText;
        TextView contentText;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            contentText = (TextView)itemView.findViewById(R.id.content);
            authorText = (TextView)itemView.findViewById(R.id.author);
            dateText = (TextView)itemView.findViewById(R.id.date);
        }
    }
}
