package com.example.final_project.utilities.mappers;

import com.example.final_project.database.entities.meeting.Meeting;
import com.example.final_project.database.entities.message.Message;
import com.example.final_project.dto.MeetingDTO;
import com.example.final_project.dto.MessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper {

    MessageDTO messageToMessageDTO(Message message);
    Message messageDTOToMessage(MessageDTO messageDTO);
}

