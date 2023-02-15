package com.example.final_project.controller.factory.commands.student;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.TaskService;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Command of student role
 * Using to show detailed course information to user
 */
public class StudentDetailedCourseCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseTitle = request.getParameter("title");
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");

        CourseService courseService = new CourseService(connectionPool);
        UserService userService = new UserService(connectionPool);
        TaskService taskService = new TaskService(connectionPool);

        CourseDTO course = courseService.findCourse(courseTitle);
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");

        String state = userService.getUserRegisteredState(user, course);
        if (state != null) {
            request.setAttribute("registerState", state);
            int grade = userService.getUserGradeForCourse(user.getLogin(), courseTitle);
            request.setAttribute("grade", grade);
        }

        request.setAttribute("course", course);
        request.setAttribute("tasksList", taskService.getTaskToCourse(course));
        request.setAttribute("pageToInclude", "/student/studentDetailedCourse.jsp");


        request.getRequestDispatcher("/student/studentPage.jsp").forward(request, response);
    }

}
