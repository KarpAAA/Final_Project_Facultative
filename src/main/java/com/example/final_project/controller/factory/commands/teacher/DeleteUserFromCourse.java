package com.example.final_project.controller.factory.commands.teacher;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.database.dao.UserDao;
import com.example.final_project.services.CourseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteUserFromCourse implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if (isPost) executePost(request, response);
        else executeGet(request, response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        CourseService courseService = new CourseService(connectionPool);
        String login = request.getParameter("login");
        String courseTitle = request.getParameter("course");
        courseService.removeStudentFromCourse(login, courseTitle);
        response.sendRedirect("/project/controller?command=teacherDetailedCourse&title=" + courseTitle);
    }

    @Override
    public boolean securityCheck(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("login") != null && request.getParameter("course") != null;
    }

}
