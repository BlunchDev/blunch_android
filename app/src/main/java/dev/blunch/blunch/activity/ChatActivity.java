package dev.blunch.blunch.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import dev.blunch.blunch.domain.Menu;
import dev.blunch.blunch.domain.User;
import dev.blunch.blunch.services.MenuService;
import dev.blunch.blunch.services.ServiceFactory;
import dev.blunch.blunch.utils.Preferences;

@SuppressWarnings("all")
public class ChatActivity extends AppCompatActivity {

    private MenuService menuService;
    private String menuId;

    private FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder> mAdapter;
    public static final String MENU_ID = "menu_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menuService = ServiceFactory.getMenuService(getApplicationContext());

        menuId = "GLOBAL";
        if (getIntent().getStringExtra(MENU_ID) != null)
            menuId = getIntent().getStringExtra(MENU_ID);
        if (!"GLOBAL".equals(menuId)) {
            Menu menu = menuService.getMenu(menuId);
            setTitle("Chat " + menu.getName());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        menuService.resetMessageCountToActualUser(menuId);

        final RecyclerView recycler = (RecyclerView) findViewById(R.id.chat_list);
        assert recycler != null;
        recycler.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recycler.setLayoutManager(layout);
        Firebase.setAndroidContext(getApplicationContext());

        final Firebase mRef = new Firebase("https://blunch.firebaseio.com/chats/" + String.valueOf(menuId));


        mAdapter = new FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder>(ChatMessage.class, R.layout.message, ChatMessageViewHolder.class, mRef) {
            @Override
            public void populateViewHolder(ChatMessageViewHolder chatMessageViewHolder, ChatMessage chatMessage, int position) {
                chatMessageViewHolder.contentText.setText(chatMessage.getContent());
                String email = chatMessage.getAuthor();
                User user = menuService.findUserByEmail(email);
                chatMessageViewHolder.authorText.setText(user.getName());
                try {
                    chatMessageViewHolder.authorImage.setImageDrawable(user.getImageRounded(getResources()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DateFormat sdf = SimpleDateFormat.getDateTimeInstance();
                chatMessageViewHolder.dateText.setText(sdf.format(chatMessage.createdAt()));
            }
        };
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mAdapter.getItemCount() > 0) {
                    recycler.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        recycler.setAdapter(mAdapter);
        setupSendAction(mRef);
    }

    private void setupSendAction(final Firebase mRef) {
        Button sendButton = (Button) findViewById(R.id.send);
        final EditText messageText = (EditText) findViewById(R.id.new_message);
        assert sendButton != null;
        assert messageText != null;
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = messageText.getText().toString();
                if (messageContent == null || "".equals(messageContent)) {
                    Snackbar.make(findViewById(R.id.chat_layout), "Los mensajes vacios no són permitidos", Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                ChatMessage message = new ChatMessage(Preferences.getCurrentUserEmail(), messageContent);

                mRef.push().setValue(message);
                messageText.setText("");
                menuService.increaseMessageCountToOtherUsers(menuId);
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
        ImageView authorImage;
        TextView dateText;
        TextView authorText;
        TextView contentText;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);
            contentText = (TextView) itemView.findViewById(R.id.content);
            authorText = (TextView) itemView.findViewById(R.id.author);
            dateText = (TextView) itemView.findViewById(R.id.date);
            authorImage = (ImageView) itemView.findViewById(R.id.user_icon);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        menuService.resetMessageCountToActualUser(menuId);
    }
}
