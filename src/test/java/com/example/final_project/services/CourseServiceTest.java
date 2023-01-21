package com.example.final_project.services;

import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.course.Course;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.database.entities.course.CourseBuilder;
import com.example.final_project.database.entities.course.State;
import com.example.final_project.database.entities.user.Blocked_State;
import com.example.final_project.database.entities.user.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CourseServiceTest {
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
                        "+3809641345", new byte[]{1, 2, 3}, Blocked_State.UNLOCKED)
                , new Date(1), new Date(1), 120, 120, 120, State.InProgress, new byte[]{1, 2, 3});
    }


    private CourseBuilder getTestCourseBuilder(){
        CourseBuilder courseBuilder = new CourseBuilder();
        Course course = getTestCourse();

        courseBuilder.setFinishDate(course.getFinishDate());
        courseBuilder.setStartDate(course.getStartDate());
        courseBuilder.setTopic(course.getTitle());
        courseBuilder.setTopic(course.getTopic());
        courseBuilder.setDescription(course.getDescription());
        courseBuilder.setTeacher(course.getTeacher());
        courseBuilder.setMaxStudentsAmount(course.getMaxStudentsAmount());
        courseBuilder.setCurrentStudentsAmount(course.getCurrentStudentsAmount());
        courseBuilder.setPrice(course.getPrice());
        courseBuilder.setState(course.getState());
        courseBuilder.setPhoto(course.getPhoto());

        return courseBuilder;
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
    void validateCourse() {
        CourseService courseService = new CourseService(mockConnectionPool);
        CourseBuilder courseBuilder = getTestCourseBuilder();
        courseBuilder.setStartDate(null);
        List<String> errorList = courseService.validateCourse(courseBuilder.buildCourse());
        errorList.remove("title");
        Assertions.assertTrue(errorList.size()==1 &&errorList.contains("startDate"));

        courseBuilder = getTestCourseBuilder().setFinishDate(null);
        errorList = courseService.validateCourse(courseBuilder.buildCourse());
        errorList.remove("title");
        Assertions.assertTrue(errorList.size()==1 &&errorList.contains("finishDate"));

        errorList = courseService.validateCourse(getTestCourseBuilder().buildCourse());
        errorList.remove("title");
        Assertions.assertTrue(errorList.isEmpty());
    }

    @Test
    void validateCourseState() {
        CourseService courseService = new CourseService(mockConnectionPool);
        Assertions.assertEquals(courseService.validateCourseState("InProgress"),State.InProgress);
        Assertions.assertEquals(courseService.validateCourseState("Finished"),State.Finished);
        Assertions.assertEquals(courseService.validateCourseState("NotStarted"),State.NotStarted);
        Assertions.assertEquals(courseService.validateCourseState(null),State.NotStarted);
    }
}