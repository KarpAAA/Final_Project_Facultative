package com.example.final_project.entities.message;


import com.example.final_project.entities.user.User;

public class MessageBuilder {
    private int id;
    private String text;
    private String subject = "";
    private User sender;
    private User receiver;
    private Status status;
    public Message buildMessage() {
        /*if (!validationOfNecessaryFields())
            throw new IllegalArgumentException("Not all necessary fields entered");*/

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

    private boolean validationOfNecessaryFields() {
        return !text.equals("") && sender != null && receiver != null;
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
