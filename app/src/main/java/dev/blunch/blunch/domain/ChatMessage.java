package dev.blunch.blunch.domain;

/**
 * Created by casassg on 03/05/16.
 *
 * @author casassg
 */
public class ChatMessage {
    private String author;
    private String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
