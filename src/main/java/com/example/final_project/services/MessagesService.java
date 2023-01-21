package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.MessageDao;
import com.example.final_project.database.entities.message.Message;
import com.example.final_project.dto.MessageDTO;
import com.example.final_project.database.entities.message.Status;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.utilities.MessageMapper;
import com.example.final_project.utilities.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MessagesService {
    private final MessageDao messageDao;

    public MessagesService(ConnectionPool connectionPool) {
        this.messageDao = new MessageDao(connectionPool);
    }

    public List<MessageDTO> getUserMessages(UserDTO userDTO) {
        return messageDao.findMessagesByReceiver(UserMapper.userDTOToUser(userDTO)).stream()
                .map(MessageMapper::messageToMessageDTO)
                .sorted(
                (message, message1) -> message.getStatus().compareTo(message1.getStatus()) * -1
        ).collect(Collectors.toList());
    }

    public void sendMessage(UserDTO receiver, UserDTO sender, String subject, String text) {
        messageDao.sendMessage(new Message(-1,
                text,
                subject,
                UserMapper.userDTOToUser(sender),
                UserMapper.userDTOToUser(receiver),
                Status.UNREAD.name()));
    }

    public void changeStatus(List<MessageDTO> messageDTOList) {
        messageDao.changeStatus(messageDTOList
                .stream()
                .map(MessageMapper::messageDTOToMessage)
                .collect(Collectors.toList()));
    }
}
