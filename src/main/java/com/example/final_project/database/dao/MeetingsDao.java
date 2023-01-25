package com.example.final_project.database.dao;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.meeting.Meeting;
import com.example.final_project.database.entities.meeting.MeetingBuilder;
import com.example.final_project.database.entities.message.Message;
import com.example.final_project.database.entities.message.MessageBuilder;
import com.example.final_project.database.entities.message.Status;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.database.entities.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MeetingsDao {
    private ConnectionPool connectionPool;

    public MeetingsDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public List<Meeting> findUserMeetings(User user) {
        CoursesDao coursesDao = new CoursesDao(connectionPool);
        List<Course> courseList = new ArrayList<>();

        if(user.getRole() == Role.Teacher) courseList = coursesDao.getAllTeacherCourses(user,0, Integer.MAX_VALUE);
        else if(user.getRole() == Role.Student) courseList = coursesDao.getUserCourses(user);

        Connection connection = connectionPool.getConnection();
        List<Meeting> meetingsList = new ArrayList<>();
        try {



            StringBuilder request =  new StringBuilder("SELECT * FROM Meeting WHERE Course_title = ");
            if(courseList.size() == 0){return meetingsList;}
            else{
                request.append('\'').append(courseList.get(0).getTitle()).append('\'');
                for(int i=1;i<courseList.size();++i) request.append(" OR Course_title = ").append('\'').append(courseList.get(i).getTitle()).append('\'');
            }
            PreparedStatement statement = connection.prepareStatement(request.toString());
            ResultSet resultSet  = statement.executeQuery();

            while (resultSet.next()) {
                meetingsList.add(getMeeting(resultSet));
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return meetingsList;

    }


    public void addMeeting(Meeting meeting) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT into Meeting (id,description,title,link,startDate,time, Course_title) VALUES(null,?,?,?,?,?,?)");

            statement.setString(1, meeting.getDescription());
            statement.setString(2, meeting.getTitle());
            statement.setString(3, meeting.getLink());
            statement.setDate(4, meeting.getStartDate());
            statement.setTime(5, meeting.getTime());
            statement.setString(6, meeting.getCourse().getTitle());

            statement.executeUpdate();

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




    private Meeting getMeeting(ResultSet resultSet) throws SQLException {
        MeetingBuilder meetingBuilder = new MeetingBuilder();
        CoursesDao coursesDao = new CoursesDao(connectionPool);


        meetingBuilder.setDescription(resultSet.getString(2))
                .setTitle( resultSet.getString(3))
                .setLink(resultSet.getString(4))
                .setStartDate(resultSet.getDate(5))
                .setTime(resultSet.getTime(6))
                .setCourse(coursesDao.findCourse(resultSet.getString(7)));

        return meetingBuilder.buildMeeting();
    }


}
