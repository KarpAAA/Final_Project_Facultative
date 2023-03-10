package com.example.final_project.controller.factory.commands.admin;

import com.example.final_project.controller.factory.commands.Command;
import com.example.final_project.database.connection.ConnectionPool;
import com.example.final_project.database.entities.user.Role;
import com.example.final_project.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Command of admin role
 * Using to show all users by specified role
 */
public class AdminUsersCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean isPost = "POST".equals(request.getMethod());
        if(isPost)executePost(request,response);
        else executeGet(request,response);
    }

    private void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    private void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool connectionPool = (ConnectionPool) request.getServletContext().getAttribute("connectionPool");
        UserService userService = new UserService(connectionPool);

        if(request.getParameter("action").compareToIgnoreCase("teachers") == 0){
            request.setAttribute("map", userService.getUserToCourseMapByRole(Role.Teacher));
            request.setAttribute("users", "teachers");
        }
        else{
            request.setAttribute("map", userService.getUserToCourseMapByRole(Role.Student));
            request.setAttribute("users", "students");
        }


        request.setAttribute("pageToInclude", "/admin/adminUsersAdmin.jsp");
        request.getRequestDispatcher("/admin/adminPage.jsp").forward(request, response);
    }

    @Override
    public boolean securityCheck(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("action")!=null;
    }
}

