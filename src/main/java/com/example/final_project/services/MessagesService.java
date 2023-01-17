package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.MessageDao;
import com.example.final_project.entities.message.Message;
import com.example.final_project.entities.message.MessageBuilder;
import com.example.final_project.entities.message.Status;
import com.example.final_project.entities.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class MessagesService {
    private final MessageDao messageDao;

    public MessagesService(ConnectionPool connectionPool) {
        this.messageDao = new MessageDao(connectionPool);
    }

    public List<Message> getUserMessages(User user) {
        return messageDao.findMessagesByReceiver(user).stream().sorted(
                (message, message1) -> message.getStatus().compareTo(message1.getStatus()) * -1
        ).collect(Collectors.toList());
    }

    public void sendMessage(User receiver, User user, String subject, String text) {
        messageDao.sendMessage((new MessageBuilder()).setReceiver(receiver)
                .setSender(user)
                .setStatus(Status.UNREAD)
                .setSubject(subject)
                .setText(text).buildMessage());
    }

    public void changeStatus(List<Message> messageList) {
        messageDao.changeStatus(messageList);
    }
}
