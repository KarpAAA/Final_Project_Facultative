package com.example.final_project.database.dao;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.task.Task;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.database.entities.course.State;
import com.example.final_project.database.entities.user.Blocked_State;
import com.example.final_project.database.entities.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class UserDaoTest {

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

    UserDao userDao;

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
    void setUp() throws SQLException, InstantiationException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        userDao = new UserDao(mockConnectionPool);

        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);


    }

    @Test
    void selectAllUsers() throws SQLException {
        userDao.selectAllUsers();
        verify(mockConnectionPool).getConnection();
        verify(mockConnection).createStatement();
        verify(mockStatement).executeQuery(anyString());
        verify(mockResultSet, atLeast(1)).next();

        verify(mockConnectionPool).releaseConnection(mockConnection);
    }

    @Test
    void insertUser() throws SQLException {
        userDao.insertUser(getTestUser());
        verify(mockConnectionPool, atLeast(1)).getConnection();
        verify(mockConnection, atLeast(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(4)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, atLeast(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, atLeast(1)).executeUpdate();


        verify(mockConnectionPool, atLeast(2)).releaseConnection(mockConnection);
    }

    @Test
    void updateUser() throws SQLException {
        userDao.updateUser(getTestUser());
        verify(mockConnectionPool).getConnection();
        verify(mockConnection, atLeast(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(4)).setString(anyInt(), anyString());

        verify(mockPreparedStmnt, atLeast(1)).executeUpdate();


        verify(mockConnectionPool).releaseConnection(mockConnection);
    }

    @Test
    void deleteUser() throws SQLException {
        userDao.deleteUser(getTestUser());

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }

    @Test
    void getUsersByRole() throws SQLException {
        userDao.getUsersByRole(Role.Student);

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, atLeast(1)).next();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }

    @Test
    void updateUserPhoto() throws SQLException {
        userDao.updateUserPhoto(getTestUser());

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).setBinaryStream(anyInt(), any());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }

    @Test
    void checkIfLoginAvailable() throws SQLException {
        userDao.checkIfLoginAvailable(getTestUser().getLogin());

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, atLeast(1)).next();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }

    @Test
    void selectUsersByCourse() throws SQLException {
        userDao.selectUsersByCourse(getTestCourse());

        verify(mockConnectionPool, atLeast(1)).getConnection();
        verify(mockConnection, atLeast(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, atLeast(1)).executeQuery();
        verify(mockResultSet, atLeast(1)).next();
        verify(mockConnectionPool, atLeast(1)).releaseConnection(mockConnection);
    }

    @Test
    void getUserRegisteredState() throws SQLException {
        userDao.getUserRegisteredState(getTestUser(), getTestCourse());
        verify(mockConnectionPool).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, atLeast(1)).next();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }

    @Test
    void getUserGradeForCourse() throws SQLException {
        userDao.getUserGradeForCourse(getTestUser().getLogin(), getTestCourse().getTitle());
        verify(mockConnectionPool).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, atLeast(1)).next();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);

    }

    @Test
    void getRegisteredUserToCourse() throws SQLException {
        userDao.getRegisteredUserToCourse(getTestCourse().getTitle());
        verify(mockConnectionPool, atLeast(1)).getConnection();
        verify(mockConnection, atLeast(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, atLeast(1)).executeQuery();
        verify(mockResultSet, atLeast(1)).next();
        verify(mockConnectionPool, atLeast(1)).releaseConnection(mockConnection);

    }

//    @Test
//    void saveMarks() throws SQLException {
//        HashMap<User, Map<Task,Integer>> map = new HashMap<>();
//        map.put(getTestUser(), List.of(getTestUser()));
//        userDao.saveMarks(map, getTestCourse().getTitle());
//        verify(mockConnectionPool, times(1)).getConnection();
//        verify(mockConnection, times(1)).prepareStatement(anyString());
//        verify(mockPreparedStmnt, times(2)).setString(anyInt(), anyString());
//        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
//        verify(mockPreparedStmnt, times(1)).executeUpdate();
//
//        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
//
//    }

    @Test
    void updateBlockedState() throws SQLException {

        userDao.updateBlockedState(getTestUser(),Blocked_State.UNLOCKED);
        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeUpdate();

        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);

    }

    @Test
    void identifyUser() throws SQLException {
        userDao.identifyUser(getTestUser().getLogin(), getTestUser().getPassword());
        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setString(anyInt(), anyString());

        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet,atLeast(1)).next();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }


    @Test
    void updateUserPassword() throws SQLException {
        userDao.updateUserPassword(getTestUser(), getTestUser().getPassword());
        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setString(anyInt(), anyString());

        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }

    @Test
    void saveTaskMarks()  throws SQLException {
        Map<User, Map<Task,Integer>> map = new HashMap<>();
        Map<Task,Integer> taskMap = new HashMap<>();
        taskMap.put(new Task(1,"title","condition",getTestCourse()),10);
        map.put(getTestUser(),taskMap);
        userDao.saveTaskMarks(map);
        verify(mockConnectionPool, atLeast(1)).getConnection();
        verify(mockConnection, atLeast(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, atLeast(2)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, atLeast(1)).addBatch();

        verify(mockPreparedStmnt, times(1)).executeBatch();
        verify(mockConnectionPool, atLeast(1)).releaseConnection(mockConnection);
    }

}