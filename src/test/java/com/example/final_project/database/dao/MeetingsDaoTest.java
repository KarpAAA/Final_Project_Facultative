package com.example.final_project.database.dao;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.course.State;
import com.example.final_project.database.entities.meeting.Meeting;
import com.example.final_project.database.entities.user.Blocked_State;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.database.entities.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class MeetingsDaoTest {

    @Mock
    Connection mockConnection;
    @Mock
    PreparedStatement mockPreparedStmnt;
    @Mock
    Statement mockStatement;
    @Mock
    ConnectionPool mockConnectionPool;
    @Mock
    ResultSet mockResultSet;

    MeetingsDao meetingsDao;

    Meeting meeting =
            new Meeting("title", "description", "link",
                    getTestCourse(),
                    Date.valueOf(LocalDate.now()),
                    Time.valueOf(LocalTime.now()));

    private Course getTestCourse() {
        return new Course("title", "topic", "description",
                new User("login", "password", "name", Role.Student, "1@1", 18, new Date(1), "Surname",
                        "+3809641345", new byte[]{1, 2, 3}, Blocked_State.UNLOCKED, 1000)
                , new Date(1), new Date(1), 120, 110, 120, State.InProgress, new byte[]{1, 2, 3});
    }

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        meetingsDao = new MeetingsDao(mockConnectionPool);

        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(mockResultSet.getString(6)).thenReturn("READ");


    }


    @Test
    void addMeeting() throws SQLException {
        meetingsDao.addMeeting(meeting);

        verify(mockConnectionPool).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(4)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt).setDate(anyInt(), any());
        verify(mockPreparedStmnt).setTime(anyInt(), any());
        verify(mockPreparedStmnt).executeUpdate();
        verify(mockConnectionPool).releaseConnection(mockConnection);
    }
}