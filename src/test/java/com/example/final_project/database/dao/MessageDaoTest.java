package com.example.final_project.database.dao;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.message.Message;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.database.entities.message.Status;
import com.example.final_project.database.entities.user.Blocked_State;
import com.example.final_project.database.entities.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

class MessageDaoTest {

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

    MessageDao messagesDao;


    private User getTestUser() {
        return new User("login", "password", "name", Role.Student, "1@1", 18, new Date(1), "Surname",
                "+3809641345", new byte[]{1, 2, 3}, Blocked_State.UNLOCKED, 100);
    }

    private Message getTestMessage() {
        return new Message(1, "text", "subject", getTestUser(), getTestUser(), Status.UNREAD.toString());

    }

    @BeforeEach
    void setUp() throws SQLException, NoSuchMethodException {
        MockitoAnnotations.initMocks(this);
        messagesDao = new MessageDao(mockConnectionPool);

        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(mockResultSet.getString(6)).thenReturn("READ");


    }

    @Test
    void findMessagesByReceiver() throws SQLException {
        messagesDao.findMessagesByReceiver(getTestUser());
        verify(mockConnectionPool, atLeast(1)).getConnection();
        verify(mockConnection, atLeast(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(1)).setString(anyInt(), anyString());

        verify(mockPreparedStmnt, atLeast(1)).executeQuery();
        verify(mockResultSet, atLeast(1)).next();
        verify(mockConnectionPool, atLeast(1)).releaseConnection(mockConnection);
    }

    @Test
    void findMessagesBySender() throws SQLException {
        messagesDao.findMessagesBySender(getTestUser());
        verify(mockConnectionPool, atLeast(1)).getConnection();
        verify(mockConnection, atLeast(1)).prepareStatement(anyString());
        verify(mockPreparedStmnt, atLeast(1)).setString(anyInt(), anyString());

        verify(mockPreparedStmnt, atLeast(1)).executeQuery();
        verify(mockResultSet, atLeast(1)).next();
        verify(mockConnectionPool, atLeast(1)).releaseConnection(mockConnection);
    }

    @Test
    void sendMessage() throws SQLException {
        messagesDao.sendMessage(getTestMessage());

        verify(mockConnectionPool).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(5)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt).executeUpdate();
        verify(mockConnectionPool).releaseConnection(mockConnection);
    }

    @Test
    void changeStatus() throws SQLException {
        messagesDao.changeStatus(List.of(getTestMessage(), getTestMessage()));
        verify(mockConnectionPool).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(2)).setInt(anyInt(), anyInt());
        verify(mockPreparedStmnt, times(2)).executeUpdate();
        verify(mockConnectionPool).releaseConnection(mockConnection);
    }

    @Test
    void clearUserMessages() throws SQLException {
        messagesDao.clearUserMessages(getTestUser());
        verify(mockConnectionPool).getConnection();
        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStmnt, times(1)).setString(anyInt(), anyString());
        verify(mockPreparedStmnt, times(1)).executeUpdate();
        verify(mockConnectionPool).releaseConnection(mockConnection);
    }
}