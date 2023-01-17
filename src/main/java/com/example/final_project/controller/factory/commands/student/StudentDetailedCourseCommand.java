package com.example.final_project.controller.factory.commands.student;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.dao.CoursesDao;
import com.example.final_project.database.dao.UserDao;
import com.example.final_project.entities.course.Course;
import com.example.final_project.entities.user.User;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;




public class StudentDetailedCourseCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String courseTitle = request.getParameter("title");
        ConnectionPool connectionPool =  (ConnectionPool) request.getSession().getAttribute("connectionPool");
        CourseService courseService = new CourseService(connectionPool);
        UserService userService = new UserService(connectionPool);

        Course course = courseService.findCourse(courseTitle);
        User user = (User) request.getSession().getAttribute("user");

        String state = userService.getUserRegisteredState(user, course);
        if(state!=null){
            request.setAttribute("registerState", state);
            int grade = userService.getUserGradeForCourse(user.getLogin(),courseTitle);
            request.setAttribute("grade", grade);
        }

        request.setAttribute("course", course);
        request.setAttribute("pageToInclude", "/client/detailedCourse.jsp");


        request.getRequestDispatcher("/client/clientPage.jsp").forward(request, response);
    }

}
