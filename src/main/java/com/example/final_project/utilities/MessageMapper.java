package com.example.final_project.utilities;

import com.example.final_project.database.entities.message.Message;
import com.example.final_project.dto.MessageDTO;


public class MessageMapper {
    public static MessageDTO messageToMessageDTO(Message message){
        if(message==null) return null;
        return new MessageDTO(
                message.getId(),
                message.getText(),
                message.getSubject(),
                UserMapper.userToUserDTO(message.getSender()),
                UserMapper.userToUserDTO(message.getReceiver()),

                message.getStatus());
    }
    public static Message messageDTOToMessage(MessageDTO messageDTO){
        if(messageDTO==null) return null;
        return new Message(
                messageDTO.getId(),
                messageDTO.getText(),
                messageDTO.getSubject(),
                UserMapper.userDTOToUser(messageDTO.getSender()),
                UserMapper.userDTOToUser(messageDTO.getReceiver()),
                messageDTO.getStatus());
    }
}
