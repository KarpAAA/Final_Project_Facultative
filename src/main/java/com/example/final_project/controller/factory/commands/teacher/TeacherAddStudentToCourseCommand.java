package com.example.final_project.controller.factory.commands.teacher;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.services.CourseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Command of teacher role
 * Using to admit student to course
 */
public class TeacherAddStudentToCourseCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }
    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");

        String ifAdd = request.getParameter("add");
        String courseTitle = request.getParameter("courseTitle");
        String userLogin = request.getParameter("userLogin");

        CourseService courseService = new CourseService(connectionPool);
        courseService.addOrBanUserToCourse(ifAdd,userLogin,courseTitle);

        response.sendRedirect("/project/controller?command=teacherMessages");
    }

}
