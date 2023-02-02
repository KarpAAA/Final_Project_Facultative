package com.example.final_project.validation;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.database.entities.course.State;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ValidatorTest {

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
    void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);


        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(Boolean.TRUE,Boolean.FALSE);


    }

    @Test
    void validateUserNormalCase() {
        Validator validator = new Validator();
        List<String> errorList = validator.validateUser(mockConnectionPool, getTestUser());
        errorList.remove("login");
        Assertions.assertTrue(errorList.isEmpty());
    }
    @Test
    void validateUserPhone(){
        Validator validator = new Validator();

        UserBuilder userBuilder = getTestUserBuilder();
        userBuilder.setPhone("+593053");
        List<String> errorList = validator.validateUser(mockConnectionPool, userBuilder.getUser());
        errorList.remove("login");
        Assertions.assertTrue(errorList.size() == 1 && errorList.contains("phone"));

        userBuilder.setPhone("3553593053");
        errorList = validator.validateUser(mockConnectionPool, userBuilder.getUser());
        errorList.remove("login");
        Assertions.assertTrue(errorList.size() == 1 && errorList.contains("phone"));

        userBuilder.setPhone("+3553593053");
        errorList = validator.validateUser(mockConnectionPool, userBuilder.getUser());
        errorList.remove("login");
        Assertions.assertTrue(errorList.isEmpty());
    }
    @Test
    void validateUserEmail(){
        Validator validator = new Validator();

        UserBuilder userBuilder = getTestUserBuilder();
        userBuilder.setEmail("email");
        List<String> errorList = validator.validateUser(mockConnectionPool, userBuilder.getUser());
        errorList.remove("login");
        Assertions.assertTrue(errorList.size() == 1 && errorList.contains("email"));

        userBuilder = getTestUserBuilder();
        userBuilder.setEmail("email@");
        errorList = validator.validateUser(mockConnectionPool, userBuilder.getUser());
        errorList.remove("login");
        Assertions.assertTrue(errorList.size() == 1 && errorList.contains("email"));

        userBuilder = getTestUserBuilder();
        userBuilder.setEmail("@email");
        errorList = validator.validateUser(mockConnectionPool, userBuilder.getUser());
        errorList.remove("login");
        Assertions.assertTrue(errorList.size() == 1 && errorList.contains("email"));

        userBuilder = getTestUserBuilder();
        userBuilder.setEmail("email@email");
        errorList = validator.validateUser(mockConnectionPool, userBuilder.getUser());
        errorList.remove("login");
        Assertions.assertTrue(errorList.isEmpty());
    }
    @Test
    void validateUserName(){
        Validator validator = new Validator();

        UserBuilder userBuilder = getTestUserBuilder();
        userBuilder.setName("");
        List<String> errorList = validator.validateUser(mockConnectionPool, userBuilder.getUser());
        errorList.remove("login");
        Assertions.assertTrue(errorList.size() == 1 && errorList.contains("name"));



        userBuilder = getTestUserBuilder();
        userBuilder.setName("name");
        errorList = validator.validateUser(mockConnectionPool, userBuilder.getUser());
        errorList.remove("login");
        Assertions.assertTrue(errorList.isEmpty());
    }
    @Test
    void validateCourseNormalCase() {
        Validator validator = new Validator();
        List<String> errorList = validator.validateCourse(mockConnectionPool, getTestCourse());
        errorList.remove("title");
        Assertions.assertTrue(errorList.isEmpty());
    }
    @Test
    void validateDate() {

        String date;
        date = "2004-12-11";
        assertTrue(Validator.validateDate(date));


        date = "200-12-11";
        assertFalse(Validator.validateDate(date));

        date = "2004-1-11";
        assertFalse(Validator.validateDate(date));

        date = "2004-10-1";
        assertFalse(Validator.validateDate(date));
    }
}