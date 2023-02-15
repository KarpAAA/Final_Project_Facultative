package com.example.final_project.controller.factory.commands.admin;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.user.User;
import com.example.final_project.database.entities.user.DirectorBuilder;
import com.example.final_project.database.entities.user.UserBuilder;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Command of admin role
 * Using to add Teacher(User with role Teacher) to database
 */
public class AdminAddTeacher implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }

    private void executeGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/admin/adminTeacherRegistration.jsp").forward(req,resp);
    }

    private void executePost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) req.getServletContext().getAttribute("connectionPool");
        UserService userService = new UserService(connectionPool);

        User user = formUser(req);
        List<String> errorList = userService.validateUser(req.getParameter("cpwd"), user);


        req.setAttribute("errorList", errorList);
        req.setAttribute("teacher", user);

        if(errorList.size()==0){
            userService.insertUser(user);
            resp.sendRedirect("/project/controller?command=adminUsers&action=teachers");
        }
        else{
            req.getRequestDispatcher("/admin/adminTeacherRegistration.jsp").forward(req,resp);
        }

    }

    /**
     * @param req received as user request
     *  req contains fields to create user
     * @return created user
     */
    private User formUser(HttpServletRequest req){

        UserBuilder userBuilder = DirectorBuilder.buildTeacher(
                        req.getParameter("login"),
                        req.getParameter("pwd"),
                        req.getParameter("name"),
                        req.getParameter("email"))
                .setSurname(req.getParameter("surname"));

        if(!Objects.equals(req.getParameter("age"), "")){
            userBuilder.setAge(Integer.parseInt(req.getParameter("age")));
        }
        else{
            userBuilder.setAge(-1);
        }
        if(!Objects.equals(req.getParameter("phone"), "")){
            userBuilder.setPhone(req.getParameter("phone"));
        }
        else{
            userBuilder.setPhone("");
        }
        return userBuilder.getUser();
    }

}