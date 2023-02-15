package com.example.final_project.database.entities.message;


import com.example.final_project.database.entities.user.User;

/**
 * Message builder which creates message entity
 */
public class MessageBuilder {
    private int id;
    private String text;
    private String subject = "";
    private User sender;
    private User receiver;
    private Status status;
    public Message buildMessage() {

        return new Message(id, text, subject, sender, receiver,status.toString());
    }

    public MessageBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    public MessageBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public MessageBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public MessageBuilder setSender(User sender) {
        this.sender = sender;
        return this;
    }

    public MessageBuilder setReceiver(User receiver) {
        this.receiver = receiver;
        return this;
    }

    public MessageBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }
}
