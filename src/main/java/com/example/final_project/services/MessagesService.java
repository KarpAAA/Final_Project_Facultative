package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.MessageDao;
import com.example.final_project.database.entities.message.Message;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.MeetingDTO;
import com.example.final_project.dto.MessageDTO;
import com.example.final_project.database.entities.message.Status;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.utilities.mappers.CourseMapper;
import com.example.final_project.utilities.mappers.MessageMapper;
import com.example.final_project.utilities.mappers.UserMapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MessagesService {
    private final MessageDao messageDao;
    private final MessageMapper messageMapper = Mappers.getMapper(MessageMapper.class);
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final ConnectionPool connectionPool;

    public MessagesService(ConnectionPool connectionPool) {
        this.messageDao = new MessageDao(connectionPool);
        this.connectionPool = connectionPool;
    }

    public List<MessageDTO> getUserMessages(UserDTO userDTO) {
        List<MessageDTO> list = messageDao.findMessagesByReceiver(userMapper.userDTOToUser(userDTO))
                .stream()
                .map(messageMapper::messageToMessageDTO)
                .collect(Collectors.toList());
        Collections.reverse(list);
        return list;

    }

    public void sendMessage(UserDTO receiver, UserDTO sender, String subject, String text) {
        messageDao.sendMessage(new Message(-1,
                text,
                subject,
                userMapper.userDTOToUser(sender),
                userMapper.userDTOToUser(receiver),
                Status.UNREAD.name()));
    }

    public void changeStatus(List<MessageDTO> messageDTOList) {
        messageDao.changeStatus(messageDTOList
                .stream()
                .map(messageMapper::messageDTOToMessage)
                .collect(Collectors.toList()));
    }
    public void clearUserMessages(UserDTO userDTO){
        messageDao.clearUserMessages(userMapper.userDTOToUser(userDTO));
    }

    public List<MessageDTO> getMessagesBySender(UserDTO userDTO) {
        List<MessageDTO> list = messageDao.findMessagesBySender(userMapper.userDTOToUser(userDTO))
                .stream()
                .map(messageMapper::messageToMessageDTO)
                .collect(Collectors.toList());
        if(userDTO.getRole() == Role.Teacher){
            list = list.stream().filter( message -> message.getSubject().compareTo("Invitation to meeting") != 0
            && message.getSubject().compareTo("Adding to Course") != 0).toList();
        }
        return list;
    }

    public void notifyUserAddingToCourse(UserDTO user, CourseDTO course) {
        MessagesService messagesService = new MessagesService(connectionPool);
        String message = "Dear " + user.getLogin() + "!" +
                "<br>You was admitted to " + course.getTitle() + " course"
                +"<br>Enjoy studying with Be To Study";
        messagesService.sendMessage(user,course.getTeacher(),"Adding to Course",message);
    }
    public void notifyStudentsAboutMeeting(MeetingDTO meeting) {
        CourseDTO course = meeting.getCourse();
        UserService userService = new UserService(connectionPool);

        Map<UserDTO, Integer> map = userService.getUsersMarksMap(course);

        List<UserDTO> courseStudentList = new ArrayList<>();
        for(var entry:map.entrySet()){
            courseStudentList.add(entry.getKey());
        }

        for(var student:courseStudentList){
            String message = "Dear " + student.getLogin() + "!<br><br>" +
                    course.getTeacher().getName() + " has arranged meeting at "
                    + meeting.getStartDate().toString()+" "+meeting.getTime().toLocalTime().toString() + " <br>To join follow the link below: <br>" +meeting.getLink();
            sendMessage(student,meeting.getCourse().getTeacher(),"Invitation to meeting"
                    ,message);
        }

    }
    public void notifyStudentsAboutSuccessfullyRegistration(UserDTO user){
        UserService userService = new UserService(connectionPool);
        String message = "Dear " + user.getLogin() + "!" +
                "<br>Welcome to our site" +
                "<br>Wish you good using of provided resource" +
                "<br>Feel free to contact us!";
        sendMessage(user,userService.findUser("Be To Study"),"Registration", message);
    }
}
