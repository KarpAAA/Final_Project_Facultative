package com.example.final_project.dto;

public class MessageDTO {
    private final int id;
    private final String text;
    private final String subject;
    private final UserDTO sender;
    private final UserDTO receiver;
    private final String status;

    public MessageDTO(int id, String text, String subject, UserDTO sender, UserDTO receiver, String status) {
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

    public UserDTO getSender() {
        return sender;
    }

    public UserDTO getReceiver() {
        return receiver;
    }

    public String getSubject() {
        return subject;
    }

    public String getStatus() {
        return status;
    }
}
