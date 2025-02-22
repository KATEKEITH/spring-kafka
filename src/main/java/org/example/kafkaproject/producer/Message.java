package org.example.kafkaproject.producer;

import java.time.LocalDateTime;

public class Message {

    private String id;
    private String content;
    private LocalDateTime timestamp;
    private int size; // 바이트 단위

    public Message() {
        this.timestamp = LocalDateTime.now();
    }

    public Message(String content) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.content = content;
        this.size = content.getBytes().length;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getSize() {
        return size;
    }

    public void setContent(String content) {
        this.content = content;
        this.size = content.getBytes().length;
    }

    @Override
    public String toString() {
        return String.format(
                "Message{id='%s', content='%s', timestamp=%s, size=%d bytes}",
                id, content, timestamp, size
        );
    }

}
