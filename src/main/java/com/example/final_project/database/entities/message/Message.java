package com.example.final_project.database.entities.message;

import com.example.final_project.database.entities.user.User;

public class Message {
    private final int id;
    private final String text;
    private final String subject;
    private final User sender;
    private final User receiver;
    private final String status;

    public Message(int id, String text, String subject, User sender, User receiver, String status) {
        this.id = id;
        this.text = text;
        this.subject = subject;
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getSubject() {
        return subject;
    }

    public String getStatus() {
        return status;
    }
}
