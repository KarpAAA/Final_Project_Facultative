package com.example.final_project.database.dao;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.database.entities.course.State;
import com.example.final_project.database.entities.user.Blocked_State;
import com.example.final_project.database.entities.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class CoursesDaoTest {

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

    CoursesDao coursesDao;
    private Course getTestCourse() {
        return new Course("title", "topic", "description",
                new User("login", "password", "name", Role.Student, "1@1", 18, new Date(1), "Surname",
                        "+3809641345", new byte[]{1, 2, 3}, Blocked_State.UNLOCKED, 100)
                , new Date(1), new Date(1), 120, 120, 120, State.InProgress, new byte[]{1, 2, 3});
    }

    private User getTestUser() {
        return new User("login", "password", "name", Role.Student, "1@1", 18, new Date(1), "Surname",
                "+3809641345", new byte[]{1, 2, 3}, Blocked_State.UNLOCKED, 100);
    }
    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        coursesDao = new CoursesDao(mockConnectionPool);

        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE,Boolean.FALSE);

    }

    @Test
    void addCourseNormalCase() throws SQLException {

        coursesDao.addCourse(getTestCourse());
        verify(mockConnectionPool).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(4)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(2)).setDate(anyInt(), any());
        verify(mockPreparedStmnt, times(5)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).setBinaryStream(anyInt(), any());

        verify(mockPreparedStmnt).executeUpdate();
        verify(mockConnectionPool).releaseConnection(mockConnection);

    }


    @Test
    void updateCourseNormalCase() throws SQLException {
        coursesDao.updateCourse(getTestCourse());
        verify(mockConnectionPool).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(4)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(2)).setDate(anyInt(), any());
        verify(mockPreparedStmnt, times(4)).setInt(anyInt(), anyInt());


        verify(mockPreparedStmnt).executeUpdate();
        verify(mockConnectionPool).releaseConnection(mockConnection);
    }

    @Test
    void deleteCourse() throws SQLException {
        coursesDao.deleteCourse(getTestCourse().getTitle());
        verify(mockConnectionPool).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStmnt).setString(anyInt(),anyString());
        verify(mockPreparedStmnt).executeUpdate();
        verify(mockConnectionPool).releaseConnection(mockConnection);

    }

    @Test
    void setPhotoToCourse()  throws SQLException {
        coursesDao.setPhotoToCourse(getTestCourse());
        verify(mockConnectionPool).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(),anyString());
        verify(mockPreparedStmnt, times(1)).setBinaryStream(anyInt(),any());
        verify(mockPreparedStmnt).executeUpdate();
        verify(mockConnectionPool).releaseConnection(mockConnection);

    }

    @Test
    void findCourse() throws SQLException {
        coursesDao.findCourse(getTestCourse().getTitle());

        verify(mockConnectionPool, times(2)).getConnection();
        verify(mockConnection, times(2)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(),anyString());
        verify(mockPreparedStmnt, times(2)).executeQuery();

        verify(mockResultSet,times(3)).next();
        verify(mockResultSet,times(5)).getString(anyInt());
        verify(mockResultSet,times(2)).getDate(anyInt());
        verify(mockResultSet,times(3)).getInt(anyInt());
        verify(mockResultSet,times(1)).getBlob(anyInt());
        verify(mockConnectionPool, times(2)).releaseConnection(mockConnection);


    }



    @Test
    void registerStudentToCourse() throws SQLException {
        coursesDao.registerStudentToCourse("", getTestCourse().getTitle());

        verify(mockConnectionPool).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setString(anyInt(),anyString());
        verify(mockPreparedStmnt, times(1)).setDate(anyInt(),any());
        verify(mockPreparedStmnt).executeUpdate();
        verify(mockConnectionPool).releaseConnection(mockConnection);
    }


    @Test
    void removeStudentFromCourse() throws SQLException {
        coursesDao.removeStudentFromCourse("", getTestCourse().getTitle());
        verify(mockConnectionPool, atLeast(1)).getConnection();
        verify(mockConnection, atLeast(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(2)).setString(anyInt(),anyString());

        verify(mockPreparedStmnt, atLeast(1)).executeUpdate();
        verify(mockConnectionPool, atLeast(1)).releaseConnection(mockConnection);
    }

    @Test
    void blockStudentToCourse() throws SQLException {
        coursesDao.blockStudentToCourse("", getTestCourse().getTitle());

        verify(mockConnectionPool).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setString(anyInt(),anyString());

        verify(mockPreparedStmnt).executeUpdate();
        verify(mockConnectionPool).releaseConnection(mockConnection);
    }

    @Test
    void selectStatedAmountOfUserCourses() throws SQLException {
        coursesDao.selectStatedAmountOfUserCourses(getTestUser(),0,12,State.InProgress);

        verify(mockConnectionPool, times(2)).getConnection();
        verify(mockConnection, times(2)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(),anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(),anyInt());
        verify(mockPreparedStmnt,times(2)).executeQuery();

        verify(mockResultSet,atLeast(1)).next();
        verify(mockResultSet,times(5)).getString(anyInt());
        verify(mockResultSet,times(2)).getDate(anyInt());
        verify(mockResultSet,times(3)).getInt(anyInt());
        verify(mockResultSet,times(1)).getBlob(anyInt());
        verify(mockConnectionPool, times(2)).releaseConnection(mockConnection);
    }



    @Test
    void getAllTeacherCourses() throws SQLException {
        coursesDao.getAllTeacherCourses(getTestUser(), 0 ,100);

        verify(mockConnectionPool,atLeast(1)).getConnection();
        verify(mockConnection,atLeast(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(1)).setString(anyInt(),anyString());
        verify(mockPreparedStmnt,atLeast(1)).executeQuery();
        verify(mockResultSet,atLeast(1)).next();

        verify(mockConnectionPool,atLeast(1)).releaseConnection(mockConnection);
    }


    @Test
    void getAllTopics() throws SQLException {
        coursesDao.getAllTopics();

        verify(mockConnectionPool, atLeast(1)).getConnection();
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement,times(1)).executeQuery(anyString());
        verify(mockResultSet,atLeast(1)).next();
        verify(mockConnectionPool, atLeast(1)).releaseConnection(mockConnection);
    }
}