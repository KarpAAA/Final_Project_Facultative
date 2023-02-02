package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.MeetingsDao;
import com.example.final_project.dto.MeetingDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.utilities.mappers.MeetingMapper;
import com.example.final_project.utilities.mappers.UserMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

public class MeetingsService {
    private final MeetingsDao meetingsDao;
    private final MeetingMapper meetingMapper = Mappers.getMapper(MeetingMapper.class);
    private final ConnectionPool connectionPool;
    public MeetingsService(ConnectionPool connectionPool) {
        this.meetingsDao = new MeetingsDao(connectionPool);
        this.connectionPool = connectionPool;
    }


    public List<MeetingDTO> getUserMeetings(UserDTO userDTO) {
        return meetingsDao.findUserMeetings(Mappers.getMapper(UserMapper.class).userDTOToUser(userDTO)).stream()
                .map(meetingMapper::meetingToMeetingDTO).collect(Collectors.toList());

    }
    public void addEvent(MeetingDTO meeting){
        MessagesService messagesService = new MessagesService(connectionPool);
        messagesService.notifyStudentsAboutMeeting(meeting);
        meetingsDao.addMeeting(meetingMapper.meetingDTOToMeeting(meeting));
    }
}
