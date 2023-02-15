package com.example.final_project.database.dao;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.meeting.Meeting;
import com.example.final_project.database.entities.meeting.MeetingBuilder;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.database.entities.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * MeetingsDao which implements functions to interact with database
 */
public class MeetingsDao {
    private ConnectionPool connectionPool;

    /**
     * @param connectionPool pool of connections used to request to database
     */
    public MeetingsDao(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * @param user whose meetings will be selected
     * @return list of user meetings
     */
    public List<Meeting> getUserMeetings(User user) {
        CoursesDao coursesDao = new CoursesDao(connectionPool);
        List<Course> courseList = new ArrayList<>();

        if (user.getRole() == Role.Teacher) courseList = coursesDao.getAllTeacherCourses(user, 0, Integer.MAX_VALUE);
        else if (user.getRole() == Role.Student) courseList = coursesDao.getUserCourses(user);

        Connection connection = connectionPool.getConnection();
        List<Meeting> meetingsList = new ArrayList<>();

        try {
            StringBuilder request = new StringBuilder("SELECT * FROM Meeting WHERE Course_title = ");
            if (courseList.size() == 0) {
                return meetingsList;
            } else {
                request.append('\'').append(courseList.get(0).getTitle()).append('\'');
                for (int i = 1; i < courseList.size(); ++i)
                    request.append(" OR Course_title = ").append('\'').append(courseList.get(i).getTitle()).append('\'');
            }
            PreparedStatement statement = connection.prepareStatement(request.toString());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                meetingsList.add(getMeeting(resultSet));
            }

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return meetingsList;

    }

    /**
     * @param meeting to add to database
     */
    public void addMeeting(Meeting meeting) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT into Meeting (id,Course_title,description,title,link,start_date,time) VALUES(null,?,?,?,?,?,?)");

            statement.setString(1, meeting.getCourse().getTitle());
            statement.setString(2, meeting.getDescription());
            statement.setString(3, meeting.getTitle());
            statement.setString(4, meeting.getLink());
            statement.setDate(5, meeting.getStartDate());
            statement.setTime(6, meeting.getTime());


            statement.executeUpdate();

            connectionPool.releaseConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param resultSet received dataSet from database
     * @return formed meeting from Result Set
     * @throws SQLException
     */
    private Meeting getMeeting(ResultSet resultSet) throws SQLException {
        MeetingBuilder meetingBuilder = new MeetingBuilder();
        CoursesDao coursesDao = new CoursesDao(connectionPool);


        meetingBuilder.setDescription(resultSet.getString("description"))
                .setTitle(resultSet.getString("title"))
                .setLink(resultSet.getString("link"))
                .setStartDate(resultSet.getDate("start_date"))
                .setTime(resultSet.getTime("time"))
                .setCourse(coursesDao.findCourse(resultSet.getString("Course_title")));

        return meetingBuilder.buildMeeting();
    }


}
