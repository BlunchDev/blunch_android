package dev.blunch.blunch.repositories;

import android.content.Context;

import com.firebase.client.DataSnapshot;
import dev.blunch.blunch.domain.ChatMessage;
import dev.blunch.blunch.utils.FirebaseRepository;

/**
 * Chat Message Repository Class
 * @author albert
 */
public class ChatMessageRepository extends FirebaseRepository<ChatMessage> {

    public ChatMessageRepository(Context context) {
        super(context);
    }

    @Override
    protected ChatMessage convert(DataSnapshot data) {
        if (data == null) return null;

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(data.getKey());

        for (DataSnapshot d : data.getChildren()) {
            if ("author".equals(d.getKey())) {
                chatMessage.setAuthor(d.getValue(String.class));
            } else if ("content".equals(d.getKey())) {
                chatMessage.setContent(d.getValue(String.class));
            } else if ("createdAt".equals(d.getKey())) {
                chatMessage.setCreatedAt(d.getValue(Long.class));
            }
        }
        return chatMessage;
    }

    @Override
    public String getObjectReference() {
        return "chats";
    }
}
