package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.MeetingsDao;
import com.example.final_project.dto.MeetingDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.utilities.LoggingManager;
import com.example.final_project.utilities.mappers.MeetingMapper;
import com.example.final_project.utilities.mappers.UserMapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Meeting services as additional layer to communicate with database and controlled
 */
public class MeetingsService {
    private final MeetingsDao meetingsDao;
    private final MeetingMapper meetingMapper = Mappers.getMapper(MeetingMapper.class);
    private final ConnectionPool connectionPool;

    /**
     * @param connectionPool pool of connections used to request to database
     */
    public MeetingsService(ConnectionPool connectionPool) {
        this.meetingsDao = new MeetingsDao(connectionPool);
        this.connectionPool = connectionPool;
    }


    /**
     * @param userDTO user whose meetings will be selected
     * @return list of users meetings
     */
    public List<MeetingDTO> getUserMeetings(UserDTO userDTO) {
        return meetingsDao.getUserMeetings(Mappers.getMapper(UserMapper.class).userDTOToUser(userDTO)).stream()
                .map(meetingMapper::meetingToMeetingDTO).collect(Collectors.toList());

    }

    /**
     * @param meeting to be inserted to database
     */
    public void addEvent(MeetingDTO meeting){
        MessagesService messagesService = new MessagesService(connectionPool);
        messagesService.notifyStudentsAboutMeeting(meeting);
        meetingsDao.addMeeting(meetingMapper.meetingDTOToMeeting(meeting));
        LoggingManager.logAuditTrail(LoggingManager.Change.CREATED,meeting);
    }
}
