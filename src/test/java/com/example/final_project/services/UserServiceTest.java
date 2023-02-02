package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.database.entities.user.Blocked_State;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.database.entities.user.UserBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserServiceTest {
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



    private User getTestUser() {
        return new User("login", "password", "name", Role.Student, "1@1", 18, new Date(1), "Surname",
                "+3809641345", new byte[]{1, 2, 3}, Blocked_State.UNLOCKED, 100);
    }
    private UserBuilder getTestUserBuilder(){
        UserBuilder userBuilder = new UserBuilder();
        User user = getTestUser();
        userBuilder.setLogin(user.getLogin());
        userBuilder.setPassword(user.getPassword());
        userBuilder.setName(user.getName());
        userBuilder.setRole(user.getRole());
        userBuilder.setEmail(user.getEmail());
        userBuilder.setAge(user.getAge());
        userBuilder.setRegistrationDate(user.getRegistrationDate());
        userBuilder.setSurname(user.getSurname());
        userBuilder.setPhone(user.getPhone());
        userBuilder.setPhoto(user.getPhoto());
        userBuilder.setBlocked_state(user.getBlocked_state());
        return userBuilder;
    }
    @BeforeEach
    void setUp() throws SQLException{
        MockitoAnnotations.initMocks(this);


        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);


    }
    @Test
    void validateUser() {
        UserService userService = new UserService(mockConnectionPool);
        UserBuilder userBuilder = getTestUserBuilder();
        userBuilder.setAge(0);
        List<String> errorList = userService.validateUser("123",userBuilder.getUser());

        Assertions.assertTrue(errorList.size()==2);
    }
}