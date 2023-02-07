package com.example.final_project.database.dao;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.course.State;
import com.example.final_project.database.entities.task.Task;
import com.example.final_project.database.entities.user.Blocked_State;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.database.entities.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TaskDaoTest {


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

    TaskDao taskDao;
    Course course = getTestCourse();
    Task task = new Task(1,"title", "condition",course);
    User user = new User("login", "password", "name", Role.Student, "1@1", 18, new Date(1), "Surname",
                "+3809641345", new byte[]{1, 2, 3}, Blocked_State.UNLOCKED, 100);

    private Course getTestCourse() {
        return new Course("title", "topic", "description",
                new User("login", "password", "name", Role.Student, "1@1", 18, new Date(1), "Surname",
                        "+3809641345", new byte[]{1, 2, 3}, Blocked_State.UNLOCKED, 100)
                , new Date(1), new Date(1), 120, 120, 120, State.InProgress, new byte[]{1, 2, 3});
    }

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        taskDao = new TaskDao(mockConnectionPool);

        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

    }

    @Test
    void getUserSolution() throws SQLException {
        taskDao.getUserSolution(user,task);
        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());

        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, atLeast(1)).next();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }

    @Test
    void getTaskByCourse() throws SQLException {
        taskDao.getTaskByCourse(course);

        verify(mockConnectionPool, atLeast(1)).getConnection();
        verify(mockConnection, atLeast(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, atLeast(1)).executeQuery();
        verify(mockResultSet, atLeast(1)).next();
        verify(mockConnectionPool, atLeast(1)).releaseConnection(mockConnection);
    }

    @Test
    void addTaskToCourse() throws SQLException {
        taskDao.addTaskToCourse(task, course);

        verify(mockConnectionPool, atLeast(1)).getConnection();
        verify(mockConnection, atLeast(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(3)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, atLeast(1)).executeUpdate();
        verify(mockConnectionPool, atLeast(1)).releaseConnection(mockConnection);
    }

    @Test
    void addStudentToTask() throws SQLException {
        taskDao.addStudentToTask(user.getLogin(),task);
        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }

    @Test
    void deleteTaskFromCourse() throws SQLException {
        taskDao.deleteTaskFromCourse(task);

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }

    @Test
    void updateTask() throws SQLException {
        taskDao.updateTask(task.getId(), task);

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }

    @Test
    void addAnswerToTask() throws SQLException {
        taskDao.addAnswerToTask(user,task, new byte[]{1,2,3});

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());

        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }


    @Test
    void updateAnswerToTask() throws SQLException {
        taskDao.updateAnswerToTask(user,task, new byte[]{1,2,3});

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());

        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }

    @Test
    void getTask() throws SQLException {
        taskDao.getTask(task.getId());

        verify(mockConnectionPool, atLeast(1)).getConnection();
        verify(mockConnection, atLeast(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, atLeast(1)).executeQuery();
        verify(mockResultSet, atLeast(1)).next();
        verify(mockConnectionPool, atLeast(1)).releaseConnection(mockConnection);
    }

    @Test
    void getUserGradeForTask() throws SQLException {
        taskDao.getUserGradeForTask(user,task);

        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }
}