package dev.blunch.blunch.domain;

import java.util.Date;

import dev.blunch.blunch.utils.Entity;

/**
 * Created by casassg on 03/05/16.
 *
 * @author casassg
 */
public class ChatMessage implements Entity {

    private String id;
    private String author;
    private String content;
    private Long createdAt;

    public ChatMessage() {
        author="";
        content="";
        createdAt = new Date().getTime();
    }

    public ChatMessage(String author, String content) {
        this.author = author;
        this.content = content;
        this.createdAt = new Date().getTime();
    }

    public ChatMessage(String author, String content, Long createdAt) {
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
    }

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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Date createdAt(){
        return new Date(createdAt);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}

