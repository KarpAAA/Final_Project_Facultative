package com.example.final_project.controller.factory.commands.student;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.dto.CourseDTO;
import com.example.final_project.dto.UserDTO;
import com.example.final_project.services.CourseService;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StudentBuyCourse implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }
    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        UserDTO user = (UserDTO) request.getSession().getAttribute("user");
        CourseService courseService = new CourseService(connectionPool);
        CourseDTO courseDTO = courseService.findCourse(request.getParameter("course"));

        courseService.userBuyCourse(courseDTO,user);
        request.getSession().removeAttribute("user");
        request.getSession().setAttribute("user",new UserService(connectionPool).findUser(user.getLogin()));
        response.sendRedirect("/project/controller?command=studentCourses");
    }

}
